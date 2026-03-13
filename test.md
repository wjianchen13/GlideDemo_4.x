请问下面这个方法是什么意思？
@NonNull
synchronized <Model> List<ModelLoader<Model, ?>> build(@NonNull Class<Model> modelClass) {
try {
List<ModelLoader<Model, ?>> loaders = new ArrayList<>();
for (Entry<?, ?> entry : entries) {
// Avoid stack overflow recursively creating model loaders by only creating loaders in
// recursive requests if they haven't been created earlier in the chain. For example:
// A Uri loader may translate to another model, which in turn may translate back to a Uri.
// The original Uri loader won't be provided to the intermediate model loader, although
// other Uri loaders will be.
if (alreadyUsedEntries.contains(entry)) {
continue;
}
if (entry.handles(modelClass)) {
alreadyUsedEntries.add(entry);
loaders.add(this.<Model, Object>build(entry));
alreadyUsedEntries.remove(entry);
}
}
return loaders;
} catch (Throwable t) {
alreadyUsedEntries.clear();
throw t;
}
}

我分析SourceGenerator的startNext()方法，有如下的代码片段：
boolean started = false;
while (!started && hasNextModelLoader()) {
loadData = helper.getLoadData().get(loadDataListIndex++);
if (loadData != null
&& (helper.getDiskCacheStrategy().isDataCacheable(loadData.fetcher.getDataSource())
|| helper.hasLoadPath(loadData.fetcher.getDataClass()))) {
started = true;
startNextLoad(loadData);
}
}
然后我跟进这个方法：helper.hasLoadPath(loadData.fetcher.getDataClass())，这个方法内部会调用
Registry的getLoadPath()方法
请问这个getLoadPath方法是什么意思？

Registry中的getLoadPath方法，方法代码如下：
@Nullable
public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(
@NonNull Class<Data> dataClass,
@NonNull Class<TResource> resourceClass,
@NonNull Class<Transcode> transcodeClass) {
LoadPath<Data, TResource, Transcode> result =
loadPathCache.get(dataClass, resourceClass, transcodeClass);
if (loadPathCache.isEmptyLoadPath(result)) {
return null;
} else if (result == null) {
List<DecodePath<Data, TResource, Transcode>> decodePaths =
getDecodePaths(dataClass, resourceClass, transcodeClass);
// It's possible there is no way to decode or transcode to the desired types from a given
// data class.
if (decodePaths.isEmpty()) {
result = null;
} else {
result =
new LoadPath<>(
dataClass, resourceClass, transcodeClass, decodePaths, throwableListPool);
}
loadPathCache.put(dataClass, resourceClass, transcodeClass, result);
}
return result;
}
请问这是什么意思？

这个：LoadPathCache是什么意思

LoadPathCache中的get方法是什么意思
代码片段：
public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(
Class<Data> dataClass, Class<TResource> resourceClass, Class<Transcode> transcodeClass) {
MultiClassKey key = getKey(dataClass, resourceClass, transcodeClass);
LoadPath<?, ?, ?> result;
synchronized (cache) {
result = cache.get(key);
}
keyRef.set(key);

    return (LoadPath<Data, TResource, Transcode>) result;
}

请问一下LoadPathCache中的cache缓存
private final ArrayMap<MultiClassKey, LoadPath<?, ?, ?>> cache = new ArrayMap<>();
为什么put方法的时候，需要new MultiClassKey，如果这样的话，get的时候又new MultiClassKey对象
虽然他们传入的参数都是一样的，但是这不是2个不一样key了吗
public void put(
Class<?> dataClass,
      Class<?> resourceClass,
Class<?> transcodeClass,
      @Nullable LoadPath<?, ?, ?> loadPath) {
synchronized (cache) {
cache.put(
new MultiClassKey(dataClass, resourceClass, transcodeClass),
loadPath != null ? loadPath : NO_PATHS_SIGNAL);
}
}

