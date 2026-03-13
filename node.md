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

decoderRegistry保存的数据：
registry.append(Registry.BUCKET_ANIMATION, InputStream.class, Drawable.class, AnimatedWebpDecoder.streamDecoder(imageHeaderParsers, arrayPool));
registry.append(Registry.BUCKET_ANIMATION, ByteBuffer.class, Drawable.class, AnimatedWebpDecoder.byteBufferDecoder(imageHeaderParsers, arrayPool));
.append(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder)
.append(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder);
registry.append(Registry.BUCKET_BITMAP, ParcelFileDescriptor.class, Bitmap.class, new ParcelFileDescriptorBitmapDecoder(downsampler));
registry .append(Registry.BUCKET_BITMAP, ParcelFileDescriptor.class, Bitmap.class, parcelFileDescriptorVideoDecoder)
.append(Registry.BUCKET_BITMAP, AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(bitmapPool))
.append(Registry.BUCKET_BITMAP, Bitmap.class, Bitmap.class, new UnitBitmapDecoder())
.append(Registry.BUCKET_BITMAP_DRAWABLE, ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, byteBufferBitmapDecoder))
.append(Registry.BUCKET_BITMAP_DRAWABLE, InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, streamBitmapDecoder))
.append(Registry.BUCKET_BITMAP_DRAWABLE, ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, parcelFileDescriptorVideoDecoder))
.append(Registry.BUCKET_ANIMATION, InputStream.class, GifDrawable.class, new StreamGifDecoder(imageHeaderParsers, byteBufferGifDecoder, arrayPool))
.append(Registry.BUCKET_ANIMATION, ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder)
.append(Registry.BUCKET_BITMAP, GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool))
.append(BUCKET_APPEND_ALL,Uri.class, Drawable.class, resourceDrawableDecoder)
.append(BUCKET_APPEND_ALL,Uri.class, Bitmap.class, new ResourceBitmapDecoder(resourceDrawableDecoder, bitmapPool))
.append(BUCKET_APPEND_ALL,File.class, File.class, new FileDecoder())
.append(BUCKET_APPEND_ALL,Drawable.class, Drawable.class, new UnitDrawableDecoder())
registry.append(BUCKET_APPEND_ALL,ByteBuffer.class, Bitmap.class, byteBufferVideoDecoder);
registry.append(BUCKET_APPEND_ALL,ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, byteBufferVideoDecoder));

transcoderRegistry保存的数据
.register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources))
.register(Bitmap.class, byte[].class, bitmapBytesTranscoder)
.register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder))
.register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);























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

在EngineJob第128行的start()方法中
调用executor.execute(decodeJob);切换到子线程执行

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

.append(String.class, InputStream.class, new DataUrlLoader.StreamFactory<String>())
.append(String.class, InputStream.class, new StringLoader.StreamFactory())
.append(String.class, ParcelFileDescriptor.class, new StringLoader.FileDescriptorFactory())
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())

new DataUrlLoader()

ModelLoaderCache里面的结构如下
Map<Class<?>, Entry<?>>
其中Entry结构如下
List<ModelLoader<Model, ?>>
                                  
上面的hasNextModelLoader()方法最终会调用到DecodeHelper第208行的getLoaData()方法。
DecodeHelper第212行调用getModelLoaders()最终会进入到ModelLoaderRegistry第74行的
getModelLoaders(@NonNull A model)，在ModelLoaderRegistry第74行的getModelLoaders()方法中，
第75行调用第110行的getModelLoadersForClass()方法

第一次进入cache是拿不到数据的，所以会调用multiModelLoaderFactory.build(modelClass)创建和String
有关的ModerLoader，进入MultiModelLoaderFactory的第91行的build方法
Entry的结构如下：
private static class Entry<Model, Data> {
    private final Class<Model> modelClass;
    @Synthetic final Class<Data> dataClass;
    @Synthetic final ModelLoaderFactory<? extends Model, ? extends Data> factory;
}
Entry分别对应了注册的下面参数
.append(String.class, AssetFileDescriptor.class, new StringLoader.AssetFileDescriptorFactory())

他会从entries这个list里面循环查找，然后调用handles方法判断是否满足modelClass的条件，当是String参数的时候，
找到满足条件如
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

上面代码执行完成之后，loaders的数据如下：
List<ModelLoader<Model, ?>> loaders
[0] = DataUrlLoader

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
如果上面创建的ModelLoader数量大于1，还会调用下面方法包装成MultiModelLoader
factory.build(loaders, throwableListPool)
如果数量等于1，就直接返回这个列表中的第一个ModelLoader实例

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
List<ModelLoader<Model, ?>>
[0]=DataUrlLoader<String, InputStream>
[1]=StringLoader<String, InputStream>
    ModelLoader<Uri.class, InputStream.class> uriLoader = MultiModelLoader<Uri.class, InputStream.class>
        List<Uri, InputStream.class>
        DataUrlLoader<Uri, InputStream.class>
        AssetUriLoader<Uri, InputStream.class>
        MediaStoreImageThumbLoader<Uri, InputStream.class>
        MediaStoreVideoThumbLoader<Uri, InputStream.class>
        QMediaStoreUriLoader<Uri, InputStream.class>
        UriLoader<Uri, InputStream.class>
        UrlUriLoader<Uri, InputStream.class>
            urlLoader=HttpGlideUrlLoader<GlideUrl, InputStream>
[2]=StringLoader<String, ParcelFileDescriptor>
    uriLoader = MultiModelLoader<String, InputStream>->List
        QMediaStoreUriLoader<Uri, ParcelFileDescriptor>
        UriLoader<Uri, ParcelFileDescriptor>
[3]=StringLoader<String, AssetFileDescriptor>
    uriLoader = MultiModelLoader<String, InputStream>->List
        AssetUriLoader<Uri, AssetFileDescriptor>
        UriLoader<Uri, AssetFileDescriptor>

返回ModelLoaderRegistry第110行的getModelLoadersForClass()方法，cache会存储(String, List<ModelLoader<A, ?>>)
列表里面的内容就是上面3个StringLoader。

然后回到ModelLoaderRegistry第74行的getModelLoaders()方法，这里拿到了List<String, List<ModelLoader<A, ?>>
之后，接着会使用for循环，调用每个ModelLoad的handles()方法，把符合条件的都存放到filteredLoaders并返回。
然后再ModelLoaderRegistry 的getModelLoaders()方法会对得到的list过滤一遍，通过handles()方法
过滤之后就只有3个对象了
[0]=StringLoader<String, InputStream>
    ModelLoader<Uri.class, InputStream.class> uriLoader = MultiModelLoader<Uri.class, InputStream.class>
        List<Uri, InputStream.class>
        DataUrlLoader<Uri, InputStream.class>
        AssetUriLoader<Uri, InputStream.class>
        MediaStoreImageThumbLoader<Uri, InputStream.class>
        MediaStoreVideoThumbLoader<Uri, InputStream.class>
        QMediaStoreUriLoader<Uri, InputStream.class>
        UriLoader<Uri, InputStream.class>
        UrlUriLoader<Uri, InputStream.class>
            urlLoader=HttpGlideUrlLoader<GlideUrl, InputStream>
[1]=StringLoader<String, ParcelFileDescriptor>
    uriLoader = MultiModelLoader<String, InputStream>->List
        QMediaStoreUriLoader<Uri, ParcelFileDescriptor>
        UriLoader<Uri, ParcelFileDescriptor>
[2]=StringLoader<String, AssetFileDescriptor>
    uriLoader = MultiModelLoader<String, InputStream>->List
        AssetUriLoader<Uri, AssetFileDescriptor>
        UriLoader<Uri, AssetFileDescriptor>

然后回到DecodeHelper第208行的getLoadData()方法。
DecodeHelper getLoadData()
通过上面生成的ModelLoader重新生成对应的LoadData对象列表，并返回
LoadData结构
class LoadData<Data> {
    public final Key sourceKey;
    public final List<Key> alternateKeys;
    public final DataFetcher<Data> fetcher;
}
第一个StringLoader<String, InputStream>，调用buildLoadData()方法会进入StringLoader中第29行的buildLoadData()
方法。uriLoader的类型是MultiModelLoader，然后会调用MultiModelLoader第62行的handles()方法，
在这个方法里面for循环判断UrlUriLoader为true，然后回到StringLoader中的buildLoadData()方法中，
接着调用MultiModelLoader的buildLoadData(方法)。所以通过MultiModelLoader<String, InputStream>的
buildLoadData()方法创建LoadData，
在buildLoadData()方法这里循环，这一轮一共有7条数据，找到符合条件的只有UrlUriLoader。
内部再次调用子UrlUriLoader的buildLoadData()方法
进行创建LoadData， UrlUriLoader内部又是通过HttpGlideUrlLoader的buildLoadData()方法进行创建LoadData，
创建时又新建了一个HttpUrlFetcher对象，传入到LoadData。 

