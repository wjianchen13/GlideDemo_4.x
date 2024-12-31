
ModelLoaderRegistry 主要介绍
MultiModelLoaderFactory 

数据结构
MultiModelLoaderFactory内部类Entry如下
private static class Entry<Model, Data> {
private final Class<Model> modelClass; // 模型类
@Synthetic final Class<Data> dataClass; // 目标数据类
@Synthetic final ModelLoaderFactory<? extends Model, ? extends Data> factory; // 对应的生成ModelLoader工厂类

这里面存储的内容如下：
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
实际上就是所有model，dataClass，ModelLoaderFactory都注册到这里了

ResourceDecoderRegistry 介绍
ResourceDecoderRegistry里面的decoders是在Glide里面注册的
它通过下面方法注册
public <Data, TResource> Registry append(@NonNull String bucket,
@NonNull Class<Data> dataClass, @NonNull Class<TResource> resourceClass,
@NonNull ResourceDecoder<Data, TResource> decoder) { 
    decoderRegistry.append(bucket, decoder, dataClass, resourceClass);
    return this;
}
各参数的意思是：
数据输入是dataClass经过decoder之后，转成了resourceClass类型的东西

decoderRegistry主要的内容包括：
Glide里面注册的顺序
registry.append(Registry.BUCKET_ANIMATION, InputStream.class, Drawable.class, AnimatedWebpDecoder.streamDecoder(imageHeaderParsers, arrayPool)); L434
registry.append(Registry.BUCKET_ANIMATION, ByteBuffer.class, Drawable.class, AnimatedWebpDecoder.byteBufferDecoder(imageHeaderParsers, arrayPool)); L439
.append(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder) L465
.append(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder); L466
registry.append(Registry.BUCKET_BITMAP, ParcelFileDescriptor.class, Bitmap.class, new ParcelFileDescriptorBitmapDecoder(downsampler)); L469
.append(Registry.BUCKET_BITMAP, ParcelFileDescriptor.class, Bitmap.class, parcelFileDescriptorVideoDecoder) L477
.append(Registry.BUCKET_BITMAP, AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(bitmapPool)) L482
.append(Registry.BUCKET_BITMAP, Bitmap.class, Bitmap.class, new UnitBitmapDecoder()) L488
.append(Registry.BUCKET_BITMAP_DRAWABLE, ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, byteBufferBitmapDecoder)) L491
.append(Registry.BUCKET_BITMAP_DRAWABLE, InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, streamBitmapDecoder)) L496
.append(Registry.BUCKET_BITMAP_DRAWABLE, ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, parcelFileDescriptorVideoDecoder)) L501
.append(Registry.BUCKET_ANIMATION, InputStream.class, GifDrawable.class, new StreamGifDecoder(imageHeaderParsers, byteBufferGifDecoder, arrayPool)) L508
.append(Registry.BUCKET_ANIMATION, ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder) L513
.append(Registry.BUCKET_BITMAP, GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool)) L520
.append(Uri.class, Drawable.class, resourceDrawableDecoder) L526
.append(Uri.class, Bitmap.class, new ResourceBitmapDecoder(resourceDrawableDecoder, bitmapPool)) L527
.append(File.class, File.class, new FileDecoder()) L533
.append(Drawable.class, Drawable.class, new UnitDrawableDecoder()) L592
registry.append(ByteBuffer.class, Bitmap.class, byteBufferVideoDecoder); L606
registry.append(ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, byteBufferVideoDecoder)); L607
上面第一第二个由于不满足Glide第433行的条件experiments.isEnabled(EnableImageDecoderForAnimatedWebp.class)，所以是没有添加进去的。

