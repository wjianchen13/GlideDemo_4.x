modelClass 输入原始模型 比如Url, 文件路径
dataClass 数据类 比如InputStream， FileDescriptor
resourceClass 被解码后的资源，比如Bitmap， GiftDrawable
transcodeClass 被转码后的资源类，比如Byte[], BitmapDrawable


modelLoaderRegistry保存的数据，按照顺序
.append(Bitmap.class, Bitmap.class, UnitModelLoader.Factory.<Bitmap>getInstance())
.append(GifDecoder.class, GifDecoder.class, UnitModelLoader.Factory.<GifDecoder>getInstance())
.append(File.class, ByteBuffer.class, new ByteBufferFileLoader.Factory())
.append(File.class, InputStream.class, new FileLoader.StreamFactory())
.append(File.class, ParcelFileDescriptor.class, new FileLoader.FileDescriptorFactory())
.append(File.class, File.class, UnitModelLoader.Factory.<File>getInstance())
.append(int.class, InputStream.class, resourceLoaderStreamFactory)
.append(int.class, ParcelFileDescriptor.class, resourceLoaderFileDescriptorFactory)
.append(Integer.class, InputStream.class, resourceLoaderStreamFactory)
.append(Integer.class, ParcelFileDescriptor.class, resourceLoaderFileDescriptorFactory)
.append(Integer.class, Uri.class, resourceLoaderUriFactory)
.append(int.class, AssetFileDescriptor.class, resourceLoaderAssetFileDescriptorFactory)
.append(Integer.class, AssetFileDescriptor.class, resourceLoaderAssetFileDescriptorFactory)
.append(int.class, Uri.class, resourceLoaderUriFactory)
.append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
.append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory<Uri>())
.append(String.class, InputStream.class, new StringLoader.StreamFactory())
.append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
.append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets()))
.append(Uri.class,AssetFileDescriptor.class,new AssetUriLoader.FileDescriptorFactory(context.getAssets()))
.append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context))
.append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context));
.append(Uri.class, InputStream.class, new QMediaStoreUriLoader.InputStreamFactory(context));
.append(Uri.class,ParcelFileDescriptor.class,new QMediaStoreUriLoader.FileDescriptorFactory(context));
.append(Uri.class, InputStream.class, new UriLoader.StreamFactory(contentResolver))
.append(Uri.class,ParcelFileDescriptor.class,new UriLoader.FileDescriptorFactory(contentResolver))
.append(Uri.class,AssetFileDescriptor.class,new UriLoader.AssetFileDescriptorFactory(contentResolver))
.append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory())
.append(URL.class, InputStream.class, new UrlLoader.StreamFactory())
.append(Uri.class, File.class, new MediaStoreFileLoader.Factory(context))
.append(GlideUrl.class, InputStream.class, new HttpGlideUrlLoader.Factory())
.append(byte[].class, ByteBuffer.class, new ByteArrayLoader.ByteBufferFactory())
.append(byte[].class, InputStream.class, new ByteArrayLoader.StreamFactory())
.append(Uri.class, Uri.class, UnitModelLoader.Factory.<Uri>getInstance())
.append(Drawable.class, Drawable.class, UnitModelLoader.Factory.<Drawable>getInstance())






























RequestBuilder
into(ImageView)
into(
@NonNull Y target,
@Nullable RequestListener<TranscodeType> targetListener,
BaseRequestOptions<?> options,
Executor callbackExecutor)

RequestManager
track(@NonNull Target<?> target, @NonNull Request request)

RequestTracker
runRequest(@NonNull Request request)

SingleRequest
begin()
onSizeReady(int width, int height)

Engine
load() L155
waitForExistingOrStartNewJob() L225

EngineJob
start(DecodeJob<R> decodeJob)

DecodeJob
run()
runWrapped();
getNextGenerator()
currentGenerator = new SourceGenerator(decodeHelper, this);
runGenerators();
!(isStarted = currentGenerator.startNext()))

