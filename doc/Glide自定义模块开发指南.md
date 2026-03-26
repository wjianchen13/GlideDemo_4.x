# Glide 自定义模块开发指南

> 基于 Glide 4.13.2，以加载 PAG 动画文件为例，说明如何自定义 Glide 的各个模块。

---

## 一、整体架构

Glide 加载资源的完整数据流：

```
Model（URL字符串）
  ↓ ModelLoader（获取数据）
Data（InputStream / ByteBuffer）
  ↓ ResourceDecoder（解码数据）
Resource（PAGFile）
  ↓ ResourceTranscoder（转码，可选）
TranscodedResource（PagData）
  ↓ ViewTarget（设置到 View 显示）
View（PAGView）
```

对应需要自定义的模块：

| 模块               | 接口                                    | 作用                           | 是否必须                              |
|--------------------|-----------------------------------------|-------------------------------|--------------------------------------|
| ModelLoader        | `ModelLoader<Model, Data>`              | 获取原始数据（网络下载等）      | 可选，Glide 内置了网络加载              |
| ResourceDecoder    | `ResourceDecoder<Data, Resource>`       | 将原始数据解码为资源对象        | **必须**                               |
| Resource           | `Resource<T>`                           | 资源的包装类，管理生命周期和回收 | **必须**                               |
| ResourceTranscoder | `ResourceTranscoder<Z, R>`              | 将资源从一种类型转为另一种类型   | 可选，不需要转码时 Glide 使用 UnitTranscoder 透传 |
| ViewTarget         | `CustomViewTarget<View, R>`             | 将资源设置到自定义 View 上       | **必须**（自定义 View 时）              |
| GlideModule        | `LibraryGlideModule` / `AppGlideModule` | 注册以上所有自定义组件           | **必须**                               |

---

## 二、各模块详细说明

---

### 2.1 Resource —— 资源包装类

**作用**：包装解码后的资源对象，管理其生命周期。当 LruCache 淘汰资源时，Glide 会调用 `recycle()` 释放资源。

**接口定义**：

```java
public interface Resource<Z> {

    /**
     * 返回资源的 Class 类型
     * Glide 内部用于 Registry 匹配
     */
    @NonNull
    Class<Z> getResourceClass();

    /**
     * 返回被包装的资源对象
     * 在回调 onResourceReady() 中，Glide 调用此方法获取真正的资源
     */
    @NonNull
    Z get();

    /**
     * 返回资源占用的内存大小（字节数）
     *
     * 重要：必须返回真实大小！
     * - LruCache 根据此值计算已用内存，决定何时淘汰旧资源
     * - 如果返回 1，LruCache 几乎永远不会触发淘汰，导致内存持续增长
     * - 如果是 Bitmap：返回 bitmap.getAllocationByteCount()
     * - 如果是 PAGFile：返回文件的字节数 bytes.length
     */
    int getSize();

    /**
     * 释放资源
     *
     * 在以下情况被调用：
     * 1. LruCache 满了，淘汰最久没用的资源时
     * 2. 请求被取消且资源不需要缓存时
     *
     * 在此方法中做清理工作：
     * - 如果资源有显式释放方法（如 bitmap.recycle()），在这里调用
     * - 如果没有（如 PAGFile），断开引用让 GC 回收
     * - 如果资源可复用（如 Bitmap），放回对象池（BitmapPool）
     */
    void recycle();
}
```

**PAG 示例 —— PAGFileResource**：

```java
public class PAGFileResource implements Resource<PAGFile> {
    private PAGFile pagFile;
    private final int size;

    public PAGFileResource(@NonNull PAGFile pagFile, int size) {
        this.pagFile = pagFile;
        this.size = size;
    }

    @NonNull
    @Override
    public Class<PAGFile> getResourceClass() {
        return PAGFile.class;
    }

    @NonNull
    @Override
    public PAGFile get() {
        return pagFile;
    }

    @Override
    public int getSize() {
        return size;  // 返回 PAG 文件的真实字节数
    }

    @Override
    public void recycle() {
        // PAGFile 没有显式释放方法，断开引用让 GC 尽早回收
        pagFile = null;
    }
}
```

---

### 2.2 ResourceDecoder —— 解码器

**作用**：将原始数据（InputStream / ByteBuffer）解码为资源对象（如 PAGFile）。

**接口定义**：