LoadPathCache第52行的get方法：
public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> get(
Class<Data> dataClass, Class<TResource> resourceClass, Class<Transcode> transcodeClass) {
MultiClassKey key = getKey(dataClass, resourceClass, transcodeClass);
LoadPath<?, ?, ?> result;
synchronized (cache) {
result = cache.get(key);
}
keyRef.set(key);

    return (LoadPath<Data, TResource, Transcode>) result;
}
上面的result = cache.get(key);一定可以获取到(LoadPath<Data, TResource, Transcode>)类型的值吗？


DecodeHelper下面的方法
<Data> LoadPath<Data, ?, Transcode> getLoadPath(Class<Data> dataClass) {
return glideContext.getRegistry().getLoadPath(dataClass, resourceClass, transcodeClass);
}
resourceClass和transcodeClass从哪里传入的？

这个DecodePath里面的内容表示什么意思，什么是dataClass，什么是decoders，什么是transcoder

我在返回的decodePaths拿到的第一条数据如下：
DecodePath{ dataClass=class java.io.InputStream, decoders=[com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@da9e2e0,  com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@db60999], transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@2c4e45e}
为什么decoders里面有2个一样的com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder？

请问注册的时候：
@NonNull
public <Data, TResource> Registry append(
@NonNull String bucket,
@NonNull Class<Data> dataClass,
@NonNull Class<TResource> resourceClass,
@NonNull ResourceDecoder<Data, TResource> decoder) {
decoderRegistry.append(bucket, decoder, dataClass, resourceClass);
return this;
}
传的第一个参数bucket是什么意思，
包括这些类型：
public static final String BUCKET_ANIMATION = "Animation";
public static final String BUCKET_BITMAP = "Bitmap";
public static final String BUCKET_BITMAP_DRAWABLE = "BitmapDrawable";
private static final String BUCKET_PREPEND_ALL = "legacy_prepend_all";
private static final String BUCKET_APPEND_ALL = "legacy_append";


我看Registry中还有
private final EncoderRegistry encoderRegistry;
private final ResourceEncoderRegistry resourceEncoderRegistry;
上面这两个Encoder是什么情况下才会使用的


Registry中的getLoadPath方法，方法代码如下：
@Nullable
public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(
@NonNull Class<Data> dataClass,
@NonNull Class<TResource> resourceClass,
@NonNull Class<Transcode> transcodeClass) {
LoadPath<Data, TResource, Transcode> result =
loadPathCache.get(dataClass, resourceClass, transcodeClass);
if (loadPathCache.isEmptyLoadPath(result)) {
return null;
} else if (result == null) {
List<DecodePath<Data, TResource, Transcode>> decodePaths =
getDecodePaths(dataClass, resourceClass, transcodeClass);
// It's possible there is no way to decode or transcode to the desired types from a given
// data class.
if (decodePaths.isEmpty()) {
result = null;
} else {
result =
new LoadPath<>(
dataClass, resourceClass, transcodeClass, decodePaths, throwableListPool);
}
loadPathCache.put(dataClass, resourceClass, transcodeClass, result);
}
return result;
}
请问传入的参数dataClass, resourceClass, transcodeClass是什么意思

TranscoderRegistry里面有下面的代码片段，可以用最通俗的语言告诉我是什么意思吗
有一个条件传入的参数是
resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable
transcodedClass=class android.graphics.drawable.Drawable
@NonNull
@SuppressWarnings("unchecked")
public synchronized <Z, R> ResourceTranscoder<Z, R> get(
@NonNull Class<Z> resourceClass, @NonNull Class<R> transcodedClass) {
// For example, there may be a transcoder that can convert a GifDrawable to a Drawable, which
// will be caught above. However, if there is no registered transcoder, we can still just use
// the UnitTranscoder to return the Drawable because the transcode class (Drawable) is
// assignable from the resource class (GifDrawable).
if (transcodedClass.isAssignableFrom(resourceClass)) {
return (ResourceTranscoder<Z, R>) UnitTranscoder.get();
}
for (Entry<?, ?> entry : transcoders) {
if (entry.handles(resourceClass, transcodedClass)) {
return (ResourceTranscoder<Z, R>) entry.transcoder;
}
}

    throw new IllegalArgumentException(
        "No transcoder registered to transcode from " + resourceClass + " to " + transcodedClass);
}