SourceGenerator
while (!started && hasNextModelLoader()) {
hasNextModelLoader()

DecodeHelper
getLoadData()

.append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
.append(String.class, InputStream.class, new StringLoader.StreamFactory())
.append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())

new DataUrlLoader()


ModelLoaderCache里面的结构如下
Map<Class<?>, Entry<?>>
其中Entry结构如下
List<ModelLoader<Model, ?>>

DecodeHelper调用getModelLoaders()最终会进入到ModelLoaderRegistry第74行的getModelLoaders(@NonNull A model)
接着调用第110行的getModelLoadersForClass()方法
第一次进入cache是拿不到数据的，所以会调用multiModelLoaderFactory.build(modelClass)创建和String有关的ModerLoader
进入MultiModelLoaderFactory的第91行的build方法
Entry的结构如下：
private static class Entry<Model, Data> {
private final Class<Model> modelClass;
@Synthetic final Class<Data> dataClass;
@Synthetic final ModelLoaderFactory<? extends Model, ? extends Data> factory;
分别对应了注册的下面参数
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
他会从entries这个list里面循环查找，然后调用handles方法判断是否满足modelClass的条件，当是String参数的时候，找到满足条件如
.append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
.append(String.class, InputStream.class, new StringLoader.StreamFactory())
.append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
第一个找到满足条件的是:
.append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
然后会调用DataUrlLoader.StreamFactory的build方法创建一个ModelLoader实例，添加到loaders这个列表中
创建的实例是DataUrlLoader<Model, InputStream>
public ModelLoader<Model, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
return new DataUrlLoader<>(opener);
}
接下来找到
.append(String.class, InputStream.class, new StringLoader.StreamFactory())
然后会调用 StringLoader的build方法，最后会调用到StringLoader.StreamFactory的build方法
public ModelLoader<String, InputStream> build(@NonNull MultiModelLoaderFactory multiFactory) {
return new StringLoader<>(multiFactory.build(Uri.class, InputStream.class));
}
这里又会调用MultiModelLoaderFactory第128行的buid方法
public synchronized <Model, Data> ModelLoader<Model, Data> build(
@NonNull Class<Model> modelClass, @NonNull Class<Data> dataClass)
传入的参数是：
(multiFactory.build(Uri.class, InputStream.class)
接下来还是从entries循环查找满足上面2个参数条件的entry，然后调用entry.factory.build()方法创建ModelLoader
满足条件的有下面7条：
.append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory<Uri>())
.append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets()))
.append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context))
.append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context));
.append(Uri.class, InputStream.class, new QMediaStoreUriLoader.InputStreamFactory(context));
.append(Uri.class, InputStream.class, new UriLoader.StreamFactory(contentResolver))
.append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory())
第一条
.append(Uri.class, InputStream.class, new DataUrlLoader.StreamFactory<Uri>())
会调用DataUrlLoader.StreamFactory的build()方法创建一个DataUrlLoader<Uri, InputStream.class>
第二条
.append(Uri.class, InputStream.class, new AssetUriLoader.StreamFactory(context.getAssets()))
会调用AssetUriLoader.StreamFactory的build创建一个AssetUriLoader<Uri, InputStream.class>
第三条
.append(Uri.class, InputStream.class, new MediaStoreImageThumbLoader.Factory(context))
会调用MediaStoreImageThumbLoader.Factory的build创建一个MediaStoreImageThumbLoader<Uri, InputStream.class>
第四条
.append(Uri.class, InputStream.class, new MediaStoreVideoThumbLoader.Factory(context));
会调用MediaStoreVideoThumbLoader.Factory的build创建一个MediaStoreVideoThumbLoader<Uri, InputStream.class>
第五条
.append(Uri.class, InputStream.class, new QMediaStoreUriLoader.InputStreamFactory(context));
会调用QMediaStoreUriLoader.InputStreamFactory的build创建一个QMediaStoreUriLoader<Uri, InputStream.class>
第六条
.append(Uri.class, InputStream.class, new UriLoader.StreamFactory(contentResolver))
会调用UriLoader.StreamFactory的build创建一个UriLoader<Uri, InputStream.class>
第七条
.append(Uri.class, InputStream.class, new UrlUriLoader.StreamFactory())
会调用UrlUriLoader.StreamFactory的build创建一个UrlUriLoader<Uri, InputStream.class>
最后，回到MultiModelLoaderFactory第128行的build()方法，把上面创建好的ModelLoader都放到一个list里面
loaders.add(this.<Model, Data>build(entry));
如果上面创建的ModelLoader数量大于1，还会调用下面方法
factory.build(loaders, throwableListPool)
包装成MultiModelLoader，