```java
public interface ResourceDecoder<T, Z> {
    // T = 输入数据类型（InputStream / ByteBuffer）
    // Z = 输出资源类型（PAGFile / Bitmap）

    /**
     * 判断当前解码器是否能处理这份数据
     *
     * 重要：
     * - 必须通过读取数据特征（如文件头魔数）来判断，不要无条件返回 true
     * - 如果无条件返回 true，会拦截所有数据，导致其他解码器没机会处理
     * - 读取数据后注意恢复位置（ByteBuffer.position / InputStream.reset）
     *
     * @param source  原始数据
     * @param options 加载选项
     * @return true 表示可以处理，Glide 会接着调用 decode()
     */
    boolean handles(@NonNull T source, @NonNull Options options) throws IOException;

    /**
     * 将原始数据解码为 Resource 包装的资源对象
     *
     * @param source  原始数据
     * @param width   目标宽度（可用于缩放，PAG 不需要）
     * @param height  目标高度（可用于缩放，PAG 不需要）
     * @param options 加载选项
     * @return Resource 包装的资源对象，返回 null 表示解码失败
     */
    @Nullable
    Resource<Z> decode(@NonNull T source, int width, int height, @NonNull Options options)
        throws IOException;
}
```

**PAG 示例 —— ByteBuffer 输入（配合 Glide 内置网络加载 + 磁盘缓存）**：

```java
public class PAGFileResourceDecoder implements ResourceDecoder<ByteBuffer, PAGFile> {

    private static final byte[] PAG_MAGIC = {0x50, 0x41, 0x47}; // "PAG" 文件头

    @Override
    public boolean handles(@NonNull ByteBuffer source, @NonNull Options options) throws IOException {
        // 检查文件头魔数，确认是 PAG 文件
        if (source.remaining() < PAG_MAGIC.length) {
            return false;
        }
        int position = source.position();
        for (byte magicByte : PAG_MAGIC) {
            if (source.get() != magicByte) {
                source.position(position); // 恢复位置，不影响后续解码器
                return false;
            }
        }
        source.position(position); // 恢复位置
        return true;
    }

    @Override
    public Resource<PAGFile> decode(@NonNull ByteBuffer source, int width, int height,
            @NonNull Options options) throws IOException {
        byte[] bytes = new byte[source.remaining()];
        source.get(bytes);

        PAGFile pagFile = PAGFile.Load(bytes);
        if (pagFile == null) {
            throw new IOException("Failed to decode PAG file");
        }
        return new PAGFileResource(pagFile, bytes.length);
    }
}
```

**PAG 示例 —— InputStream 输入（配合自定义 ModelLoader）**：

```java
public class PAGFileStreamDecoder implements ResourceDecoder<InputStream, PAGFile> {

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        // 如果配合自定义 ModelLoader 使用，ModelLoader.handles() 已做过滤
        // 这里可以返回 true，也可以再次检查文件头
        return true;
    }

    @Override
    public Resource<PAGFile> decode(@NonNull InputStream source, int width, int height,
            @NonNull Options options) throws IOException {
        // 将 InputStream 读取为 byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = source.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] bytes = baos.toByteArray();

        PAGFile pagFile = PAGFile.Load(bytes);
        if (pagFile == null) {
            throw new IOException("Failed to decode PAG file");
        }
        return new PAGFileResource(pagFile, bytes.length);
    }
}
```

**两种输入类型如何选择**：

| 输入类型 | 适用场景 | 优缺点 |
|----------|----------|--------|
| `ByteBuffer` | 配合 Glide 内置网络加载 + 磁盘缓存 | 依赖磁盘缓存才有 ByteBuffer（ByteBufferFileLoader 读取缓存文件产出） |
| `InputStream` | 通用性更强 | 不依赖缓存也能工作，网络和缓存都能产出 InputStream |

**建议**：优先使用 `InputStream` 作为输入类型，适用性更强。

---

### 2.3 ModelLoader —— 数据加载器（可选）

**作用**：将 Model（URL、文件路径等）转换为原始数据（InputStream / ByteBuffer）。如果 Glide 内置的网络加载能满足需求，可以不自定义。

**什么时候需要自定义 ModelLoader**：

- 需要自定义网络请求逻辑（如添加自定义 Header、使用 OkHttp 等）
- 需要拦截特定类型的 URL（如只拦截 .pag 结尾的 URL）
- 数据来源不是标准 URL（如 Base64 字符串、自定义协议等）

**涉及三个类**：

#### 2.3.1 ModelLoader 接口