下面代码片段
@NonNull
private <Data, TResource, Transcode> List<DecodePath<Data, TResource, Transcode>> getDecodePaths(
@NonNull Class<Data> dataClass,
@NonNull Class<TResource> resourceClass,
@NonNull Class<Transcode> transcodeClass) {
List<DecodePath<Data, TResource, Transcode>> decodePaths = new ArrayList<>();
List<Class<TResource>> registeredResourceClasses =
decoderRegistry.getResourceClasses(dataClass, resourceClass);

    for (Class<TResource> registeredResourceClass : registeredResourceClasses) {
      List<Class<Transcode>> registeredTranscodeClasses =
          transcoderRegistry.getTranscodeClasses(registeredResourceClass, transcodeClass);

      for (Class<Transcode> registeredTranscodeClass : registeredTranscodeClasses) {

        List<ResourceDecoder<Data, TResource>> decoders =
            decoderRegistry.getDecoders(dataClass, registeredResourceClass);
        ResourceTranscoder<TResource, Transcode> transcoder =
            transcoderRegistry.get(registeredResourceClass, registeredTranscodeClass);
        @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
        DecodePath<Data, TResource, Transcode> path =
            new DecodePath<>(
                dataClass,
                registeredResourceClass,
                registeredTranscodeClass,
                decoders,
                transcoder,
                throwableListPool);
        decodePaths.add(path);
      }
    }
    return decodePaths;
}
new DecodePath()传入参数：
dataClass，registeredResourceClass， registeredTranscodeClass，decoders，transcoder分别表示什么意思

MultiModelLoaderFactory下面的代码：
@NonNull
synchronized <Model> List<ModelLoader<Model, ?>> build(@NonNull Class<Model> modelClass) {
try {
List<ModelLoader<Model, ?>> loaders = new ArrayList<>();
for (Entry<?, ?> entry : entries) {
// Avoid stack overflow recursively creating model loaders by only creating loaders in
// recursive requests if they haven't been created earlier in the chain. For example:
// A Uri loader may translate to another model, which in turn may translate back to a Uri.
// The original Uri loader won't be provided to the intermediate model loader, although
// other Uri loaders will be.
if (alreadyUsedEntries.contains(entry)) {
continue;
}
if (entry.handles(modelClass)) {
alreadyUsedEntries.add(entry);
loaders.add(this.<Model, Object>build(entry));
alreadyUsedEntries.remove(entry);
}
}
return loaders;
} catch (Throwable t) {
alreadyUsedEntries.clear();
throw t;
}
}
Entry代码如下：
private static class Entry<Model, Data> {
private final Class<Model> modelClass;
@Synthetic final Class<Data> dataClass;
@Synthetic final ModelLoaderFactory<? extends Model, ? extends Data> factory;

    public Entry(
        @NonNull Class<Model> modelClass,
        @NonNull Class<Data> dataClass,
        @NonNull ModelLoaderFactory<? extends Model, ? extends Data> factory) {
      this.modelClass = modelClass;
      this.dataClass = dataClass;
      this.factory = factory;
    }

    public boolean handles(@NonNull Class<?> modelClass, @NonNull Class<?> dataClass) {
      return handles(modelClass) && this.dataClass.isAssignableFrom(dataClass);
    }

    public boolean handles(@NonNull Class<?> modelClass) {
      return this.modelClass.isAssignableFrom(modelClass);
    }
}
请问Entry里面的变量：
private final Class<Model> modelClass;
@Synthetic final Class<Data> dataClass;
@Synthetic final ModelLoaderFactory<? extends Model, ? extends Data> factory;
分别表示什么意思？