接下来找到：
.append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
然后会调用 new StringLoader.FileDescriptorFactory的build方法，最后会调用MultiModelLoaderFactory第128行的buid方法
传入的参数是：Uri.class, ParcelFileDescriptor.class
.append(Uri.class,ParcelFileDescriptor.class,new QMediaStoreUriLoader.FileDescriptorFactory(context));
.append(Uri.class,ParcelFileDescriptor.class,new UriLoader.FileDescriptorFactory(contentResolver))
第一条
.append(Uri.class,ParcelFileDescriptor.class,new QMediaStoreUriLoader.FileDescriptorFactory(context));
会调用QMediaStoreUriLoader.FileDescriptorFactory的build()方法创建一个QMediaStoreUriLoader<Uri, ParcelFileDescriptor>
第二条
.append(Uri.class,ParcelFileDescriptor.class,new UriLoader.FileDescriptorFactory(contentResolver))
会调用UriLoader.FileDescriptorFactory的build()方法创建一个UriLoader<Uri, ParcelFileDescriptor>

接下来找到：
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
然后会调用 new StringLoader.AssetFileDescriptorFactory的build方法，最后会调用MultiModelLoaderFactory第128行的buid方法
传入的参数是：String.class, AssetFileDescriptor.class
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())
会调用StringLoader.AssetFileDescriptorFactory的build()方法创建一个StringLoader<String, AssetFileDescriptor.class>
这里又会调用MultiModelLoaderFactory第128行的buid方法
传入的参数是：Uri.class, AssetFileDescriptor.class
.append(Uri.class,AssetFileDescriptor.class,new AssetUriLoader.FileDescriptorFactory(context.getAssets()))
.append(Uri.class,AssetFileDescriptor.class,new UriLoader.AssetFileDescriptorFactory(contentResolver))
第一条
.append(Uri.class,AssetFileDescriptor.class,new AssetUriLoader.FileDescriptorFactory(context.getAssets()))
会调用AssetUriLoader.FileDescriptorFactory的build()方法创建一个AssetUriLoader<Uri, AssetFileDescriptor>
第二条
.append(Uri.class,AssetFileDescriptor.class,new UriLoader.AssetFileDescriptorFactory(contentResolver))
会调用UriLoader.AssetFileDescriptorFactory的build()方法创建一个UriLoader<Uri, AssetFileDescriptor>

综合上面，MultiModelLoaderFactory第91行最后返回的数据结构如下
DataUrlLoader<String, InputStream>
StringLoader<String, InputStream>
MultiModelLoader<String, InputStream>->List
DataUrlLoader<Uri, InputStream.class>
AssetUriLoader<Uri, InputStream.class>
MediaStoreImageThumbLoader<Uri, InputStream.class>
MediaStoreVideoThumbLoader<Uri, InputStream.class>
QMediaStoreUriLoader<Uri, InputStream.class>
UriLoader<Uri, InputStream.class>
UrlUriLoader<Uri, InputStream.class>
StringLoader<String, ParcelFileDescriptor>
MultiModelLoader<String, InputStream>->List
QMediaStoreUriLoader<Uri, ParcelFileDescriptor>
UriLoader<Uri, ParcelFileDescriptor>
StringLoader<String, AssetFileDescriptor>
MultiModelLoader<String, InputStream>->List
AssetUriLoader<Uri, AssetFileDescriptor>
UriLoader<Uri, AssetFileDescriptor>