```java
public interface ModelLoader<Model, Data> {
    // Model = 输入模型类型（GlideUrl / String / Uri）
    // Data  = 输出数据类型（InputStream / ByteBuffer）

    /**
     * 判断是否能处理这个 Model
     *
     * 注意：此方法在主线程调用，不要做耗时操作
     *
     * @param model 要加载的模型（如 URL）
     * @return true 表示可以处理
     */
    boolean handles(@NonNull Model model);

    /**
     * 构建加载数据所需的信息
     *
     * @param model   要加载的模型
     * @param width   目标宽度
     * @param height  目标高度
     * @param options 加载选项
     * @return LoadData 包含缓存 Key 和 DataFetcher，返回 null 表示无法加载
     */
    @Nullable
    LoadData<Data> buildLoadData(@NonNull Model model, int width, int height, @NonNull Options options);
}
```

#### 2.3.2 DataFetcher —— 真正执行数据获取

```java
public interface DataFetcher<T> {
    // T = 数据类型（InputStream / ByteBuffer）

    /**
     * 在子线程中执行，获取数据
     *
     * 成功时调用 callback.onDataReady(data)
     * 失败时调用 callback.onLoadFailed(exception)
     *
     * @param priority 加载优先级
     * @param callback 结果回调
     */
    void loadData(@NonNull Priority priority, @NonNull DataCallback<? super T> callback);

    /**
     * 取消正在进行的加载
     * 当请求被取消时调用（如页面关闭、重复加载）
     * 在此方法中关闭网络连接等，释放资源
     */
    void cancel();

    /**
     * 清理资源
     * 数据使用完毕后调用，关闭流、释放连接等
     */
    void cleanup();

    /**
     * 返回数据的 Class 类型
     * Glide 用于 Registry 匹配 Decoder
     */
    @NonNull
    Class<T> getDataClass();

    /**
     * 返回数据来源类型
     *
     * 影响磁盘缓存策略的判断：
     * - DataSource.REMOTE：网络数据，可被磁盘缓存
     * - DataSource.LOCAL：本地数据
     * - DataSource.DATA_DISK_CACHE：来自数据缓存
     * - DataSource.RESOURCE_DISK_CACHE：来自资源缓存
     * - DataSource.MEMORY_CACHE：来自内存缓存
     */
    @NonNull
    DataSource getDataSource();
}
```

#### 2.3.3 ModelLoaderFactory —— 创建 ModelLoader 的工厂

```java
public interface ModelLoaderFactory<Model, Data> {

    /**
     * 创建 ModelLoader 实例
     *
     * @param multiFactory 可用于获取其他已注册的 ModelLoader（实现委托链）
     * @return ModelLoader 实例
     */
    @NonNull
    ModelLoader<Model, Data> build(@NonNull MultiModelLoaderFactory multiFactory);

    /**
     * 释放资源
     * 当 Glide 被销毁时调用
     */
    void teardown();
}
```

**PAG 示例**：

```java
// ---- ModelLoader ----
public final class PAGModelLoader implements ModelLoader<GlideUrl, InputStream> {

    @Override
    public boolean handles(GlideUrl model) {
        String url = model.toStringUrl();
        int queryIndex = url.indexOf('?');
        String path = queryIndex > 0 ? url.substring(0, queryIndex) : url;
        return path.endsWith(".pag");  // 只拦截 .pag 结尾的 URL
    }

    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl model, int width, int height, Options options) {
        return new LoadData<>(new ObjectKey(model), new PAGDataFetcher(model));
    }
}

// ---- DataFetcher ----
public class PAGDataFetcher implements DataFetcher<InputStream> {
    private final GlideUrl url;
    private HttpURLConnection conn;

    public PAGDataFetcher(GlideUrl url) {
        this.url = url;
    }

    @Override
    public void loadData(Priority priority, DataCallback<? super InputStream> callback) {
        try {
            conn = (HttpURLConnection) new URL(url.toStringUrl()).openConnection();
            conn.setConnectTimeout(15000);
            conn.setReadTimeout(15000);
            callback.onDataReady(conn.getInputStream());
        } catch (Exception e) {
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cancel() {
        if (conn != null) {
            conn.disconnect();
        }
    }

    @Override
    public void cleanup() {
        if (conn != null) {
            conn.disconnect();
        }
    }

    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;  // 标记为网络数据
    }
}

// ---- Factory ----
public class PAGModelLoaderFactory implements ModelLoaderFactory<GlideUrl, InputStream> {

    @Override
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new PAGModelLoader();
    }

    @Override
    public void teardown() {
        // 无需清理
    }
}
```

---

### 2.4 ResourceTranscoder —— 转码器（可选）