modelClass dataClass  factory 和DecodePath实例化是传入的参数dataClass，registeredResourceClass，
registeredTranscodeClass，decoders，transcoder有什么联系？


Registry中有如下代码片段：
@Nullable
public <Data, TResource, Transcode> LoadPath<Data, TResource, Transcode> getLoadPath(
@NonNull Class<Data> dataClass,
@NonNull Class<TResource> resourceClass,
@NonNull Class<Transcode> transcodeClass) {
LoadPath<Data, TResource, Transcode> result =
loadPathCache.get(dataClass, resourceClass, transcodeClass);
if (loadPathCache.isEmptyLoadPath(result)) {
return null;
} else if (result == null) {
List<DecodePath<Data, TResource, Transcode>> decodePaths =
getDecodePaths(dataClass, resourceClass, transcodeClass);
// It's possible there is no way to decode or transcode to the desired types from a given
// data class.
if (decodePaths.isEmpty()) {
result = null;
} else {
result =
new LoadPath<>(
dataClass, resourceClass, transcodeClass, decodePaths, throwableListPool);
}
loadPathCache.put(dataClass, resourceClass, transcodeClass, result);
}
return result;
}
我看到返回的result结果如下：
DecodePath{
dataClass=class java.io.InputStream,
decoders=[
com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@db60999,
com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@2c4e45e
],
transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
DecodePath{
dataClass=class java.io.InputStream,
decoders=[
com.bumptech.glide.load.resource.gif.StreamGifDecoder@a1e103f
],
transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
DecodePath{
dataClass=class java.io.InputStream,
decoders=[
com.bumptech.glide.integration.webp.decoder.StreamAnimatedBitmapDecoder@77a4d0c,
com.bumptech.glide.integration.webp.decoder.StreamBitmapWebpDecoder@5d4a655,
com.bumptech.glide.integration.webp.decoder.StreamAnimatedBitmapDecoder@f6e86a,
com.bumptech.glide.integration.webp.decoder.StreamBitmapWebpDecoder@fed715b,
com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@8052df8
],
transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@40652d1
}
DecodePath{
dataClass=class java.io.InputStream,
decoders=[
com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@205009d,
com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@8bf4912,
com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@7a1c8e3
],
transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
为什么decoder里面有相同类型的对象，比如说有3个BitmapDrawableDecoder的对象？

从HttpUrlFetcher的load()方法开始，如果能够正确获取数据并成功，到最终把数据设置到view上面中间的过程麻烦给我说一下，要详细一点，通俗易懂一点
@Override
public void loadData(
@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
long startTime = LogTime.getLogTime();
try {
InputStream result = loadDataWithRedirects(glideUrl.toURL(), 0, null, glideUrl.getHeaders());
callback.onDataReady(result);
} catch (IOException e) {
if (Log.isLoggable(TAG, Log.DEBUG)) {
Log.d(TAG, "Failed to load data for url", e);
}
callback.onLoadFailed(e);
} finally {
if (Log.isLoggable(TAG, Log.VERBOSE)) {
Log.v(TAG, "Finished http url fetcher fetch in " + LogTime.getElapsedMillis(startTime));
}
}
}

DecodeJob有下面代码片段：
private <Data, ResourceType> Resource<R> runLoadPath(
Data data, DataSource dataSource, LoadPath<Data, ResourceType, R> path)
throws GlideException {
Options options = getOptionsWithHardwareConfig(dataSource);
DataRewinder<Data> rewinder = glideContext.getRegistry().getRewinder(data);
try {
// ResourceType in DecodeCallback below is required for compilation to work with gradle.
return path.load(
rewinder, options, width, height, new DecodeCallback<ResourceType>(dataSource));
} finally {
rewinder.cleanup();
}
}
请问是什么意思，你解释一下


请帮我分析一下DecodePath下面的代码是什么意思，分析详细一点，通俗易懂一点
public Resource<Transcode> decode(
DataRewinder<DataType> rewinder,
int width,
int height,
@NonNull Options options,
DecodeCallback<ResourceType> callback)
throws GlideException {
Resource<ResourceType> decoded = decodeResource(rewinder, width, height, options);
Resource<ResourceType> transformed = callback.onResourceDecoded(decoded);
return transcoder.transcode(transformed, options);
}

StreamWebpDecoder下面的代码片段是什么意思
@Override
public boolean handles(@NonNull InputStream inputStream, @NonNull Options options) throws IOException {
if (options.get(DISABLE_ANIMATION)) {
return false;
}

        WebpHeaderParser.WebpImageType webpType = WebpHeaderParser.getType(inputStream, byteArrayPool);
        return WebpHeaderParser.isAnimatedWebpType(webpType);
    }



StreamGifDecoder下面的代码片段是什么意思
@Override
public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
return !options.get(GifOptions.DISABLE_ANIMATION)
&& ImageHeaderParserUtils.getType(parsers, source, byteArrayPool) == ImageType.GIF;
}


StreamAnimatedBitmapDecoder
下面的代码片段是什么意思
@Override
public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
return bitmapDecoder.handles(source, options);
}

DecodePath有如下代码片段：
@NonNull
private Resource<ResourceType> decodeResourceWithList(
DataRewinder<DataType> rewinder,
int width,
int height,
@NonNull Options options,
List<Throwable> exceptions)
throws GlideException {
Resource<ResourceType> result = null;
//noinspection ForLoopReplaceableByForEach to improve perf
for (int i = 0, size = decoders.size(); i < size; i++) {
ResourceDecoder<DataType, ResourceType> decoder = decoders.get(i);
try {
DataType data = rewinder.rewindAndGet();
if (decoder.handles(data, options)) {
data = rewinder.rewindAndGet();
result = decoder.decode(data, width, height, options);
}
// Some decoders throw unexpectedly. If they do, we shouldn't fail the entire load path, but
// instead log and continue. See #2406 for an example.
} catch (IOException | RuntimeException | OutOfMemoryError e) {
if (Log.isLoggable(TAG, Log.VERBOSE)) {
Log.v(TAG, "Failed to decode data for " + decoder, e);
}
exceptions.add(e);
}

      if (result != null) {
        break;
      }
    }

    if (result == null) {
      throw new GlideException(failureMessage, new ArrayList<>(exceptions));
    }
    return result;
}
请问这两句代码是什么意思？
data = rewinder.rewindAndGet();
result = decoder.decode(data, width, height, options);



StreamBitmapDecoder有如下代码片段，请问是什么意思？
@Override
public Resource<Bitmap> decode(
@NonNull InputStream source, int width, int height, @NonNull Options options)
throws IOException {

    // Use to fix the mark limit to avoid allocating buffers that fit entire images.
    final RecyclableBufferedInputStream bufferedStream;
    final boolean ownsBufferedStream;
    if (source instanceof RecyclableBufferedInputStream) {
      bufferedStream = (RecyclableBufferedInputStream) source;
      ownsBufferedStream = false;
    } else {
      bufferedStream = new RecyclableBufferedInputStream(source, byteArrayPool);
      ownsBufferedStream = true;
    }

    // Use to retrieve exceptions thrown while reading.
    // TODO(#126): when the framework no longer returns partially decoded Bitmaps or provides a
    // way to determine if a Bitmap is partially decoded, consider removing.
    ExceptionPassthroughInputStream exceptionStream =
        ExceptionPassthroughInputStream.obtain(bufferedStream);

    // Use to read data.
    // Ensures that we can always reset after reading an image header so that we can still
    // attempt to decode the full image even when the header decode fails and/or overflows our read
    // buffer. See #283.
    MarkEnforcingInputStream invalidatingStream = new MarkEnforcingInputStream(exceptionStream);
    UntrustedCallbacks callbacks = new UntrustedCallbacks(bufferedStream, exceptionStream);
    try {
      return downsampler.decode(invalidatingStream, width, height, options, callbacks);
    } finally {
      exceptionStream.release();
      if (ownsBufferedStream) {
        bufferedStream.release();
      }
    }
}

为什么我在Downsampler没有看到这个方法被调用：BitmapFactory.decodeStream(inputStream, null, options);

这个方法为什么要调用ImageReader.InputStreamImageReader(is, parsers, byteArrayPool)，ImageReader是什么？
public Resource<Bitmap> decode(
InputStream is,
int requestedWidth,
int requestedHeight,
Options options,
DecodeCallbacks callbacks)
throws IOException {
return decode(
new ImageReader.InputStreamImageReader(is, parsers, byteArrayPool),
requestedWidth,
requestedHeight,
options,
callbacks);
}

Downsampler这段代码是什么意思？
private Resource<Bitmap> decode(
ImageReader imageReader,
int requestedWidth,
int requestedHeight,
Options options,
DecodeCallbacks callbacks)
throws IOException {
byte[] bytesForOptions = byteArrayPool.get(ArrayPool.STANDARD_BUFFER_SIZE_BYTES, byte[].class);
BitmapFactory.Options bitmapFactoryOptions = getDefaultOptions();
bitmapFactoryOptions.inTempStorage = bytesForOptions;

    DecodeFormat decodeFormat = options.get(DECODE_FORMAT);
    PreferredColorSpace preferredColorSpace = options.get(PREFERRED_COLOR_SPACE);
    DownsampleStrategy downsampleStrategy = options.get(DownsampleStrategy.OPTION);
    boolean fixBitmapToRequestedDimensions = options.get(FIX_BITMAP_SIZE_TO_REQUESTED_DIMENSIONS);
    boolean isHardwareConfigAllowed =
        options.get(ALLOW_HARDWARE_CONFIG) != null && options.get(ALLOW_HARDWARE_CONFIG);

    try {
      Bitmap result =
          decodeFromWrappedStreams(
              imageReader,
              bitmapFactoryOptions,
              downsampleStrategy,
              decodeFormat,
              preferredColorSpace,
              isHardwareConfigAllowed,
              requestedWidth,
              requestedHeight,
              fixBitmapToRequestedDimensions,
              callbacks);
      return BitmapResource.obtain(result, bitmapPool);
    } finally {
      releaseOptions(bitmapFactoryOptions);
      byteArrayPool.put(bytesForOptions);
    }
}

请问这个方法
private void decodeFromRetrievedData() {
if (Log.isLoggable(TAG, Log.VERBOSE)) {
logWithTimeAndKey(
"Retrieved data",
startFetchTime,
"data: "
+ currentData
+ ", cache key: "
+ currentSourceKey
+ ", fetcher: "
+ currentFetcher);
}
Resource<R> resource = null;
try {
resource = decodeFromData(currentFetcher, currentData, currentDataSource);
} catch (GlideException e) {
e.setLoggingDetails(currentAttemptingKey, currentDataSource);
throwables.add(e);
}
if (resource != null) {
notifyEncodeAndRelease(resource, currentDataSource, isLoadingFromAlternateCacheKey);
} else {
runGenerators();
}
}
调用decodeFromData(currentFetcher, currentData, currentDataSource);成功获取了Resource资源之后，
+ 后面的步骤是怎样的，怎么把这个资源设置到具体的ImageView上？

最后这个DrawableImageViewTarget是怎么出来的，为什么不是BitmapImageViewTarget？

这几个的关系是什么：
EngineJob
DecodeJob
DecodePath
SingleRequest

我项目中为什么com.example.glidedemo_4x.test4.PAGFileResourceDecoder 类型是<ByteBuffer, PAGFile>
而com.example.glidedemo_4x.test5.PAGFileStreamDecoder的类型却是<InputStream, PAGFile> 






