然后再ModelLoaderRegistry 的getModelLoaders()方法会对得到的list过滤一遍，通过handles()方法
过滤之后就只有3个对象了
StringLoader<String, InputStream>
StringLoader<String, ParcelFileDescriptor>
StringLoader<String, AssetFileDescriptor>

DecodeHelper getLoadData() L：208
通过上面生成的ModelLoader重新生成对应的LoadData对象列表，并返回
LoadData结构
class LoadData<Data> {
public final Key sourceKey;
public final List<Key> alternateKeys;
public final DataFetcher<Data> fetcher;


第一个StringLoader<String, InputStream>，命中UrlUriLoader<Uri, InputStream.class>的handles,
所以通过MultiModelLoader<String, InputStream>的buildLoadData（）创建LoadData，内部再次调用子ModelLoader
UrlUriLoader buildLoadData 进行创建LoadData， UrlUriLoader内部又是通过urlLoader的buildLoadData()方法进行创建
urlLoader是一个HttpGlideUrlLoader类型的实力，因为在创建UrlUriLoader的时候，使用了下面代码创建：
public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiFactory) {
return new UrlUriLoader<>(multiFactory.build(GlideUrl.class, InputStream.class));
}
HttpGlideUrlLoader内部创建了LoaData(GlideUrl, HttpUrlFetcher)

根据分析可以得出：
StringLoader<String, InputStream>  multiFactory.build(Uri, InputStream)
UrlUriLoader<Uri, InputStream.class>
HttpGlideUrlLoader<GlideUrl, InputStream>

最后得到的LoadData如下

LoadData {
url
MultiModelLoader.MultiFetcher->List
HttpUrlFetcher(url, timeout)
}


接下来会调用这个方法，这个方法返回true之后，才会继续下一步
helper.hasLoadPath(loadData.fetcher.getDataClass())
这个方法内部会创建一个MultiClassKey，并且通过
set(@NonNull Class<?> first, @NonNull Class<?> second, @Nullable Class<?> third)
设置参数
然后回到Registry第475 getLoadPath()方法，返回的result=null，回去新建一个LoadPath
new LoadPath<>(java.io.InputStream, java.lang.Object, android.graphics.drawable.Drawable,
decodePaths, throwableListPool);
decodePaths是一个List，里面有下面3个元素
DecodePath{ dataClass=class java.io.InputStream, decoders=[com.bumptech.glide.load.resource.gif.StreamGifDecoder@433defe], transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@e7b865f}
DecodePath{ dataClass=class java.io.InputStream, decoders=[com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@a0358ac], transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@9795975}
DecodePath{ dataClass=class java.io.InputStream, decoders=[com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@76aed0a], transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@e7b865f}

接着到SourceGenerator 第88行的startNextLoad(),调用loadData.fetcher.loadData(),实际上调用的是HttpUrlFetcher
的loadData()方法，
InputStream result = loadDataWithRedirects(glideUrl.toURL(), 0, null, glideUrl.getHeaders());
callback.onDataReady(result);
然后会回调到SourceGenerator 的startNextLoad()方法内部的onDataReady()方法
接着调用onDataReadyInternal()方法
这里先忽略缓存，最后会回调到到DecodeJob的onDataFetcherReady()方法
接下来的调用流程：
DecodeJob#decodeFromRetrievedData() 调用链顶层，拿到Resource结果,回调onResourceReady与通知释放。
DecodeJob#decodeFromData() 拿到Resource结果，DataFetcher做出cleanup操作（也就是HttpUrlFetcher关闭流并且断开连接）
DecodeJob#decodeFromFetcher() 获取一个解码的途径来拿到Resource结果
DecodeJob#runLoadPath() 获取图片宽高、options，包裹InputStream。然后通过解码途径来拿到Resource结果
LoadPath#load() 通过解码途径来拿到Resource结果
LoadPath#loadWithExceptionList() 包裹错误对应的异常列表，再来拿到Resource结果
这个内部有一个decodePaths列表，他有3个元素，分别是
StreamGifDecoder
StreamBitmapDecoder
BitmapDrawableDecoder