urlLoader是一个HttpGlideUrlLoader类型的实例，因为在创建UrlUriLoader的时候，使用了下面代码创建：
public ModelLoader<Uri, InputStream> build(MultiModelLoaderFactory multiFactory) {
    return new UrlUriLoader<>(multiFactory.build(GlideUrl.class, InputStream.class));
}
HttpGlideUrlLoader第42行的buildLoadData()方法内部创建了LoaData(GlideUrl, HttpUrlFetcher)然后返回。
回到MultiModelLoader第40行的buildLoadData方法，创建完成之后，最后返回新的LoadData(GlideUrl, MultiFetcher<>(List<DataFetcher<Data>>, exceptionListPool))
的对象。最后回到DecodeHelper第222行，返回了一个LoadData列表。
它此次的数据结构如下
MultiModelLoader.LoadData {
    sourceKey
    MultiFetcher {
        List<DataFetcher<Data>> fetchers
            [0] = HttpUrlFetcher 对象
    } 
}
返回到DecodeHelper第217行，返回的LoadData如果不为空的话，会添加到loadData列表。
所以第一轮循环loadData的数据为:
[0]=
MultiModelLoader.LoadData {
    sourceKey
    MultiFetcher {
        List<DataFetcher<Data>> fetchers
            [0] = HttpUrlFetcher 对象
    }
}

接着进行第二轮循环，进入StringLoader的buildLoadData()方法，第32行的handles(uri)返回false，所以直接返回
null。
接着进行第三轮循环，进入StringLoader的buildLoadData()方法，第32行的handles(uri)返回false，所以直接返回
null。
最后DecodeHelper第222行返回的loadData数据如下：
[0]=
    MultiModelLoader.LoadData {
    sourceKey
    MultiFetcher {
        List<DataFetcher<Data>> fetchers
        [0] = HttpUrlFetcher 对象
    }
}

DecodeHelper第209行有个isLoadDataSet标志，首次请求时false，获取了loadData数据之后是true，后面再次调用
getLoadData()方法，就可以直接返回成员变量loadData。

回到SourceGenerator第82行， 接下来会调用DecodeHelper的hasLoadPath()这个方法，这个方法返回true之后，
才会继续下一步
helper.hasLoadPath(loadData.fetcher.getDataClass())
这个方法最终会调用到Registry第475行的getLoadPath()方法
第480行调用了LoadPathCache的get()方法，在LoadPathCache第52行的get()方法内部，首先调用了LoadPathCache
第76行的getKey方法生成一个MultiClassKey的对象， 这个方法内部会创建一个MultiClassKey，并且通过
set(@NonNull Class<?> first, @NonNull Class<?> second, @Nullable Class<?> third)设置参数