**作用**：将解码后的资源从一种类型转为另一种类型。如果解码产物就是你最终需要的类型，不需要自定义转码器。

**什么时候需要自定义 Transcoder**：

- 解码产物是 A 类型，但你想让回调接收 B 类型
- 需要在传递给 Target 之前做额外包装

**接口定义**：

```java
public interface ResourceTranscoder<Z, R> {
    // Z = 输入类型（解码产物，如 PAGFile）
    // R = 输出类型（最终类型，如 PagData）

    /**
     * 将一种 Resource 转换为另一种 Resource
     *
     * 重要：
     * - toTranscode 是原始 Resource，持有原始资源的引用
     * - 新建的 Resource 应该持有 toTranscode 的引用
     * - 在新 Resource 的 recycle() 中委托给 toTranscode.recycle()
     * - 这样确保原始资源能被正确回收（参考 LazyBitmapDrawableResource 的设计）
     *
     * @param toTranscode 解码后的原始 Resource
     * @param options     加载选项
     * @return 转码后的 Resource
     */
    @Nullable
    Resource<R> transcode(@NonNull Resource<Z> toTranscode, @NonNull Options options);
}
```

**PAG 示例 —— PAGFile 转 PagData**：

```java
// ---- 转码后的数据类 ----
public class PagData {
    public PAGFile pagFile;
}

// ---- 转码后的 Resource 包装 ----
public class PagDataResource implements Resource<PagData> {
    // 持有原始 Resource 引用，确保原始资源能被正确回收
    private final Resource<PAGFile> originalResource;
    private final PagData pagData;

    public PagDataResource(@NonNull Resource<PAGFile> originalResource) {
        this.originalResource = originalResource;
        this.pagData = new PagData();
        this.pagData.pagFile = originalResource.get();
    }

    @Override
    public Class<PagData> getResourceClass() { return PagData.class; }

    @Override
    public PagData get() { return pagData; }

    @Override
    public int getSize() { return originalResource.getSize(); }

    @Override
    public void recycle() {
        // 委托给原始 Resource 回收，不要自己直接释放 PAGFile
        originalResource.recycle();
    }
}

// ---- 转码器 ----
public class PAGFileToPagDataTranscoder implements ResourceTranscoder<PAGFile, PagData> {

    @Override
    public Resource<PagData> transcode(@NonNull Resource<PAGFile> toTranscode, @NonNull Options options) {
        // 把原始 Resource 整个传进去，由 PagDataResource 管理生命周期
        return new PagDataResource(toTranscode);
    }
}
```

---

### 2.5 ViewTarget —— 自定义 View 目标

**作用**：将加载完成的资源设置到自定义 View 上。

**为什么用 CustomViewTarget 而不是 CustomTarget**：

|                      | CustomViewTarget | CustomTarget |
|----------------------|---|---|
| 绑定 View              | 是，通过 View.tag 存储请求 | 否 |
| 重复加载自动取消旧请求          | ✅ 自动 | ❌ 需手动管理 |
| Activity 销毁自动取消      | ✅ 自动 | ✅ 自动 |

**接口定义**：

```java
public abstract class CustomViewTarget<V extends View, R> {
    // V = View 类型（如 PAGView）
    // R = 资源类型（如 PagData / PAGFile）

    /**
     * 构造方法，传入目标 View
     * Glide 会自动将 Request 存入 view.tag
     */
    public CustomViewTarget(@NonNull V view) { ... }

    /**
     * 资源加载成功
     * 在此方法中将资源设置到 View 上
     *
     * @param resource   加载完成的资源
     * @param transition 过渡动画（可为 null）
     */
    public abstract void onResourceReady(@NonNull R resource, @Nullable Transition<? super R> transition);

    /**
     * 资源被清除
     *
     * 在以下情况被调用：
     * 1. 同一个 View 重新加载新资源时，旧资源被清除
     * 2. Activity 销毁时，RequestManager 清除请求
     * 3. 手动调用 Glide.with(context).clear(target)
     *
     * 在此方法中：
     * - 停止播放、动画等
     * - 解除 View 对资源的引用（避免使用已回收的资源）
     */
    protected abstract void onResourceCleared(@Nullable Drawable placeholder);

    /**
     * 加载失败
     * 可设置错误占位图或其他提示
     */
    public abstract void onLoadFailed(@Nullable Drawable errorDrawable);

    /**
     * 获取目标 View
     */
    @NonNull
    public V getView() { ... }
}
```

**PAG 示例 —— PagDataViewTarget（支持自定义处理回调）**：