DecodePath{ dataClass=class com.bumptech.glide.util.ContentLengthInputStream, decoders=[com.bumptech.glide.load.resource.gif.StreamGifDecoder@6728a2c], transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@ee66cf5}
DecodePath{ dataClass=class com.bumptech.glide.util.ContentLengthInputStream, decoders=[com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@7cd3a8a], transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@af428fb}
DecodePath{ dataClass=class com.bumptech.glide.util.ContentLengthInputStream, decoders=[com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@242d65c], transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@ee66cf5}


DecodePath#decode() 通过DecodePath#decodeResource() 解码Resource,并且拿对应的transform再做一层包裹形成新的Resource
DecodePath#decodeResource()
DecodePath#decodeResourceWithList()

最后在使用的是StreamBitmapDecoder进行处理，然后调用Downsampler的decode()方法进行解码

然后回调DecodeJob的decodeFromRetrievedData()方法中
拿到了Resource之后调用notifyEncodeAndRelease()方法
最后调用的哦EngineJog的onResourceReady
然后通过线程池运行CallResourceReady
entry.executor.execute(new CallResourceReady(entry.cb));
executor在 Engine 创建一个engineJob的时候会通过下面方法添加进来
engineJob.addCallback(cb, callbackExecutor);
在Engine的第225行的waitForExistingOrStartNewJob
而Engine的这个callbackExecutor则是从SingleRequest哪里传来的，SingleRequest第206行
而SingleRequest的这个参数又是从RequestBuilder的into方法传进来的，RequestBuilder第771行
@NonNull
public <Y extends Target<TranscodeType>> Y into(@NonNull Y target) {
    return into(target, /*targetListener=*/ null, Executors.mainThreadExecutor());
}
CallResourceReady的run方法会调用
callCallbackOnResourceReady(cb);
接着调用
cb.onResourceReady()
会回调到
SingleRequest中的onResourceReady()方法
接着第657行会回调
target.onResourceReady(result, animation);
接着会回调到ImageViewTarget的第99行 onResourceReady()方法
然后调用setResourceInternal()方法
然后调用setResource()方法
最后回调到DrawableImageViewTarget的setResource()方法

glide 缓存
从Engine 第155行的load()方法开始
第177行生成一个key
EngineKey key = keyFactory.buildKey()
Glide内存缓存的实现采取的是方案是：LruCache算法+弱引用机制
第190行，调用loadFromMemory()方法从内存缓存中查找图片
在loadFromMemory()内部，
第298行，判断是否开启了内存缓存，如果没有开启，直接返回null
第302行，从活动缓存中调用loadFromActiveResources()方法查找缓存，如果找到了直接返回EngineResource实例
如果没找到，第310行调用loadFromCache()方法从内存缓存中查找，找到了就直接返回，否则返回null
如果内存缓存中查找到图片资源，将图片从cache中移除，并且添加到正在使用的图片缓存activeResources
添加到活动缓存的逻辑在第339行。
内存缓存是使用LinkedHashMap进行缓存的，在MemoryCache类中，其中cache是LruResourceCache的一个实例，这个在
GlideBuilder第542进行实例化。
活动缓存在这个类里面实现ActiveResources
把正在使用的图片对象的弱引用添加到HashMap：activeEngineResources中，使用弱引用是因为持有图片对象的Activity或
Fragment有可能会被销毁，这样做可以及时清除缓存并释放内存，防止内存泄漏。
内存缓存功能小结：
读取缓存的顺序是：正在使用的图片缓存 > 内存缓存 > 磁盘缓存
缓存正在使用的图片采取HashMap+弱引用，而内存缓存使用LinkedHashMap