然后回到Registry第475 getLoadPath()方法，返回的result=null，会去新建一个LoadPath
new LoadPath<>(java.io.InputStream, java.lang.Object, android.graphics.drawable.Drawable,
decodePaths, throwableListPool);
接下来进入getDecodePaths()方法：
传入的参数是：
dataClass = java.io.InputStream,
resourceClass = java.lang.Object,
transcodeClass = android.graphics.drawable.Drawable
接着会调用ResourceDecoderRegistry的getResourceClasses(java.io.InputStream, java.lang.Object)方法
在getResourceClasses()方法内部，会根据分类bucketPriorityList进行查找
首先查找legacy_prepend_all分类，找到了有4个
entries：
[0] = {dataClass=class java.io.InputStream, decoder=StreamWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[2] = {dataClass=class java.io.InputStream, decoder=StreamWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[3] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
然后把上面entries中dataClass和java.io.InputStream同一类型或者是java.io.InputStream父类的数据，并且resourceClass和java.lang.Object
同一类型或者是java.lang.Object子类的数据都找出来
2个点：
dataClass是class java.io.InputStream的父类或相等
resourceClass是java.lang.Object子类或相等。
第一轮下来，找到com.bumptech.glide.integration.webp.decoder.WebpDrawable，其实有2条数据满足但是getResourceClasses
方法会判断列表中是否存在相等的ResourceClasses，只有不相等的才会添加到列表中。
第二轮 分类是Animation 找到2个
entries：
[0] = {dataClass=class java.io.InputStream, decoder=StreamGifDecoder, resourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferGifDecoder, resourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable}
上面数据中第0条满足条件，此时把com.bumptech.glide.load.resource.gif.GifDrawable添加到result列表

第三轮 分类是Bitmap 找到15个
[0] = {dataClass=class java.io.InputStream, decoder=StreamAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[2] = {dataClass=class java.io.InputStream, decoder=StreamBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[3] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[4] = {dataClass=class java.io.InputStream, decoder=StreamAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[5] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[6] = {dataClass=class java.io.InputStream, decoder=StreamBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[7] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[8] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[9] = {dataClass=class java.io.InputStream, decoder=StreamBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[10] = {dataClass=class android.os.ParcelFileDescriptor, decoder=ParcelFileDescriptorBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[11] = {dataClass=class android.os.ParcelFileDescriptor, decoder=VideoDecoder, resourceClass=class android.graphics.Bitmap}
[12] = {dataClass=class android.content.res.AssetFileDescriptor, decoder=VideoDecoder, resourceClass=class android.graphics.Bitmap}
[13] = {dataClass=class android.graphics.Bitmap, decoder=UnitBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[14] = {dataClass=interface com.bumptech.glide.gifdecoder.GifDecoder, decoder=GifFrameResourceDecoder, resourceClass=class android.graphics.Bitmap}
上面数据中有很多满足条件条件，去重之后把class android.graphics.Bitmap添加到result列表

第四轮 分类是BitmapDrawable 找到7个
[0] = {dataClass=class java.io.InputStream, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[2] = {dataClass=class java.io.InputStream, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[3] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[4] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[5] = {dataClass=class java.io.InputStream, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[6] = {dataClass=class android.os.ParcelFileDescriptor, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
上面数据中有很多满足条件条件，去重之后把class android.graphics.drawable.BitmapDrawable添加到result列表

第五轮 分类是legacy_append 找到6个
[0] = {dataClass=class android.net.Uri, decoder=ResourceDrawableDecoder, resourceClass=class android.graphics.drawable.Drawable}
[1] = {dataClass=class android.net.Uri, decoder=ResourceBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[2] = {dataClass=class java.io.File, decoder=FileDecoder, resourceClass=class java.io.File}
[3] = {dataClass=class android.graphics.drawable.Drawable, decoder=UnitDrawableDecoder, resourceClass=class android.graphics.drawable.Drawable}
[4] = {dataClass=class java.nio.ByteBuffer, decoder=VideoDecoder, resourceClass=class android.graphics.Bitmap}
[5] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}

这个方法执行完之后，返回了一个List列表，里面存储的是Class对象,内容如下：
registeredResourceClasses:
[0]=class com.bumptech.glide.integration.webp.decoder.WebpDrawable
[1]=class com.bumptech.glide.load.resource.gif.GifDrawable
[2]=class android.graphics.Bitmap
[3]=class android.graphics.drawable.BitmapDrawable
然后返回到Registry第506行，这里的registeredResourceClasses列表存储的就是上面4个Class对象
然后通过找到的ResourceClass从已经注册在TranscoderRegistry中找到匹配的registeredTranscodeClasses
调用TranscoderRegistry第64行的getTranscodeClasses()方法，
transcoderRegistry里面的transcoder类型是List<Entry<?, ?>>，Entry的类型是TranscoderRegistry里面的Entry，
存储的内容是：
[0]={fromClass=class android.graphics.Bitmap, toClass=class android.graphics.drawable.BitmapDrawable, transcoder=BitmapDrawableTranscoder}
[1]={fromClass=class android.graphics.Bitmap, toClass=class [B, transcoder=BitmapByteTranscoder}
[2]={fromClass=class android.graphics.drawable.Drawable, toClass=class [B, transcoder=DrawableByteTranscoder}
[3]={fromClass=class com.bumptech.glide.load.resource.gif.GifDrawable, toClass=class [B, transcoder=GifDrawableByteTranscoder}
和前面注册的是一样的：
.register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources))
.register(Bitmap.class, byte[].class, bitmapBytesTranscoder)
.register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder))
.register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);

接着调用ResourceDecoderRegistry的getTranscodeClasses()方法，
第一次传入的参数是(class com.bumptech.glide.integration.webp.decoder.WebpDrawable, class android.graphics.drawable.Drawable)
返回的数据只有一条：
class android.graphics.drawable.Drawable
registeredTranscodeClasses最后的数据如下
[0]=class android.graphics.drawable.Drawable


在第二个for循环内部：
registeredResourceClass = class com.bumptech.glide.integration.webp.decoder.WebpDrawable
registeredTranscodeClass = class android.graphics.drawable.Drawable
第515行调用了ResourceDecoderRegistry的getDecoders()方法
dataClass = class java.io.InputStream
registeredResourceClass = class com.bumptech.glide.integration.webp.decoder.WebpDrawable
流程和上面的getResourceClasses()方法差不多，只不过这里添加到列表中的是decoder，getResourceClasses()添加的是resourceClass对象。
在getDecoders()方法内部，会根据分类bucketPriorityList进行查找
首先查找legacy_prepend_all分类，找到了有4个
entries：
[0] = {dataClass=class java.io.InputStream, decoder=StreamWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[2] = {dataClass=class java.io.InputStream, decoder=StreamWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[3] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
然后把上面entries中dataClass和java.io.InputStream同一类型或者是java.io.InputStream父类的数据，并且resourceClass和java.lang.Object
同一类型或者是java.lang.Object子类的数据都找出来
2个点：
dataClass是(参数)class class java.io.InputStream的父类或相等
resourceClass是(参数)class android.graphics.drawable.Drawable子类或相等。
第一轮下来，满足条件的数据如下：
[0] = {dataClass=class java.io.InputStream, decoder=StreamWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
[2] = {dataClass=class java.io.InputStream, decoder=StreamWebpDecoder, resourceClass=class com.bumptech.glide.integration.webp.decoder.WebpDrawable}
所以会把2个一样的StreamWebpDecoder对象都添加到列表中进行返回，

第二轮 分类是Animation 找到2个
entries：
[0] = {dataClass=class java.io.InputStream, decoder=StreamGifDecoder, resourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferGifDecoder, resourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable}
不符合

第三轮 分类是Bitmap 找到15个
[0] = {dataClass=class java.io.InputStream, decoder=StreamAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[2] = {dataClass=class java.io.InputStream, decoder=StreamBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[3] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[4] = {dataClass=class java.io.InputStream, decoder=StreamAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[5] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferAnimatedBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[6] = {dataClass=class java.io.InputStream, decoder=StreamBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[7] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferBitmapWebpDecoder, resourceClass=class android.graphics.Bitmap}
[8] = {dataClass=class java.nio.ByteBuffer, decoder=ByteBufferBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[9] = {dataClass=class java.io.InputStream, decoder=StreamBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[10] = {dataClass=class android.os.ParcelFileDescriptor, decoder=ParcelFileDescriptorBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[11] = {dataClass=class android.os.ParcelFileDescriptor, decoder=VideoDecoder, resourceClass=class android.graphics.Bitmap}
[12] = {dataClass=class android.content.res.AssetFileDescriptor, decoder=VideoDecoder, resourceClass=class android.graphics.Bitmap}
[13] = {dataClass=class android.graphics.Bitmap, decoder=UnitBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[14] = {dataClass=interface com.bumptech.glide.gifdecoder.GifDecoder, decoder=GifFrameResourceDecoder, resourceClass=class android.graphics.Bitmap}
不符合

第四轮 分类是BitmapDrawable 找到7个
[0] = {dataClass=class java.io.InputStream, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[1] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[2] = {dataClass=class java.io.InputStream, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[3] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[4] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[5] = {dataClass=class java.io.InputStream, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
[6] = {dataClass=class android.os.ParcelFileDescriptor, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
不符合

第五轮 分类是legacy_append 找到6个
[0] = {dataClass=class android.net.Uri, decoder=ResourceDrawableDecoder, resourceClass=class android.graphics.drawable.Drawable}
[1] = {dataClass=class android.net.Uri, decoder=ResourceBitmapDecoder, resourceClass=class android.graphics.Bitmap}
[2] = {dataClass=class java.io.File, decoder=FileDecoder, resourceClass=class java.io.File}
[3] = {dataClass=class android.graphics.drawable.Drawable, decoder=UnitDrawableDecoder, resourceClass=class android.graphics.drawable.Drawable}
[4] = {dataClass=class java.nio.ByteBuffer, decoder=VideoDecoder, resourceClass=class android.graphics.Bitmap}
[5] = {dataClass=class java.nio.ByteBuffer, decoder=BitmapDrawableDecoder, resourceClass=class android.graphics.drawable.BitmapDrawable}
不符合

最后
第一轮下来
dataClass=java.io.InputStream
registeredResourceClass = class com.bumptech.glide.integration.webp.decoder.WebpDrawable
registeredTranscodeClass=class android.graphics.drawable.Drawable
decoders=List<ResourceDecoder<Data, TResource>>
[0]=StreamWebpDecoder对象
[1]=StreamWebpDecoder对象
transcoder=UnitTranscoder
然后第520行就使用了上面参数new DecodePath();

然后继续509行的循环，第二轮
registeredResourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable
返回的registeredTranscodeClasses是一个list
[0]=class android.graphics.drawable.Drawable
第515行，传入的参数是
dataClass=class java.io.InputStream
resourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable
找到的result只有一个结果
[0]=StreamGifDecoder对象
第518行，调用transcoderRegistry的get()方法，传入的参数是：
registeredResourceClass=class com.bumptech.glide.load.resource.gif.GifDrawable
registeredTranscodeClass=class android.graphics.drawable.Drawable
返回的transcoder：
transcoder= UnitTranscoder对象
最后使用下面的参数new DecodePath()
dataClass=java.io.InputStream
registeredResourceClass = com.bumptech.glide.load.resource.gif.GifDrawable
registeredTranscodeClass=class android.graphics.drawable.Drawable
decoders=List<ResourceDecoder<Data, TResource>>
[0]=StreamWebpDecoder对象
transcoder=UnitTranscoder

回到513行的循环，第三轮
registeredResourceClass=class android.graphics.Bitmap
然后第511行开始调用transcoderRegistry的getTranscodeClasses()方法，
传入参数是：
resourceClass=class android.graphics.Bitmap
transcodeClass=class android.graphics.drawable.Drawable
进入getTranscodeClasses()方法
这时会走进行第73行的for循环，transcoders的内容如下：
[0]={fromClass=class android.graphics.Bitmap, toClass=class android.graphics.drawable.BitmapDrawable, transcoder=BitmapDrawableTranscoder}
[1]={fromClass=class android.graphics.Bitmap, toClass=class [B, transcoder=BitmapByteTranscoder}
[2]={fromClass=class android.graphics.drawable.Drawable, toClass=class [B, transcoder=DrawableByteTranscoder}
[3]={fromClass=class com.bumptech.glide.load.resource.gif.GifDrawable, toClass=class [B, transcoder=GifDrawableByteTranscoder}
这一轮循环下来满足条件的只有
[0]={fromClass=class android.graphics.Bitmap, toClass=class android.graphics.drawable.BitmapDrawable, transcoder=BitmapDrawableTranscoder}
所以把class android.graphics.drawable.BitmapDrawable放到列表并返回
registeredTranscodeClasses：
[0]=class android.graphics.drawable.BitmapDrawable

第515行，传入的参数是
dataClass=class java.io.InputStream
resourceClass=class android.graphics.Bitmap
找到的result只有5个结果
[0]=StreamAnimatedBitmapDecoder对象
[1]=StreamBitmapWebpDecoder对象
[2]=StreamAnimatedBitmapDecoder对象
[3]=StreamBitmapWebpDecoder对象
[4]=StreamBitmapDecoder对象

第518行，调用transcoderRegistry的get()方法，传入的参数是：
registeredResourceClass=class android.graphics.Bitmap
registeredTranscodeClass=class android.graphics.drawable.BitmapDrawable
这个时候会进入TranscoderRegistry到第52行，transcoders的内容如下：
[0]={fromClass=class android.graphics.Bitmap, toClass=class android.graphics.drawable.BitmapDrawable, transcoder=BitmapDrawableTranscoder}
[1]={fromClass=class android.graphics.Bitmap, toClass=class [B, transcoder=BitmapByteTranscoder}
[2]={fromClass=class android.graphics.drawable.Drawable, toClass=class [B, transcoder=DrawableByteTranscoder}
[3]={fromClass=class com.bumptech.glide.load.resource.gif.GifDrawable, toClass=class [B, transcoder=GifDrawableByteTranscoder}
只有一个满足条件：
[0]={fromClass=class android.graphics.Bitmap, toClass=class android.graphics.drawable.BitmapDrawable, transcoder=BitmapDrawableTranscoder}
返回的transcoder：
transcoder= BitmapDrawableTranscoder对象
最后使用下面的参数new DecodePath()
dataClass=java.io.InputStream
registeredResourceClass = class android.graphics.Bitmap
registeredTranscodeClass=class android.graphics.drawable.BitmapDrawable
decoders=List<ResourceDecoder<Data, TResource>>
[0]=StreamAnimatedBitmapDecoder对象
[1]=StreamBitmapWebpDecoder对象
[2]=StreamAnimatedBitmapDecoder对象
[3]=StreamBitmapWebpDecoder对象
[4]=StreamBitmapDecoder对象
transcoder=BitmapDrawableTranscoder

回到513行的循环，第四轮
registeredResourceClass=class android.graphics.drawable.BitmapDrawable
然后第511行开始调用transcoderRegistry的getTranscodeClasses()方法，
传入参数是：
resourceClass=class android.graphics.drawable.BitmapDrawable
transcodeClass=class android.graphics.drawable.Drawable
返回的registeredTranscodeClasses是一个list
[0]=class android.graphics.drawable.Drawable
第515行，传入的参数是
dataClass=class java.io.InputStream
resourceClass=class android.graphics.drawable.BitmapDrawable
找到的result只有一个结果
[0]=BitmapDrawableDecoder对象
[1]=BitmapDrawableDecoder对象
[2]=BitmapDrawableDecoder对象
第518行，调用transcoderRegistry的get()方法，传入的参数是：
registeredResourceClass=class android.graphics.drawable.BitmapDrawable
registeredTranscodeClass=class android.graphics.drawable.Drawable
返回的transcoder：
transcoder= UnitTranscoder对象
最后使用下面的参数new DecodePath()
dataClass=java.io.InputStream
registeredResourceClass = class android.graphics.drawable.BitmapDrawable
registeredTranscodeClass=class android.graphics.drawable.Drawable
decoders=List<ResourceDecoder<Data, TResource>>
[0]=BitmapDrawableDecoder对象
[1]=BitmapDrawableDecoder对象
[2]=BitmapDrawableDecoder对象
transcoder=UnitTranscoder

然后回到Registry第484行，返回的 decodePaths是一个List，里面有下面3个元素
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
最后在Registry第491行中，把获取的decodePaths封装成了一个LoadPath对象
参数如下：
dataClass=class java.io.InputStream
resourceClass=class java.lang.Object
transcodeClass=class android.graphics.drawable.Drawable
decodePaths=上面说的List
throwableListPool=FactoryPools.threadSafeList()
然后把这个LoadPath对象扔到loadPathCache缓存池中。

接着到SourceGenerator 第88行的startNextLoad()
这里传入的参数如下：
[0]=
MultiModelLoader.LoadData {
    sourceKey
    MultiFetcher {
        List<DataFetcher<Data>> fetchers
        [0] = HttpUrlFetcher 对象
    }
}
调用loadData.fetcher.loadData(),实际上调用的是HttpUrlFetcher 的loadData()方法，
InputStream result = loadDataWithRedirects(glideUrl.toURL(), 0, null, glideUrl.getHeaders());
callback.onDataReady(result);
然后会回调到SourceGenerator 的startNextLoad()方法内部第101行的onDataReady()方法，接着调用
第202行的onDataReadyInternal()方法，这里先忽略缓存，最后会回调到到DecodeJob的onDataFetcherReady()方法
然后回调到DecodeJob第379行的onDataFetcherReady()方法，在这个方法内部会判断是否在同一个线程，如果不同线程，
就会调用下面的方法进行切换：
if (Thread.currentThread() != currentThread) {
    runReason = RunReason.DECODE_DATA;
    callback.reschedule(this);
} else {
    GlideTrace.beginSection("DecodeJob.decodeFromRetrievedData");
    try {
        decodeFromRetrievedData();
    } finally {
        GlideTrace.endSection();
    }
}
DecodeJob 430行的decodeFromRetrievedData()在这个方法内部进行解码数据，拿到Resource结果。
DecodeJob#decodeFromData() 拿到Resource结果，DataFetcher做出cleanup操作（也就是HttpUrlFetcher关闭流并且断开连接）
DecodeJob 496行decodeFromFetcher() 获取一个解码的途径来拿到Resource结果，在这里进行真正的解码
第498行获取的path数据和之前dataClass=class java.io.InputStream的情况差不多
只不过现在的dataClass=class com.bumptech.glide.util.ContentLengthInputStream
第498行获取的path数据如下：
DecodePath{
    dataClass=class com.bumptech.glide.util.ContentLengthInputStream
    decoders=[
        com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@db60999,
        com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@2c4e45e
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
DecodePath{
    dataClass=class com.bumptech.glide.util.ContentLengthInputStream,
    decoders=[
        com.bumptech.glide.load.resource.gif.StreamGifDecoder@a1e103f
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
DecodePath{
    dataClass=class com.bumptech.glide.util.ContentLengthInputStream,
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
dataClass=class com.bumptech.glide.util.ContentLengthInputStream,
    decoders=[
        com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@205009d,
        com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@8bf4912,
        com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@7a1c8e3
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}

然后调用DecodeJob的runLoadPath() 方法，传入的参数是
data=ContentLengthInputStream 对象
dataSource="REMOTE"
path=上面说的DecodePath对象
在DecodeJob第528行的runLoadPath()方法中，首先调用getOptionsWithHardwareConfig()方法
获取图片宽高、options，包裹InputStream。然后通过解码路径来拿到Resource结果
这里首先通过getOptionsWithHardwareConfig()方法获取解码配置，
然后把data数据包装成DataRewinder对象，这个对象主要是让数据流倒回起点重新读的工具。
因为有多个解码器读取数据，如果没有DataRewinder,第一个解码器读了一部分数据后，后面的解码器就拿不到完整数据了。
第535行调用LoadPath.load()方法,进入到LoadPath第48行的load()方法会调用loadWithExceptionList()方法，
进入到第63行的loadWithExceptionList()方法，在这个方法中
会遍历decodePaths变量，decodePaths一共有4个DecodePath对象
第一轮
获取的path：
DecodePath{
    dataClass=class com.bumptech.glide.util.ContentLengthInputStream
    decoders=[
        com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@db60999,
        com.bumptech.glide.integration.webp.decoder.StreamWebpDecoder@2c4e45e
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
然后调用DecodePath的decode()方法，在decode()方法内部，首先调用decodeResource()方法，最后调用
第77行的decodeResourceWithList()方法。在第86行，首先遍历decoders,第一个decoder是：
StreamWebpDecoder，然后调用handles()方法，这个方法返回了false，所以第一个StreamWebpDecoder不处理，继续
遍历下面的Decoder。
第二个StreamWebpDecoder也不处理，所以result==null。然后第109行会抛出一个GlideException()。返回DecodePath
第70行这里也会抛出GlideException()。
然后返回到LoadPath第76行，这里的result也会是null，并且会捕获GlideException异常。
到这里完成了第一轮的循环，接着进行下一轮，继续从decodePaths获取数据。

第二轮
获取的path：
DecodePath{
    dataClass=class com.bumptech.glide.util.ContentLengthInputStream,
    decoders=[
        com.bumptech.glide.load.resource.gif.StreamGifDecoder@a1e103f
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@da9e2e0
}
然后调用DecodePath的decode()方法，在decode()方法内部，首先调用decodeResource()方法，最后调用
第77行的decodeResourceWithList()方法。在第86行，首先遍历decoders,第一个decoder是：
StreamGifDecoder，然后调用handles()方法，这个方法返回了false，所以第一个StreamWebpDecoder不处理，继续
遍历下面的Decoder。
然后第109行会抛出一个GlideException()。返回DecodePath
第70行这里也会抛出GlideException()。
然后返回到LoadPath第76行，这里的result也会是null，并且会捕获GlideException异常。
到这里完成了第二轮的循环，接着进行下一轮，继续从decodePaths获取数据。

第三轮
获取的path：
DecodePath{
    dataClass=class com.bumptech.glide.util.ContentLengthInputStream,
    decoders=[
        com.bumptech.glide.integration.webp.decoder.StreamAnimatedBitmapDecoder@77a4d0c,
        com.bumptech.glide.integration.webp.decoder.StreamBitmapWebpDecoder@5d4a655,
        com.bumptech.glide.integration.webp.decoder.StreamAnimatedBitmapDecoder@f6e86a,
        com.bumptech.glide.integration.webp.decoder.StreamBitmapWebpDecoder@fed715b,
        com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@8052df8
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@40652d1
}
然后调用DecodePath的decode()方法，在decode()方法内部，首先调用decodeResource()方法，最后调用
第77行的decodeResourceWithList()方法。在第86行，首先遍历decoders,
第一个decoder是：
StreamAnimatedBitmapDecoder，然后调用handles()方法，这个方法返回了false，所以第一个StreamAnimatedBitmapDecoder
不处理，继续遍历下面的Decoder。
第二个StreamBitmapWebpDecoder也不处理
第三个StreamAnimatedBitmapDecoder也不处理
第四个StreamBitmapWebpDecoder也不处理
第五个StreamBitmapDecoder处理了
所以会调用下面的方法
data = rewinder.rewindAndGet();
这句话是让之前读过的流恢复到初始为止，否则前面已经读取过的数据读不到
然后进入第92行，调用StreamBitmapDecoder的decode()方法。
进入StreamBitmapDecoder第34行的decode()方法
这个方法在真正解码之前，给 InputStream 包了三层"保护套"，每一层解决一个问题。
第一层：RecyclableBufferedInputStream（加缓冲）
if (source instanceof RecyclableBufferedInputStream) {
    bufferedStream = (RecyclableBufferedInputStream) source;
    ownsBufferedStream = false;
} else {
    bufferedStream = new RecyclableBufferedInputStream(source, byteArrayPool);
    ownsBufferedStream = true;
}
原始的 InputStream 是从网络来的，每次读一点点都要走一次 IO 操作，效率很低。
RecyclableBufferedInputStream 作用是加一个缓冲区，一次性读一大块数据到内存中，后续读取直接从内存拿，
减少 IO 次数。 类比：你去仓库搬货，与其一个一个搬，不如推个小推车一次搬一批。
ownsBufferedStream 标记是不是自己创建的，如果是自己创建的，最后要自己负责释放。

第二层：ExceptionPassthroughInputStream（捕获异常）
ExceptionPassthroughInputStream exceptionStream = ExceptionPassthroughInputStream.obtain(bufferedStream);
解码图片时，Android 的 BitmapFactory.decodeStream() 有个问题：读取过程中出了 IO 异常，它不会抛出来，
而是悄悄返回一个残缺的 Bitmap 或 null。 
这一层的作用是拦截并记录异常，这样后面可以知道到底出了什么错，而不是莫名其妙地拿到一个 null。
类比：给流水线装一个监控摄像头，万一出了问题可以回看。

第三层：MarkEnforcingInputStream（保证可回退）
MarkEnforcingInputStream invalidatingStream = new MarkEnforcingInputStream(exceptionStream);
解码图片分两步：
先读图片头（获取宽高、格式等信息）
再从头开始读完整图片数据来解码
读完图片头后需要 reset() 回退到起始位置。但如果图片头太大，超过了缓冲区的 mark 限制，reset() 就会失效。
MarkEnforcingInputStream 的作用是：如果发现已经超出 mark 限制了，后续读取直接返回 -1（表示读完了），
强制阻止继续往前读，避免 reset 失败导致数据错乱。
类比：你往前走的时候拉着一根绳子，绳子长度有限，一旦拉到头了就不让你再往前走，确保你还能顺着绳子回来。

最后：交给 Downsampler 执行真正的解码
return downsampler.decode(invalidatingStream, width, height, options, callbacks);
三层包装完毕，交给 Downsampler 去做真正的 Bitmap 解码（包括采样、缩放等）。

进入到Downsampler第215行的decode()方法
ImageReader 是一个接口，定义了 Downsampler 需要对图片数据做的四个操作：
interface ImageReader {
    Bitmap decodeBitmap(Options options);   // 解码成 Bitmap
    ImageType getImageType();               // 获取图片格式（JPG？PNG？WebP？）
    int getImageOrientation();              // 获取 EXIF 旋转角度
    void stopGrowingBuffers();              // 锁死缓冲区大小
}
因为据源不只是 InputStream 一种。包了 ImageReader 之后，Downsampler 不需要关心这些细节，统一调接口就行。
数据源：
实现类	                        数据源	                            decodeBitmap() 调用
InputStreamImageReader	        InputStream（网络流）	            BitmapFactory.decodeStream()
ByteArrayReader	                byte[]（字节数组）	                BitmapFactory.decodeByteArray()
ByteBufferReader	            ByteBuffer（字节缓冲区）	            BitmapFactory.decodeStream()
FileReader	                    File（本地文件）	                    BitmapFactory.decodeStream()
ParcelFileDescriptorImageReader	ParcelFileDescriptor（文件描述符）	BitmapFactory.decodeFileDescriptor()
ImageReader 就是对"图片数据源"的统一封装。不管数据来自网络流、字节数组还是文件，Downsampler 
都用同一套接口来读取，并且每次读取前自动倒带，保证能反复读取同一份数据。
进入265行decode()方法,这些是从 Options 中取出各种解码配置参数，之前在 RequestBuilder 里通过 .format()、
.downsample() 等方法设置的值，最终都流到这里。
最后第285行调用decodeFromWrappedStreams()方法
这个方法就是 Downsampler 的核心流程，分 7 步。
第一步：读取图片原始宽高
int[] sourceDimensions = getDimensions(imageReader, options, callbacks, bitmapPool);
int sourceWidth = sourceDimensions[0];   // 比如 1920
int sourceHeight = sourceDimensions[1];  // 比如 1080
之前分析过，这里用 inJustDecodeBounds = true，只读图片头，不真正解码，拿到原图宽高。
if (sourceWidth == -1 || sourceHeight == -1) {
    isHardwareConfigAllowed = false;
}
如果宽高没读到（返回 -1），说明图片头解析失败了。这时候禁止使用 Hardware Bitmap，因为 Hardware Bitmap 
是不可修改的，而宽高未知的情况下可能需要后续调整，所以需要一个可修改（mutable）的 Bitmap。

第二步：读取 EXIF 旋转信息
int orientation = imageReader.getImageOrientation();
int degreesToRotate = TransformationUtils.getExifOrientationDegrees(orientation);
boolean isExifOrientationRequired = TransformationUtils.isExifOrientationRequired(orientation);
有些手机拍照时，传感器是横着的，拍出来的图片原始数据其实是横的，但 EXIF 里记了"需要旋转 90°"。比如：
图片文件里存的：        实际应该显示的：
┌──────────┐            ┌──────┐
│  横着的  │  旋转90°→  │竖着的│
└──────────┘            └──────┘

第三步：计算目标宽高
int targetWidth =
    requestedWidth == Target.SIZE_ORIGINAL
    ? (isRotationRequired(degreesToRotate) ? sourceHeight : sourceWidth)
    : requestedWidth;
int targetHeight =
    requestedHeight == Target.SIZE_ORIGINAL
    ? (isRotationRequired(degreesToRotate) ? sourceWidth : sourceHeight)
    : requestedHeight;
如果用户设了 SIZE_ORIGINAL（原始尺寸），目标宽高就用原图尺寸。但如果图片需要旋转 90°，宽和高要互换：
原图 1920 x 1080，需要旋转 90°：
targetWidth = sourceHeight = 1080
targetHeight = sourceWidth = 1920
如果不是 SIZE_ORIGINAL（比如用户指定了 200x200），就直接用用户指定的值。

第四步：计算缩放参数 + 解码格式
calculateScaling(imageType, imageReader, callbacks, bitmapPool,
    downsampleStrategy, degreesToRotate,
    sourceWidth, sourceHeight, targetWidth, targetHeight, options);
根据原图尺寸和目标尺寸，算出 inSampleSize（粗缩放）和 inDensity/inTargetDensity（精调缩放），写入 options。
比如原图 4000x4000，目标 200x200：
inSampleSize = 16     → 先粗缩到 250x250
inDensity / inTargetDensity → 再精调到 200x200
calculateConfig(imageReader, decodeFormat, isHardwareConfigAllowed,
isExifOrientationRequired, options, targetWidth, targetHeight);
决定解码格式，比如：
ARGB_8888：每像素 4 字节，有透明通道（PNG 需要）
RGB_565：每像素 2 字节，省内存但没透明通道（JPG 可以用）
HARDWARE：Bitmap 存在 GPU 显存中，省 Java 堆内存

第五步：从 BitmapPool 复用 Bitmap
if ((options.inSampleSize == 1 || isKitKatOrGreater) && shouldUsePool(imageType)) {
    // 计算解码后预期的宽高
    int expectedWidth;
    int expectedHeight;
    ...
    float densityMultiplier = (float) options.inTargetDensity / options.inDensity;
    int sampleSize = options.inSampleSize;
    int downsampledWidth = (int) Math.ceil(sourceWidth / (float) sampleSize);
    int downsampledHeight = (int) Math.ceil(sourceHeight / (float) sampleSize);
    expectedWidth = Math.round(downsampledWidth * densityMultiplier);
    expectedHeight = Math.round(downsampledHeight * densityMultiplier);
    ...
    if (expectedWidth > 0 && expectedHeight > 0) {
        setInBitmap(options, bitmapPool, expectedWidth, expectedHeight);
    }
}

这一步是性能优化。inBitmap 是 BitmapFactory 的一个功能：不用新申请内存，直接把像素写入一个已有的 Bitmap 中。
先算出解码后的预期尺寸，然后去 BitmapPool 找一个尺寸匹配的旧 Bitmap：
BitmapPool 里有之前回收的 Bitmap：[300x300] [250x250] [500x500]
我需要 250x250 → 找到了！把这个旧 Bitmap 设为 inBitmap
→ BitmapFactory 解码时直接复用它的内存，不用新申请

第六步：真正解码
Bitmap downsampled = decodeStream(imageReader, options, callbacks, bitmapPool);
callbacks.onDecodeComplete(bitmapPool, downsampled);
之前分析过，最终调到 imageReader.decodeBitmap(options) → BitmapFactory.decodeStream()。
onDecodeComplete 就是之前讲的 ExceptionPassthroughInputStream 检查异常的地方——如果读取过程中有 IOException，在这里重新抛出，丢弃残缺的 Bitmap。

第七步：旋转
if (downsampled != null) {
downsampled.setDensity(displayMetrics.densityDpi);

    rotated = TransformationUtils.rotateImageExif(bitmapPool, downsampled, orientation);
    if (!downsampled.equals(rotated)) {
        bitmapPool.put(downsampled);  // 旋转产生了新 Bitmap，把旧的回收
    }
}
return rotated;
根据第二步读到的 EXIF 信息，旋转图片。如果确实旋转了（产生了一个新的 Bitmap），就把旋转前的旧 Bitmap 
放回 BitmapPool 复用，不浪费。

第429行调用了decodeStream()方法进行解码，接着进入757行的decodeStream()方法，
这个方法分三部分：锁定缓冲区 → 解码 → 失败后降级重试。
第一部分：锁定缓冲区
if (!options.inJustDecodeBounds) {
    callbacks.onObtainBounds();
    imageReader.stopGrowingBuffers();
}
之前分析过，decodeStream() 会被调用两次：
第几次	inJustDecodeBounds	目的
第一次	true	            只读宽高，不解码
第二次	false	            真正解码
这个 if 判断的意思是：只有在第二次（真正解码）时才执行。
callbacks.onObtainBounds()：之前讲过，调用 fixMarkLimit()，锁死缓冲区大小
imageReader.stopGrowingBuffers()：同样的意思，不让缓冲区再扩容了
为什么？因为第一次读图片头时，缓冲区可以自由扩容来适应图片头。但第二次要读整张图片了，图片可能几十 MB，
如果缓冲区还能无限扩容，内存会爆掉。所以在真正解码前，锁死缓冲区大小。

第二部分：真正解码
int sourceWidth = options.outWidth;
int sourceHeight = options.outHeight;
String outMimeType = options.outMimeType;
先把宽高和格式信息保存一份，因为 BitmapFactory.decodeStream() 调用后会重置 options 里的这些值，
如果后面需要打日志就拿不到了。
TransformationUtils.getBitmapDrawableLock().lock();
try {
    result = imageReader.decodeBitmap(options);
} finally {
    TransformationUtils.getBitmapDrawableLock().unlock();
}
加锁解码。因为在某些旧版本 Android 上，BitmapFactory.decodeStream() 不是线程安全的，多个线程同时解码
可能崩溃，所以加锁保护。

第三部分：inBitmap 失败后降级重试
catch (IllegalArgumentException e) {
...
if (options.inBitmap != null) {
    bitmapPool.put(options.inBitmap);   // 把不合适的旧 Bitmap 还回池子
    options.inBitmap = null;             // 取消 inBitmap
    return decodeStream(imageReader, options, callbacks, bitmapPool);  // 重试
}
    throw bitmapAssertionException;
}
这是处理 inBitmap 复用失败的情况。
之前说过 inBitmap 是把新图片的像素写入旧 Bitmap 的内存中。但有时候旧 Bitmap 不合适，比如：
旧 Bitmap：200 x 200（内存只够放 200x200 的像素）
新图片解码后：250 x 250
BitmapFactory：你给我的画板太小了，画不下！→ 抛 IllegalArgumentException

第781行调用ImageReader的decodeBitmap()方法，然后会进入到内部类InputStreamImageReader第193行的
decodeBitmap()方法，在这里调用BitmapFactory.decodeStream()方法进行解码，然后就可以后去Bitmap对象了。
然后回到Downsampler第284行，这里返回的Bitmap对象会被包装成BitmapResource对象并返回。

DecodePath 执行三步操作
// DecodePath.java
public Resource<Transcode> decode(...) {
    // 第一步：解码（InputStream → Bitmap）
    Resource<ResourceType> decoded = decodeResource(rewinder, width, height, options);
    // 第二步：变换（缩放、裁剪等）
    Resource<ResourceType> transformed = callback.onResourceDecoded(decoded);
    // 第三步：转换（Bitmap → BitmapDrawable）
    return transcoder.transcode(transformed, options);
}

然后返回到DecodePath第59行，这里通过解码获取了BitmapResource对象，接着进行第二步，callback.onResourceDecoded(decoded)
callback.onResourceDecoded(decoded) 实际调用的是 DecodeJob.onResourceDecoded()
它做了两件事
第一件：应用 Transformation（变换）
比如你写了这样的代码：
Glide.with(this)
    .load(url)
    .centerCrop()      // ← 这就是一个 Transformation
    .into(imageView);
那在 onResourceDecoded() 里就会执行 CenterCropTransformation：
如果你没设置任何变换，默认会用 FitCenter。

第二件：决定是否写入磁盘缓存
变换完之后，判断要不要把变换后的结果缓存到磁盘。比如 DiskCacheStrategy.ALL 或 RESOURCE 的时候，
就会把变换后的 Bitmap 写入磁盘，下次加载同样的图片 + 同样的变换，直接从缓存读，不用重新解码和变换。
但你的 TestActivity1 用了 DiskCacheStrategy.NONE，所以这一步不会写缓存。

一句话总结
callback.onResourceDecoded(decoded) 就是对解码出来的 Bitmap 做 Transformation 变换（比如 centerCrop、
圆角等），然后决定要不要把变换结果写入磁盘缓存，最后返回变换后的 Resource<Bitmap>。

DecodePath第61行代码：
return transcoder.transcode(transformed, options);
把bitmap转成Drawable，transcoder的类型是：BitmapDrawableTranscoder，所以调用的是BitmapDrawableTranscoder
的transcode()方法。在这个transcode()方法内部，会返回一个LazyBitmapDrawableResource对象，调用这个对象
的get()方法，才会真正去创建BitmapDrawable对象。
@Nullable
@Override
public Resource<BitmapDrawable> transcode(
    @NonNull Resource<Bitmap> toTranscode, @NonNull Options options) {
    return LazyBitmapDrawableResource.obtain(resources, toTranscode);
}
@NonNull
@Override
public BitmapDrawable get() {
    return new BitmapDrawable(resources, bitmapResource.get());
}
DecodePath第52行的decode()方法返回之后，回到DecodeJob第430行代码。
这里返回的resource是LazyBitmapDrawableResource对象。

DecodeJob 436行 notifyEncodeAndRelease()
notifyEncodeAndRelease(resource, currentDataSource, isLoadingFromAlternateCacheKey);
做两件事：
初始化资源（如果需要）
"初始化资源"就是在子线程提前调 bitmap.prepareToDraw()，把像素数据上传到 GPU，
避免主线程显示图片时因为等待 GPU 传输而卡顿。
调 notifyComplete() 通知上层

第2步：DecodeJob.notifyComplete()
private void notifyComplete(Resource<R> resource, ...) {
    callback.onResourceReady(resource, dataSource, isLoadedFromAlternateCacheKey);
}
这里的 callback 就是 EngineJob。

第3步：EngineJob.onResourceReady()
public void onResourceReady(Resource<R> resource, ...) {
    this.resource = resource;
    notifyCallbacksOfResult();
}
注意：此时还在子线程（sourceExecutor）上。

第4步：EngineJob.notifyCallbacksOfResult() 切换到主线程
void notifyCallbacksOfResult() {
    // 把资源放到内存缓存
    engineJobListener.onEngineJobComplete(this, localKey, localResource);

    // 关键！通过 executor 切到主线程
    for (final ResourceCallbackAndExecutor entry : copy) {
        entry.executor.execute(new CallResourceReady(entry.cb));
    }
}
这里的 entry.executor 是什么？追溯到 RequestBuilder.into()：
// RequestBuilder.java
return into(target, null, Executors.mainThreadExecutor());
//                        ↑ 传入的是主线程 Executor
而 Executors.mainThreadExecutor() 的实现：
private static final Executor MAIN_THREAD_EXECUTOR = new Executor() {
    @Override
    public void execute(Runnable command) {
        Util.postOnUiThread(command);   // 通过 Handler 切到主线程
    }
};
所以从这一步开始，后面的代码都在主线程执行了。

第5步：CallResourceReady.run()（主线程）
public void run() {
    synchronized (cb.getLock()) {
        engineResource.acquire();
        callCallbackOnResourceReady(cb);   // cb 就是 SingleRequest
    }
}

第6步：SingleRequest.onResourceReady()（主线程）
public void onResourceReady(Resource<?> resource, ...) {
    R received = (R) resource.get();   // 560行 ← 这里调了 LazyBitmapDrawableResource.get()
    // 这时候才创建 BitmapDrawable！
    onResourceReady(resource, received, dataSource, isLoadedFromAlternateCacheKey);
}
然后进入内部的 onResourceReady()：
private void onResourceReady(Resource<R> resource, R result, ...) {
    // 通知 RequestListener（如果有的话）
    // 然后：
    target.onResourceReady(result, animation);
}

这里的 target 就是 DrawableImageViewTarget。

第7步：ImageViewTarget.onResourceReady()（主线程）
public void onResourceReady(Z resource, Transition<? super Z> transition) {
    if (transition == null || !transition.transition(resource, this)) {
        setResourceInternal(resource);
    }
}

private void setResourceInternal(Z resource) {
setResource(resource);        // 调子类的方法
    maybeUpdateAnimatable(resource); // 如果是动画，启动动画
}

第8步：DrawableImageViewTarget.setResource()（主线程）
protected void setResource(Drawable resource) {
    view.setImageDrawable(resource);   // 最终！设置到 ImageView 上
}


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

在第74行调用了helper.getTransformation(resourceClass)，它会从DecodeHelper中的transformations查找对应的
Transformation。
这个Transformation的初始化流程如下：
RequestBuilder 第839行into()方法开始，调用到第860行的下面方法
requestOptions = requestOptions.clone().optionalFitCenter();
然后调用到GlideRequest类中第321行的optionalFitCenter()方法，
然后调用BaseRequestOptions类中的第738行方法
调用BaseRequestOptions类第856行optionalScaleOnlyTransform()方法
调用BaseRequestOptions类第864行scaleOnlyTransform()方法
调用BaseRequestOptions类第822行optionalTransform()方法
调用BaseRequestOptions类第964行transform()方法
在这个方法内部加入了4个transform
代码如下
DrawableTransformation drawableTransformation = new DrawableTransformation(transformation, isRequired);
transform(Bitmap.class, new FitCenter(), isRequired);
transform(Drawable.class, drawableTransformation, isRequired);
transform(BitmapDrawable.class, drawableTransformation.asBitmapDrawable(), isRequired);
transform(GifDrawable.class, new GifDrawableTransformation(transformation), isRequired);

然后从上面的拿到resourceClass在这个transformations获取对应的Transformation，这个Transformation只是用来生成
磁盘缓存的Key，然后从磁盘缓存中获取cacheFile，然后从cacheFile获取对应的modelLoader。
在第91行，helper.getModelLoaders(cacheFile)最终会调用到ModelLoaderRegistry第74行的getModelLoaders()方法，
第75行获取的modelLoaders有4个
ByteBufferFileLoader
FileLoader
FileLoader
UnitModelLoader
最后返回的filteredLoaders也是上面4个值。
第103行helper.hasLoadPath()会对实例化LoadPath，并把dataClass，resourceClass和transcodeClass作为参数传入
缓存起来，最后缓存到Registry中的loadPathCache变量，然后返回对应的LoadPath对象。然后ResourceCacheGenerator
第103行会判断这个LoadPath对象是否为空，不为空就进入条件内部。




当modelLoaders不为空之后，会进入第98行的while()循环，


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
ResourceCacheGenerator 处理过的缓存（解码+变换后）
DataCacheGenerator 原始数据缓存（未处理）
SourceGenerator 从网络获取数据

当设置了DiskCacheStrategy.RESOURCE时，首先会从ResourceCacheGenerator查找缓存
如果存在的话，最后会调用第105行的loadData()方法，这个fetcher是ByteBufferFileLoader的
ByteBufferFetcher类型。查到之后，会回调callback的onDataReady()方法。
然后回调DecodeJob第379行的onDataFetcherReady()方法。

dataClass:源数据类型
resourceClass:目标数据类型
transcodeClass：转换成目标数据类型使用类的类型













































































DecodeHelper下面的方法 <Data> LoadPath<Data, ?, Transcode> getLoadPath(Class<Data> dataClass) 
{ return glideContext.getRegistry().getLoadPath(dataClass, resourceClass, transcodeClass); } resourceClass和transcodeClass从哪里传入的？

用你的代码来追踪
Glide.with(this)
.load(mUrl1)
.into(imgvTest);

完整传递链路
第1步：RequestManager — 确定 transcodeClass
// 你写的 .load(mUrl1) 内部会先调用 asDrawable()
public RequestBuilder<Drawable> asDrawable() {
return as(Drawable.class);  // transcodeClass = Drawable.class
}

public <ResourceType> RequestBuilder<ResourceType> as(Class<ResourceType> resourceClass) {
return new RequestBuilder<>(glide, this, resourceClass, context);
//                                      ↑ 这里的参数名虽然叫 resourceClass
//                                        但实际上是 transcodeClass
}

第2步：RequestBuilder — 存储 transcodeClass
public class RequestBuilder<TranscodeType> {
private final Class<TranscodeType> transcodeClass;  // = Drawable.class

    protected RequestBuilder(Glide glide, RequestManager requestManager,
                             Class<TranscodeType> transcodeClass, Context context) {
        this.transcodeClass = transcodeClass;  // 存起来
    }
}

第3步：.into() → 创建 SingleRequest
// RequestBuilder.java
SingleRequest.obtain(
...
transcodeClass,    // Drawable.class 传给 SingleRequest
...
);

第4步：SingleRequest.begin() → Engine.load()
// SingleRequest.java
loadStatus = engine.load(
...
requestOptions.getResourceClass(),  // resourceClass → 默认是 Object.class
transcodeClass,                      // transcodeClass → Drawable.class
...
);

resourceClass 从哪来？ 来自 RequestOptions，默认值是 Object.class，表示"中间产物不限制类型"。

第5步：Engine → DecodeJob
// Engine.java
DecodeJob<R> decodeJob = decodeJobFactory.build(
...
resourceClass,     // Object.class
transcodeClass,    // Drawable.class
...
);

第6步：DecodeJob → DecodeHelper
// DecodeJob.java
decodeHelper.init(
...
resourceClass,     // Object.class
transcodeClass,    // Drawable.class
...
);

第7步：DecodeHelper 存储
// DecodeHelper.java
class DecodeHelper<Transcode> {
private Class<?> resourceClass;          // = Object.class
private Class<Transcode> transcodeClass; // = Drawable.class

    void init(..., Class<?> resourceClass, Class<R> transcodeClass, ...) {
        this.resourceClass = resourceClass;
        this.transcodeClass = (Class<Transcode>) transcodeClass;
    }
}

总结
Glide.with(this).load(mUrl1).into(imgvTest)

transcodeClass 的来源：
asDrawable() → Drawable.class
→ RequestBuilder → SingleRequest → Engine → DecodeJob → DecodeHelper

resourceClass 的来源：
RequestOptions 的默认值 → Object.class
→ SingleRequest → Engine → DecodeJob → DecodeHelper

参数	值	谁决定的
transcodeClass	Drawable.class	asDrawable() 决定的，表示最终要 Drawable
resourceClass	Object.class	默认值，表示中间类型不限制
如果你写 asBitmap()，transcodeClass 就变成 Bitmap.class。
如果你写 as(PAGFile.class)，transcodeClass 就变成 PAGFile.class。

上面的流程你说清楚一点每一步都写清楚，我要追踪一下



好的，从你的代码开始，每一步都写清楚。

你的代码
Glide.with(this)
.load(mUrl1)
.diskCacheStrategy(DiskCacheStrategy.NONE)
.skipMemoryCache(true)
.override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
.into(imgvTest);

第1步：Glide.with(this)
// Glide.java
public static RequestManager with(Activity activity) {
return getRetriever(activity).get(activity);
}

返回一个 RequestManager 对象。

第2步：.load(mUrl1)
// RequestManager.java
public RequestBuilder<Drawable> load(String string) {
return asDrawable().load(string);
}

注意，这里先调用了 asDrawable()。

第3步：asDrawable()
// RequestManager.java
public RequestBuilder<Drawable> asDrawable() {
return as(Drawable.class);
}

第4步：as(Drawable.class)
// RequestManager.java
public <ResourceType> RequestBuilder<ResourceType> as(Class<ResourceType> resourceClass) {
return new RequestBuilder<>(glide, this, resourceClass, context);
//                                      ↑
//                              Drawable.class
}

第5步：new RequestBuilder(...)
// RequestBuilder.java
public class RequestBuilder<TranscodeType> {

    private final Class<TranscodeType> transcodeClass;

    protected RequestBuilder(Glide glide, RequestManager requestManager,
                             Class<TranscodeType> transcodeClass, Context context) {
        this.transcodeClass = transcodeClass;  // transcodeClass = Drawable.class
    }
}

到这里，transcodeClass = Drawable.class 确定了。

第6步：.into(imgvTest)
// RequestBuilder.java
public ViewTarget<ImageView, TranscodeType> into(ImageView view) {
// ...
return into(
glideContext.buildImageViewTarget(view, transcodeClass),
/*targetListener=*/ null,
requestOptions,
Executors.mainThreadExecutor());
}

第7步：into() 内部调用 buildRequest()
// RequestBuilder.java
private <Y extends Target<TranscodeType>> Y into(Y target, ...) {
// ...
Request request = buildRequest(target, targetListener, options, callbackExecutor);
target.setRequest(request);
requestManager.track(target, request);
return target;
}

第8步：buildRequest() → buildRequestRecursive() → obtainRequest()
// RequestBuilder.java
private Request obtainRequest(...) {
return SingleRequest.obtain(
context,
glideContext,
requestLock,
model,              // "https://img.ayomet.com/upload/banner/..."
transcodeClass,     // Drawable.class  ← 传给 SingleRequest
requestOptions,
overrideWidth,
overrideHeight,
priority,
target,
targetListener,
requestListeners,
requestCoordinator,
engine,
animationFactory,
callbackExecutor);
}

第9步：SingleRequest.obtain()
// SingleRequest.java
public class SingleRequest<R> {

    private final Class<R> transcodeClass;

    public static <R> SingleRequest<R> obtain(..., Class<R> transcodeClass, ...) {
        return new SingleRequest<>(..., transcodeClass, ...);
    }

    private SingleRequest(..., Class<R> transcodeClass, ...) {
        this.transcodeClass = transcodeClass;  // Drawable.class
    }
}

第10步：SingleRequest.begin() → onSizeReady()
请求开始执行，最终走到 onSizeReady()：

// SingleRequest.java
public void onSizeReady(int width, int height) {
// ...
loadStatus = engine.load(
glideContext,
model,                                    // "https://img.ayomet.com/..."
requestOptions.getSignature(),
this.width,
this.height,
requestOptions.getResourceClass(),        // Object.class ← resourceClass 从这里来
transcodeClass,                            // Drawable.class
priority,
requestOptions.getDiskCacheStrategy(),
requestOptions.getTransformations(),
requestOptions.isTransformationRequired(),
requestOptions.isScaleOnlyOrNoTransform(),
requestOptions.getOptions(),
requestOptions.isMemoryCacheable(),
requestOptions.getUseUnlimitedSourceGeneratorsPool(),
requestOptions.getUseAnimationPool(),
requestOptions.getOnlyRetrieveFromCache(),
this,
callbackExecutor);
}

resourceClass 从哪来？

// BaseRequestOptions.java
private Class<?> resourceClass = Object.class;  // 默认值

public Class<?> getResourceClass() {
return resourceClass;  // 你没有设置过，所以是默认值 Object.class
}

第11步：Engine.load()
// Engine.java
public <R> LoadStatus load(
GlideContext glideContext,
Object model,                    // "https://img.ayomet.com/..."
// ...
Class<?> resourceClass,          // Object.class
Class<R> transcodeClass,         // Drawable.class
// ...
) {
// ...
DecodeJob<R> decodeJob = decodeJobFactory.build(
glideContext,
model,
key,
signature,
width,
height,
resourceClass,               // Object.class    ← 传给 DecodeJob
transcodeClass,              // Drawable.class   ← 传给 DecodeJob
// ...
);
}

第12步：DecodeJob.init()
// DecodeJob.java
DecodeJob<R> init(
// ...
Class<?> resourceClass,          // Object.class
Class<R> transcodeClass,         // Drawable.class
// ...
) {
decodeHelper.init(
glideContext,
model,
signature,
width,
height,
diskCacheStrategy,
resourceClass,               // Object.class    ← 传给 DecodeHelper
transcodeClass,              // Drawable.class   ← 传给 DecodeHelper
// ...
);
}

第13步：DecodeHelper.init() — 最终存储
// DecodeHelper.java
class DecodeHelper<Transcode> {
private Class<?> resourceClass;
private Class<Transcode> transcodeClass;

    <R> void init(
        // ...
        Class<?> resourceClass,
        Class<R> transcodeClass,
        // ...
    ) {
        this.resourceClass = resourceClass;                        // Object.class
        this.transcodeClass = (Class<Transcode>) transcodeClass;   // Drawable.class
    }
}

第14步：DecodeHelper.getLoadPath() 使用这两个值
// DecodeHelper.java
<Data> LoadPath<Data, ?, Transcode> getLoadPath(Class<Data> dataClass) {
return glideContext.getRegistry().getLoadPath(
dataClass,          // InputStream.class（由 DataFetcher 决定）
resourceClass,      // Object.class（第10步存的）
transcodeClass      // Drawable.class（第5步就确定了）
);
}

完整链路总结
transcodeClass = Drawable.class 的传递路径：

RequestManager.asDrawable()           → Drawable.class
↓
RequestManager.as(Drawable.class)
↓
new RequestBuilder(transcodeClass)    → 存到 RequestBuilder.transcodeClass
↓
SingleRequest.obtain(transcodeClass)  → 存到 SingleRequest.transcodeClass
↓
engine.load(transcodeClass)
↓
DecodeJob.init(transcodeClass)
↓
DecodeHelper.init(transcodeClass)     → 存到 DecodeHelper.transcodeClass


resourceClass = Object.class 的传递路径：

BaseRequestOptions.resourceClass      → 默认值 Object.class
↓
requestOptions.getResourceClass()     → 在 SingleRequest.onSizeReady() 中取出
↓
engine.load(resourceClass)
↓
DecodeJob.init(resourceClass)
↓
DecodeHelper.init(resourceClass)      → 存到 DecodeHelper.resourceClass


如果我设置了可以从本地磁盘读取，这个时候用的线程池是diskCacheExecutor ，后面发现本地没有缓存，要用网络下载，它是在哪里切换到sourceExecutor线程池的，你说详细一点



Searched for and located getNextGenerator method in DecodeJob.java
Searched for and located getNextGenerator method in DecodeJob.java

好，完整的链路找到了。

完整流程：从 diskCacheExecutor 切到 sourceExecutor
假设你用了 DiskCacheStrategy.ALL（先尝试读缓存）：

第一步：EngineJob.start()（主线程）
public synchronized void start(DecodeJob<R> decodeJob) {
GlideExecutor executor =
decodeJob.willDecodeFromCache() ? diskCacheExecutor : getActiveSourceExecutor();
//  true → 选 diskCacheExecutor
executor.execute(decodeJob);
}

willDecodeFromCache() 返回 true，用 diskCacheExecutor 执行 DecodeJob。

第二步：DecodeJob.run() → runWrapped()（diskCacheExecutor 线程）
private void runWrapped() {
switch (runReason) {
case INITIALIZE:
stage = getNextStage(Stage.INITIALIZE);       // → RESOURCE_CACHE
currentGenerator = getNextGenerator();         // → ResourceCacheGenerator
runGenerators();
break;
}
}

第三步：runGenerators()（diskCacheExecutor 线程）
private void runGenerators() {
boolean isStarted = false;
while (!isCancelled
&& currentGenerator != null
&& !(isStarted = currentGenerator.startNext())) {  // 尝试从缓存读

        stage = getNextStage(stage);           // 失败了，进入下一阶段
        currentGenerator = getNextGenerator(); // 获取下一个 Generator

        if (stage == Stage.SOURCE) {           // 到了 SOURCE 阶段！
            reschedule();                      // ← 关键！切换线程池
            return;
        }
    }
}

这个 while 循环的过程：

第1轮：
currentGenerator = ResourceCacheGenerator
startNext() → 找变换后的缓存 → 没有 → 返回 false
stage = getNextStage(RESOURCE_CACHE) → DATA_CACHE
currentGenerator = DataCacheGenerator

第2轮：
currentGenerator = DataCacheGenerator  
startNext() → 找原始数据缓存 → 没有 → 返回 false
stage = getNextStage(DATA_CACHE) → SOURCE
currentGenerator = SourceGenerator

发现 stage == SOURCE → 调 reschedule() → return 退出

第四步：reschedule()（diskCacheExecutor 线程）
public void reschedule() {
runReason = RunReason.SWITCH_TO_SOURCE_SERVICE;   // 标记原因
callback.reschedule(this);                         // callback 是 EngineJob
}

第五步：EngineJob.reschedule()（diskCacheExecutor 线程）
public void reschedule(DecodeJob<?> job) {
getActiveSourceExecutor().execute(job);   // 用 sourceExecutor 重新执行 DecodeJob
}

在这里！把 DecodeJob 从 diskCacheExecutor 切到了 sourceExecutor。

第六步：DecodeJob.run() → runWrapped()（sourceExecutor 线程）
DecodeJob 又被执行了一次，但这次 runReason 变了：

private void runWrapped() {
switch (runReason) {
case SWITCH_TO_SOURCE_SERVICE:            // ← 这次走这个分支
runGenerators();
break;
}
}

此时 currentGenerator 已经是 SourceGenerator，runGenerators() 里调 SourceGenerator.startNext() → 发起网络请求下载图片。

完整时间线
主线程：
engineJob.start(decodeJob)
→ diskCacheExecutor.execute(decodeJob)
────────────────────────────────────────
diskCacheExecutor 线程：
DecodeJob.run()
→ runReason = INITIALIZE
→ ResourceCacheGenerator.startNext() → 没缓存，失败
→ DataCacheGenerator.startNext()     → 没缓存，失败
→ stage = SOURCE
→ reschedule()
→ runReason = SWITCH_TO_SOURCE_SERVICE
→ EngineJob.reschedule(this)
→ sourceExecutor.execute(this)       ← 切到 sourceExecutor
────────────────────────────────────────
sourceExecutor 线程：
DecodeJob.run()
→ runReason = SWITCH_TO_SOURCE_SERVICE
→ SourceGenerator.startNext() → 发起网络下载
→ 下载完成 → 解码 → 变换 → 转码
→ EngineJob.onResourceReady()
→ mainThreadExecutor.execute(...)    ← 切回主线程
────────────────────────────────────────
主线程：
SingleRequest.onResourceReady()
→ ImageView.setImageDrawable()       ✅

所以一共切了三次线程：主线程 → diskCacheExecutor → sourceExecutor → 主线程。

SourceGenerator，ResourceCacheGenerator，DataCacheGenerator 是什么意思



三个 Generator 的含义
它们是三种数据获取器，分别负责从不同的地方获取图片数据：

Generator	从哪里获取数据	获取的是什么
ResourceCacheGenerator	磁盘缓存	变换后的图片（比如已经 centerCrop 裁剪过的）
DataCacheGenerator	磁盘缓存	原始的图片数据（下载下来的原始数据）
SourceGenerator	数据源（网络/文件等）	从源头重新获取
举个实际场景
假设你第一次加载一张网络图片，并用了 centerCrop() + DiskCacheStrategy.ALL：

第一次加载：
ResourceCacheGenerator → 磁盘里有裁剪后的图吗？→ 没有
DataCacheGenerator     → 磁盘里有原始图吗？→ 没有
SourceGenerator        → 从网络下载 → 下载成功
→ 原始数据写入磁盘缓存（DATA_CACHE）
→ 解码 + centerCrop 后的图也写入磁盘缓存（RESOURCE_CACHE）

第二次加载（同一张图片 + 同样 centerCrop）：
ResourceCacheGenerator → 磁盘里有裁剪后的图吗？→ 有！直接用 ✅
（后面两个不用执行了）

第二次加载（同一张图片 + 不同变换，比如换成 circleCrop）：
ResourceCacheGenerator → 磁盘里有圆形裁剪后的图吗？→ 没有（之前缓存的是 centerCrop 的）
DataCacheGenerator     → 磁盘里有原始图吗？→ 有！拿出来重新解码 + circleCrop ✅
（不用重新下载了）

为什么要分两层磁盘缓存？
RESOURCE_CACHE（变换后的缓存）：
优点：直接能用，不需要重新解码和变换，速度最快
缺点：跟变换绑定，换一种变换就用不了

DATA_CACHE（原始数据缓存）：
优点：不管什么变换都能用，因为存的是原始数据
缺点：每次还要重新解码 + 变换，比 RESOURCE_CACHE 慢

它们的执行顺序
ResourceCacheGenerator（最快，找变换后的缓存）
↓ 没有
DataCacheGenerator（较快，找原始数据缓存）
↓ 没有
SourceGenerator（最慢，从网络下载）

先找最快的，找不到再找次快的，都没有才走最慢的网络下载。

打个比方
你想吃一盘西红柿炒蛋：

Generator	比喻	速度
ResourceCacheGenerator	冰箱里有昨天炒好的西红柿炒蛋，热一下就能吃	最快
DataCacheGenerator	冰箱里有西红柿和鸡蛋（原材料），需要重新炒	较快
SourceGenerator	冰箱空的，要去超市买菜再回来炒	最慢