```java
public class PagDataViewTarget extends CustomViewTarget<PAGView, PagData> {

    /**
     * 可选回调：在设置到 PAGView 之前，让调用方做自定义处理
     * 不同页面可以有不同的替换逻辑（替换图片、文字等）
     */
    public interface OnPagFileReadyListener {
        void onReady(PAGFile pagFile);
    }

    private final OnPagFileReadyListener listener;

    public PagDataViewTarget(@NonNull PAGView view) {
        this(view, null);
    }

    public PagDataViewTarget(@NonNull PAGView view, @Nullable OnPagFileReadyListener listener) {
        super(view);
        this.listener = listener;
    }

    @Override
    public void onResourceReady(@NonNull PagData pagData, @Nullable Transition<? super PagData> transition) {
        // 先让调用方做自定义处理（替换图片、文字等）
        if (listener != null) {
            listener.onReady(pagData.pagFile);
        }
        // 处理完再设置到 PAGView
        PAGView pagView = getView();
        pagView.setComposition(pagData.pagFile);
        pagView.setRepeatCount(0);
        pagView.play();
    }

    @Override
    protected void onResourceCleared(@Nullable Drawable placeholder) {
        PAGView pagView = getView();
        pagView.stop();
        pagView.setComposition(null);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        PAGView pagView = getView();
        pagView.stop();
        pagView.setComposition(null);
    }
}
```

---

### 2.6 GlideModule —— 注册组件

**作用**：将自定义的 ModelLoader、Decoder、Transcoder 等注册到 Glide 的 Registry 中。

**两种 GlideModule**：

| 类型 | 继承自 | 特点 |
|------|--------|------|
| `AppGlideModule` | `AppGlideModule` | 全局唯一，项目只能有一个 |
| `LibraryGlideModule` | `LibraryGlideModule` | 可以有多个，适合模块化开发 |

**注册顺序**：Glide 初始化时，先执行所有 `LibraryGlideModule`，再执行 `AppGlideModule`。

**注册方法**：

```java
// 注册 Decoder：prepend 插到前面（优先匹配），append 插到后面
registry.prepend(数据类型.class, 资源类型.class, new 自定义Decoder());
registry.append(数据类型.class, 资源类型.class, new 自定义Decoder());

// 注册 ModelLoader：同样有 prepend 和 append
registry.prepend(模型类型.class, 数据类型.class, new 自定义ModelLoaderFactory());

// 注册 Transcoder：使用 register
registry.register(输入类型.class, 输出类型.class, new 自定义Transcoder());
```

**prepend vs append**：

| 方法 | 插入位置 | 匹配优先级 | 使用场景 |
|------|----------|------------|----------|
| `prepend` | 列表头部 | 最先被匹配 | 自定义 Decoder（推荐，避免被默认 Decoder 拦截） |
| `append` | 列表尾部 | 最后被匹配 | 作为兜底 Decoder |

**PAG 示例**：

```java
@GlideModule
public class MyAppGlideModule extends AppGlideModule {
    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {

        // ========== 方式一：只自定义 Decoder（使用 Glide 内置网络加载） ==========
        // 适用于：不需要自定义网络请求逻辑
        // 数据流：Glide 内置下载 → 磁盘缓存 → ByteBuffer → PAGFileResourceDecoder → PAGFile
        registry.prepend(ByteBuffer.class, PAGFile.class, new PAGFileResourceDecoder());


        // ========== 方式二：自定义 ModelLoader + Decoder ==========
        // 适用于：需要自定义网络请求逻辑（如自定义 Header、拦截特定 URL）
        // 数据流：PAGModelLoader 下载 → InputStream → PAGFileStreamDecoder → PAGFile
        registry.prepend(GlideUrl.class, InputStream.class, new PAGModelLoaderFactory());
        registry.prepend(InputStream.class, PAGFile.class, new PAGFileStreamDecoder());


        // ========== 方式三：完整自定义（ModelLoader + Decoder + Transcoder） ==========
        // 适用于：需要在设置到 View 之前做额外处理（如替换 PAG 文件中的图片/文字）
        // 数据流：下载 → InputStream → PAGFile（解码） → PagData（转码） → PagDataViewTarget
        registry.prepend(InputStream.class, PAGFile.class, new PAGFileStreamDecoder());
        registry.register(PAGFile.class, PagData.class, new PAGFileToPagDataTranscoder());
    }
}
```

---

## 三、Activity 中使用

### 方式一：直接使用（不需要转码）