缓存写入
是在DecodeJob的decodeFromRetrievedData()方法开始
最后回调到Engine中的第371行的onEngineJobComplete()方法
在这里会调用下面的方法把他缓存到活动缓存
activeResources.activate(key, resource);
接着回调
entry.executor.execute(new CallResourceReady(entry.cb));
最后会回调到SingleRequest的第542行的onResourceReady()方法
在这个方法最后，调用了
engine.release(toRelease);
如果图片资源被标记为需要释放，会调用engine.release(toRelease)释放图片资源
接着调用EngineResource第361行代码
接着调用EngineResource的第105行的release()方法
在该方法内部，acquired变量用于计算图片资源被引用的次数，acquired==0表示该图片资源可以被回收，在Engine的onResourceReleased()方法中实现资源回收
然后回调到Engine第394行onResourceReleased()方法，在该方法内部进行了内存缓存的存储

磁盘缓存
缓存表示
缓存表示	说明
DiskCacheStrategy.NONE	表示不开启磁盘缓存
DiskCacheStrategy.RESOURCE	表示只缓存转换之后的图片。
DiskCacheStrategy.ALL	表示既缓存原始图片，也缓存转换过后的图片。
DiskCacheStrategy.DATA	表示只缓存原始图片
DiskCacheStrategy.AUTOMATIC	根据数据源自动选择磁盘缓存策略（默认选择）

磁盘缓存是从DecodeJob 的 runWrapped()方法开始的，如果设置了diskCacheStrategy(DiskCacheStrategy.RESOURCE)
进入runWrapped()之后，首先满足第一个条件INITIALIZE，调用getNextStage()方法
在getNextStage()内部，一次匹配的stage的顺序是：
RESOURCE_CACHE->DATA_CACHE->SOURCE->FINISHED
在这个方法内部有个标志
仅从缓存中检索onlyRetrieveFromCache，它表示仅从缓存中检索
上面的Stage分别表示：
RESOURCE_CACHE:转换之后的缓存
DATA_CACHE:原始缓存
SOURCE:网络缓存
FINISHED:完成
接着调用getNextGenerator()方法，在这个方法内部，实例化的DataFetcherGenerator对应关系如下：
RESOURCE_CACHE->ResourceCacheGenerator 解码后的资源执行器
DATA_CACHE->DataCacheGenerator 原始数据执行器
SOURCE->SourceGenerator 新的请求，http执行器

如果设置了diskCacheStrategy(DiskCacheStrategy.RESOURCE)，那runWrapped()方法内部实例化的就是ResourceCacheGenerator
接着进入到ResourceCacheGenerator的startNext()方法，
RequestBuilder传入的transcodeClass如下：
class android.graphics.drawable.Drawable
第1234行SingleRequest.obtain()传入的第5个参数就是上面的transcodeClass
SingleRequest第468行传入了transcodeClass
DecodeJob上面的泛型参数R=class android.graphics.drawable.Drawable
所以DecodeJob的DecodeHelper R也和上面一样
ResourceCacheGenerator传入的DecodeHelper的实例泛型就是Drawable
resourceClass 是在BaseRequestOptions第93行定义的Object

在ResourceCacheGenerator第51行会调用helper.getRegisteredResourceClasses()
传入的参数：
modelClass = class java.lang.String
resourceClass = class java.lang.Object
transcodeClass = class android.graphics.drawable.Drawable

modelClass: 源数据初始模型，例如传入的是字符串
resourceClass:目标数据类型
transcodeClass：转换成目标数据类型使用类的类型

接着进入Registry第535的getRegisteredResourceClasses()方法
这个方法首先从modelLoaderRegistry拿到所有java.lang.String对应的dataClass，并存放到dataClasses中，它主要包含
以下数据：
class java.io.InputStream
class android.os.ParcelFileDescriptor
class android.content.res.AssetFileDescriptor