bucketPriorityList的值有以下这些：
0="legacy_prepend_all"
1="Animation"
2="Bitmap"
3="BitmapDrawable"
4="legacy_append"
decoders的值有以下这些：
Bitmap -> {ArrayList@26682}  size = 7
    0={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.nio.ByteBuffer"
        decoder={ByteBufferBitmapDecoder}
        resourceClass="class android.graphics.Bitmap"
    1={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.io.InputStream"
        decoder={StreamBitmapDecoder}
        resourceClass="class android.graphics.Bitmap"
    2={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.os.ParcelFileDescriptor"
        decoder={ParcelFileDescriptorBitmapDecoder}
        resourceClass="class android.graphics.Bitmap"
    3={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.os.ParcelFileDescriptor"
        decoder={VideoDecoder}
        resourceClass="class android.graphics.Bitmap"
    4={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.content.res.AssetFileDescriptor"
        decoder={VideoDecoder}
        resourceClass="class android.graphics.Bitmap"
    5={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.graphics.Bitmap"
        decoder={UnitBitmapDecoder}
        resourceClass="class android.graphics.Bitmap"
    6={ResourceDecoderRegistry$Entry@26696}
        dataClass="interface com.bumptech.glide.gifdecoder.GifDecoder"
        decoder={GifFrameResourceDecoder}
        resourceClass="class android.graphics.Bitmap"

Animation -> {ArrayList@26683}  size = 2
    0={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.io.InputStream"
        decoder={StreamGifDecoder}
        resourceClass="class com.bumptech.glide.load.resource.gif.GifDrawable"
    1={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.nio.ByteBuffer"
        decoder={ByteBufferGifDecoder}
        resourceClass="class com.bumptech.glide.load.resource.gif.GifDrawable"

BitmapDrawable -> {ArrayList@26608}  size = 3
    0={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.nio.ByteBuffer"
        decoder={BitmapDrawableDecoder}
        resourceClass="class android.graphics.drawable.BitmapDrawable"
    1={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.io.InputStream"
        decoder={BitmapDrawableDecoder}
        resourceClass="class android.graphics.drawable.BitmapDrawable"
    2={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.os.ParcelFileDescriptor"
        decoder={BitmapDrawableDecoder}
        resourceClass="class android.graphics.drawable.BitmapDrawable"

legacy_append -> {ArrayList@26610}  size = 6
    0={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.net.Uri"
        decoder={ResourceDrawableDecoder}
        resourceClass="class android.graphics.drawable.Drawable"
    1={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.net.Uri"
        decoder={ResourceBitmapDecoder}
        resourceClass="class android.graphics.Bitmap"
    2={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.io.File"
        decoder={FileDecoder}
        resourceClass="class java.io.File"
    3={ResourceDecoderRegistry$Entry@26696}
        dataClass="class android.graphics.drawable.Drawable"
        decoder={UnitDrawableDecoder}
        resourceClass="class android.graphics.drawable.Drawable"
    4={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.nio.ByteBuffer"
        decoder={VideoDecoder}
        resourceClass="class android.graphics.Bitmap"
    5={ResourceDecoderRegistry$Entry@26696}
        dataClass="class java.nio.ByteBuffer"
        decoder={BitmapDrawableDecoder}
        resourceClass="class android.graphics.drawable.BitmapDrawable"

TranscoderRegistry 介绍
在Glide第594行进行注册
.register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources))
.register(Bitmap.class, byte[].class, bitmapBytesTranscoder)
.register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder))
.register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);

TranscoderRegistry 内部类Entry
private static final class Entry<Z, R> {
@Synthetic final Class<Z> fromClass;
@Synthetic final Class<R> toClass;
@Synthetic final ResourceTranscoder<Z, R> transcoder;

transcoders 值如下：
0={TranscoderRegistry$Entry@26593}
    fromClass="class android.graphics.Bitmap"
    toClass="class android.graphics.drawable.BitmapDrawable"
    transcoder={BitmapDrawableTranscoder@26598}
0={TranscoderRegistry$Entry@26593}
    fromClass="class android.graphics.Bitmap"
    toClass="class [B"
    transcoder={BitmapBytesTranscoder@26598}
0={TranscoderRegistry$Entry@26593}
    fromClass="class android.graphics.drawable.Drawable"
    toClass="class [B"
    transcoder={DrawableBytesTranscoder@26598}
0={TranscoderRegistry$Entry@26593}
    fromClass="class com.bumptech.glide.load.resource.gif.GifDrawable"
    toClass="class [B"
    transcoder={GiftDrawableByteTranscoder@26598}




