```java
Glide.with(this)
        .as(PAGFile.class)              // 指定最终输出类型为 PAGFile
        .load("https://example.com/animation.pag")
        .into(new PAGViewTarget(pagView));
```

### 方式二：使用转码 + 自定义处理

```java
Glide.with(this)
        .as(PagData.class)              // 指定最终输出类型为 PagData
        .load("https://example.com/animation.pag")
        .into(new PagDataViewTarget(pagView, pagFile -> {
            // 在这里做自定义处理，每个页面可以不同
            // pagFile.getTextData(0).setText("自定义文字");
            // pagFile.replaceImage(0, customImage);
        }));
```

### 方式三：使用 CustomTarget 手动设置（需自行管理取消）

```java
public void onLoad(View v) {
    // 取消旧请求
    Object tag = v.getTag();
    if (tag instanceof CustomTarget) {
        Glide.with(this).clear((CustomTarget) tag);
    }

    CustomTarget<PagData> target = new CustomTarget<PagData>() {
        @Override
        public void onResourceReady(@NonNull PagData pagData, @Nullable Transition<? super PagData> transition) {
            pagView.setComposition(pagData.pagFile);
            pagView.setRepeatCount(0);
            pagView.play();
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
            pagView.stop();
            pagView.setComposition(null);
        }
    };
    v.setTag(target);

    Glide.with(this)
            .as(PagData.class)
            .load("https://example.com/animation.pag")
            .into(target);
}
```

---

## 四、注意事项

### 4.1 Resource 的 getSize() 必须返回真实大小

```java
// ❌ 错误：LruCache 无法正确计算内存，淘汰机制失效
public int getSize() { return 1; }

// ✅ 正确：LruCache 能正确触发淘汰
public int getSize() { return bytes.length; }
```

### 4.2 Resource 的 recycle() 必须正确释放资源

```java
// ❌ 错误：空实现，资源无法被回收
public void recycle() { }

// ✅ 正确：断开引用，让 GC 回收
public void recycle() { pagFile = null; }
```

### 4.3 转码器中必须保留原始 Resource 引用

```java
// ❌ 错误：丢弃原始 Resource，其 recycle() 永远不会被调用
public Resource<PagData> transcode(Resource<PAGFile> toTranscode, Options options) {
    PAGFile pagFile = toTranscode.get();
    return new PagDataResource(new PagData(pagFile), toTranscode.getSize());
}

// ✅ 正确：保留原始 Resource，recycle() 时委托给它
public Resource<PagData> transcode(Resource<PAGFile> toTranscode, Options options) {
    return new PagDataResource(toTranscode);  // 传入整个 Resource
}
```

### 4.4 Decoder 的 handles() 中读取数据后要恢复位置

```java
// ByteBuffer 恢复位置
int position = source.position();
// ... 读取文件头判断 ...
source.position(position);  // 恢复位置

// InputStream 恢复位置（需要 mark/reset 支持）
source.mark(headerLength);
// ... 读取文件头判断 ...
source.reset();  // 恢复位置
```

### 4.5 自定义 Decoder 建议使用 prepend 注册

```java
// ✅ 推荐：自定义 Decoder 优先匹配，避免被 Glide 默认 Decoder 拦截
registry.prepend(InputStream.class, PAGFile.class, new PAGFileStreamDecoder());

// ⚠️ 不推荐：排在默认 Decoder 后面，可能被拦截
registry.append(InputStream.class, PAGFile.class, new PAGFileStreamDecoder());
```

### 4.6 使用 CustomTarget 时需手动管理请求取消

```java
// CustomViewTarget：Glide 通过 View.tag 自动管理，无需额外处理
.into(new PagDataViewTarget(pagView));

// CustomTarget：每次 new 新对象，Glide 无法自动取消旧请求
// 必须手动保存引用并在重复加载时 clear
```

---

## 五、快速对照表

| 你想做什么 | 需要自定义哪些模块 |
|-----------|-------------------|
| 加载自定义格式文件（如 PAG、Lottie） | Resource + Decoder + ViewTarget + GlideModule |
| 自定义网络请求（如加 Header） | 上面 + ModelLoader |
| 加载前需要对资源做额外处理 | 上面 + Transcoder |

| 模块 | 注册方法 |
|------|----------|
| Decoder | `registry.prepend(Data.class, Resource.class, decoder)` |
| ModelLoader | `registry.prepend(Model.class, Data.class, factory)` |
| Transcoder | `registry.register(Input.class, Output.class, transcoder)` |