调用decoderRegistry.getResourceClasses()方法获取上面dataClasses和java.lang.Object对应的decoder class集合
dataClasses等于上面数据集合，resourceClass=java.lang.Object的所有decoder class集合
当满足条件ResourceDecoderRegistry里面的Entry的dataClass 是传入dataClasses子类或者和dataClasses相等
并且ResourceDecoderRegistry里面的Entry的resourceClass是传入resourceClass子类或者和resourceClass相等
并且缓存不存在的时候，就添加对应的decoder到List中，说明这个List不会添加2个 一模一样的decoder
存放到registeredResourceClasses，这里面把符合条件对应的resourceClass查找出来了，

调用transcoderRegistry.getTranscodeClasses()方法，在这个方法内部，当传入的参数resourceClass是transcodeClass
的父类或者resourceClass等于transcodeClass，会把对应的resourceClass直接添加到返回列表，并直接返回
否则会从transcoders便利，当满足条件传入的fromClass等于Entry的fromClass或者传入的fromClass是
Entry的fromClass的父类并且传入的toClass等于Entry的toClass或者Entry的toClass是传入toClass的父类，
也会把满足条件的Entry的toClass添加到返回列表中，然后Registry中第549行registeredTranscodeClasses接收返回值。

然后回到Registry第551行，判断返回的registeredTranscodeClasses如果不为空并且result没有添加registeredResourceClass
就把registeredResourceClass添加到result中。

第539行从这个缓存中取数据modelToResourceClassCache
从名字看就是model转成资源类的缓存
首次取那就返回null，所以接下来进入到第544行，
最后调用MultiModelLoaderFactory第117行的getDataClasses()方法
在这个方法会添加String.class对应的dataClass，因为这里有判断，所以不包括重复的InputStream.class数据
dataClasses这里有3条
class java.io.InputStream
class android.os.ParcelFileDescriptor
class android.content.res.AssetFileDescriptor

Registry第544行的List<Class<?>> dataClasses列表装的就是上面3条数据
接着第547行调用了ResourceDecoderRegistry的getResourceClasses()方法

decoderRegistry: 解码器注册
transcoderRegistry: 转码器注册

所以decoders的值在上面，decoders的值有以下这些：

在getResourceClasses()方法里面

ResourceDecodeRegistry的getResourceClasses()传入的参数是：
dataClass = class java.nio.ByteBuffer
resourceClass = class java.lang.Object


就是从上面的decoders要查找所有符合entry的dataClass是dataClass的子类，并且
entry的resourceClass是resourceClass的子类的集合。
然后把resourceClass加入到List
第一步
legacy_prepend_all 没有对应的decoder
第二步
Animation 找到Animation列表对应的1
class com.bumptech.glide.load.resource.gif.GifDrawable
第三步
Bitmap 找到
class android.graphics.Bitmap
第四步
BitmapDrawable 找到
class android.graphics.drawable.BitmapDrawable
第五步
legacy_append 找到
class android.graphics.Bitmap
class android.graphics.drawable.BitmapDrawable

所以最后
ResourceDecodeRegistry的getResourceClasses()返回的list如下
class com.bumptech.glide.load.resource.gif.GifDrawable
class android.graphics.Bitmap
class android.graphics.drawable.BitmapDrawable

所以回到Registry中的535行getRegisteredResourceClasses()方法
传入的参数：
modelClass = class java.lang.String
resourceClass = class java.lang.Object
transcodeClass = class android.graphics.drawable.Drawable

第548行第一次获取到的List数据就是上面的3条
然后进入内循环，第550进入TranscoderRegistry 里面的getTranscodeClasses()方法
在这个方法内部 transcoders有下面的值
0={TranscoderRegistry$Entry@26666}
    fromClass="class android.graphics.Bitmap"
    toClass="class android.graphics.drawable.BitmapDrawable"
    transcoder={BitmapDrawableTranscoder}
1={TranscoderRegistry$Entry@26666}
    fromClass="class android.graphics.Bitmap"
    toClass="class [B"
    transcoder={BitmapBytesTranscoder}
2={TranscoderRegistry$Entry@26666}
    fromClass="class android.graphics.drawable.Drawable"
    toClass="class [B"
    transcoder={DrawableBytesTranscoder}
3={TranscoderRegistry$Entry@26666}
    fromClass="class com.bumptech.glide.load.resource.gif.GifDrawable"
    toClass="class [B"
    transcoder={GifDrawableBytesTranscoder}

在TranscoderRegistry中的getTranscodeClasses()方法
第一步，传入
resourceClass = class com.bumptech.glide.load.resource.gif.GifDrawable
transcodeClass = class android.graphics.drawable.Drawable
满足条件transcodeClass.isAssignableFrom(resourceClass)
就直接添加class android.graphics.drawable.Drawable到List里面
第二步
传入
resourceClass = class android.graphics.Bitmap
transcodeClass = class android.graphics.drawable.Drawable
不满足条件transcodeClass.isAssignableFrom(resourceClass)
所以进入73行的for循环
它满足条件transcoders[0].fromClass.isAssignableFrom(class android.graphics.Bitmap)
所以添加到列表中
transcoders[0].toClass = class android.graphics.drawable.BitmapDrawable
第三步
传入
resourceClass = class android.graphics.drawable.BitmapDrawable
transcodeClass = class android.graphics.drawable.Drawable
满足transcodeClass.isAssignableFrom(resourceClass)
添加到列表中：
class android.graphics.drawable.Drawable

这样一轮循环结束之后，最后result列表中的数据如下
class com.bumptech.glide.load.resource.gif.GifDrawable
class android.graphics.Bitmap
class android.graphics.drawable.BitmapDrawable
然后第556行调用modelToResourceClassCache的put方法把数据添加到缓存，缓存的key是
new MultiClassKey(modelClass, resourceClass, transcodeClass)
在这个方法内部，key应该是判断上面参数相等就认为是同一个的
然后返回到ResourceCacheGenerator的第52行方法中

下面的意思应该是，数据输入是dataClass经过decoder之后，转成了resourceClass类型的东西
dataClass="class android.os.ParcelFileDescriptor"
decoder={VideoDecoder}
resourceClass="class android.graphics.Bitmap"

然后回到ResourceCacheGenerator第51行
接着到第62行while语句，首先modelLoaders == null，所以进入该循环，这里总结一句话就是如果modelLoaders
里面有内容就会跳过这个循环，如果为空就从这个循环里面找到对应的modelLoaders
拿到第一条数据com.bumptech.glide.load.resource.gif.GifDrawable，然后到第88行cacheFile = null
所以进行下一轮循环，拿到android.graphics.Bitmap，然后到第88行cacheFile == null
如果没有缓存，所有循环进行之后，会在第67行返回false。

在DecodeJob下面这个方法,就是判断是否有缓存，然后决定是否从网络加载数据的Generator
private DataFetcherGenerator getNextGenerator() {
    switch (stage) {
        case RESOURCE_CACHE:
            return new ResourceCacheGenerator(decodeHelper, this);
        case DATA_CACHE:
            return new DataCacheGenerator(decodeHelper, this);
        case SOURCE:
            return new SourceGenerator(decodeHelper, this);
        case FINISHED:
            return null;
        default:
            throw new IllegalStateException("Unrecognized stage: " + stage);
    }
}
ResourceCacheGenerator 应该是磁盘缓存RESOURCE
DataCacheGenerator 磁盘缓存DATA
SourceGenerator 从网络获取数据

当设置了DiskCacheStrategy.RESOURCE时，首先会从ResourceCacheGenerator查找缓存
如果存在的话，最后会调用第105行的loadData()方法，这个fetcher是ByteBufferFileLoader的
ByteBufferFetcher类型。查到之后，会回调callback的onDataReady()方法。
然后回调DecodeJob第379行的onDataFetcherReady()方法。

dataClass:源数据类型
resourceClass:目标数据类型
transcodeClass：转换成目标数据类型使用类的类型































































































