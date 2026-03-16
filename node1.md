

完整数据流
网络URL (Model)
↓ ① ModelLoaderRegistry        上游
原始数据流 (Data: InputStream)
↓ ② DataRewinderRegistry       中间件
↓ ③ DecoderRegistry            中间
解码后资源 (Resource: Bitmap)
↓ ④ TranscoderRegistry         下游
最终资源 (Transcode: BitmapDrawable)
↓ 显示到 ImageView

⑤ EncoderRegistry              旁路（缓存写入原始数据）
⑥ ResourceEncoderRegistry      旁路（缓存写入解码后资源）

逐个解释
#	Registry	作用	例子
①	ModelLoaderRegistry	Model → Data，把"要加载什么"变成"原始数据"	URL → InputStream
②	DataRewinderRegistry	给 Data 提供回退能力，不改变数据类型	InputStream 可以 rewind 回起点
③	DecoderRegistry	Data → Resource，把原始数据解码成资源	InputStream → Bitmap
④	TranscoderRegistry	Resource → Transcode，把资源转成最终类型	Bitmap → BitmapDrawable
⑤	EncoderRegistry	把原始数据写入磁盘缓存	InputStream → 缓存文件 (DataCache)
⑥	ResourceEncoderRegistry	把解码后的资源写入磁盘缓存	Bitmap → 缓存文件 (ResourceCache)
上下游关系
上游（获取数据）
① ModelLoaderRegistry:       URL → InputStream

中间件（辅助）
② DataRewinderRegistry:      让 InputStream 可回退（handles()读头部后要倒回去）

核心处理（解码 + 转码）
③ DecoderRegistry:           InputStream → Bitmap
④ TranscoderRegistry:        Bitmap → BitmapDrawable

旁路（缓存写入，不在主链路上，而是在主链路的"旁边"）
⑤ EncoderRegistry:           InputStream → 磁盘文件  (对应 DataCacheKey)
⑥ ResourceEncoderRegistry:   Bitmap → 磁盘文件       (对应 ResourceCacheKey)

ModelLoader获取的过程：
SourceGenerator第82行，hasNextModelLoader()方法，最终会调用到ModelLoaderRegistry第75行调用第110行的
getModelLoadersForClass()方法
最后MultiModelLoaderFactory第91行最后返回的数据结构如下
List<ModelLoader<Model, ?>>
[0]=DataUrlLoader<String, InputStream>
[1]=StringLoader<String, InputStream>
    ModelLoader<Uri.class, InputStream.class> uriLoader = MultiModelLoader<Uri.class, InputStream.class>
        List<Uri, InputStream.class>
        [0]=DataUrlLoader<Uri, InputStream.class>
        [1]=AssetUriLoader<Uri, InputStream.class>
        [2]=MediaStoreImageThumbLoader<Uri, InputStream.class>
        [3]=MediaStoreVideoThumbLoader<Uri, InputStream.class>
        [4]=QMediaStoreUriLoader<Uri, InputStream.class>
        [5]=UriLoader<Uri, InputStream.class>
        [6]=UrlUriLoader<Uri, InputStream.class>
            urlLoader=HttpGlideUrlLoader<GlideUrl, InputStream>
[2]=StringLoader<String, ParcelFileDescriptor>
    uriLoader = MultiModelLoader<String, InputStream>->List
        [0]=QMediaStoreUriLoader<Uri, ParcelFileDescriptor>
        [1]=UriLoader<Uri, ParcelFileDescriptor>
[3]=StringLoader<String, AssetFileDescriptor>
    uriLoader = MultiModelLoader<String, InputStream>->List
        [0]=AssetUriLoader<Uri, AssetFileDescriptor>
        [1]=UriLoader<Uri, AssetFileDescriptor>


然后回到Registry第475 getLoadPath()方法，返回的result=null，会去新建一个LoadPath
new LoadPath<>(java.io.InputStream, java.lang.Object, android.graphics.drawable.Drawable, decodePaths, throwableListPool);
接下来进入getDecodePaths()方法：
传入的参数是：
dataClass = java.io.InputStream,
resourceClass = java.lang.Object,
transcodeClass = android.graphics.drawable.Drawable
在Registry第497行代码中，返回的LoadPath对象是：
LoadPath{
    dataClass=class java.io.InputStream
    decodePaths=[
        [0]=DecodePath{ 
            dataClass=class java.io.InputStream, 
            decoders=[
                com.bumptech.glide.load.resource.gif.StreamGifDecoder@5d4a655
            ], 
            transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@f6e86a
        },
        [1]=DecodePath{ 
            dataClass=class java.io.InputStream, 
            decoders=[
                com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@fed715b
            ], 
            transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@8052df8
        }, 
        [2]=DecodePath{ 
            dataClass=class java.io.InputStream, 
            decoders=[
                com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@40652d1
            ], 
            transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@f6e86a
        }
    ]
}

获取LoadPath的具体代码在Registry第501行的getDecodePaths()方法
在这里，首先获取已经注册的resourceClass，传入参数是(class java.io.InputStream, class java.lang.Object)
把已经注册在decoderRegistry里面的dataClass满足：和InputStream相等，或者是InputStream父类
并且resourceClass满足：和Object相等，或者是Object子类的所有记录找出来
然后把这些记录都添加到一个列表中，并且要去重，这个列表就是registeredResourceClasses
registeredResourceClasses List<Class<TResource>>
[0]=class com.bumptech.glide.load.resource.gif.GifDrawable
[1]=class android.graphics.Bitmap
[2]=class android.graphics.drawable.BitmapDrawable

分别从registeredResourceClasses取出resourceClass和传入的transcodeClass作为TranscoderRegistry.getTranscodeClasses()
方法的参数，
这里面实现的逻辑，如果传入的resourceClass是的transcodeClass子类，或者传入的transcodeClass和resourceClass
相等，就把传入的transcodeClass放到列表中，并返回该列表
这里说一下为什么传入的transcodeClass是resourceClass的子类，就直接返回List(transcodeClass),因为后面重新获取
transcoder的时候，也是会向下兼容，就是说如果是Drawable，就判断如果是Drawable的子类，就返回对应的transcoder
后面获取transcoder，找到第一个满足条件的transcoder就返回了，后面如果还有就不管了，因为transcoder可以共用，
它只是把共同的resourceClass类型转成对应的transcodeClass类型。

当
registeredResourceClass = [0]=class com.bumptech.glide.load.resource.gif.GifDrawable
返回registeredTranscodeClasses -> List<Class<Transcode>>
[0]=class android.graphics.drawable.Drawable
调用getDecoders(class java.io.InputStream, class com.bumptech.glide.load.resource.gif.GifDrawable)
返回decoders=
[0]=StreamGifDecoder
transcoder=UnitTranscoder
所以下面的decodePaths是：
decodePaths -> List<DecodePath<Data, TResource, Transcode>>
[0]=DecodePath{ 
        dataClass=class java.io.InputStream, 
        decoders=[
            com.bumptech.glide.load.resource.gif.StreamGifDecoder@8bf4912
        ], 
        transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@7a1c8e3
    }

当registeredResourceClass = [1]=class android.graphics.Bitmap
返回registeredTranscodeClasses -> List<Class<Transcode>>
[0]=class android.graphics.drawable.BitmapDrawable
调用getDecoders(class java.io.InputStream, class android.graphics.drawable.BitmapDrawable)
返回decoders=
[0]=StreamBitmapDecoder
transcoder=BitmapDrawableDecoder
所以下面的decodePaths是：
decodePaths -> List<DecodePath<Data, TResource, Transcode>>
[0]=DecodePath{
    dataClass=class java.io.InputStream,
    decoders=[
        com.bumptech.glide.load.resource.gif.StreamGifDecoder@8bf4912
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@7a1c8e3
}
[1]=DecodePath{ 
    dataClass=class java.io.InputStream, 
    decoders=[
        com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@7a1c8e3
    ], 
    transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@da9e2e0
}

当registeredResourceClass = [2]=class android.graphics.drawable.BitmapDrawable
返回registeredTranscodeClasses -> List<Class<Transcode>>
[0]=class android.graphics.drawable.Drawable
调用getDecoders(class java.io.InputStream, class android.graphics.drawable.Drawable)
返回decoders=
[0]=BitmapDrawableDecoder
transcoder=UnitTranscoder
所以下面的decodePaths是：
decodePaths -> List<DecodePath<Data, TResource, Transcode>>
    [0]=DecodePath {
    dataClass=class java.io.InputStream,
    decoders=[
        com.bumptech.glide.load.resource.gif.StreamGifDecoder@8bf4912
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@7a1c8e3
}
[1]=DecodePath{
    dataClass=class java.io.InputStream,
    decoders=[
        com.bumptech.glide.load.resource.bitmap.StreamBitmapDecoder@7a1c8e3
    ],
    transcoder=com.bumptech.glide.load.resource.transcode.BitmapDrawableTranscoder@da9e2e0
}
[2]=DecodePath{ 
    dataClass=class java.io.InputStream, 
    decoders=[
        com.bumptech.glide.load.resource.bitmap.BitmapDrawableDecoder@db60999
    ], 
    transcoder=com.bumptech.glide.load.resource.transcode.UnitTranscoder@8bf4912
}



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
扫描的时候根据下面顺序扫描：
legacy_prepend_all
Animation
Bitmap
BitmapDrawable
legacy_append

registry.append(Registry.BUCKET_ANIMATION, InputStream.class, GifDrawable.class, new StreamGifDecoder(imageHeaderParsers, byteBufferGifDecoder, arrayPool))
registry.append(Registry.BUCKET_ANIMATION, ByteBuffer.class, GifDrawable.class, byteBufferGifDecoder)

registry.append(Registry.BUCKET_BITMAP, ByteBuffer.class, Bitmap.class, byteBufferBitmapDecoder)
registry.append(Registry.BUCKET_BITMAP, InputStream.class, Bitmap.class, streamBitmapDecoder);
registry.append(Registry.BUCKET_BITMAP, ParcelFileDescriptor.class, Bitmap.class, new ParcelFileDescriptorBitmapDecoder(downsampler));
registry.append(Registry.BUCKET_BITMAP, ParcelFileDescriptor.class, Bitmap.class, parcelFileDescriptorVideoDecoder)
registry.append(Registry.BUCKET_BITMAP, AssetFileDescriptor.class, Bitmap.class, VideoDecoder.asset(bitmapPool))
registry.append(Registry.BUCKET_BITMAP, Bitmap.class, Bitmap.class, new UnitBitmapDecoder())
registry.append(Registry.BUCKET_BITMAP, GifDecoder.class, Bitmap.class, new GifFrameResourceDecoder(bitmapPool))

registry.append(Registry.BUCKET_BITMAP_DRAWABLE, ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, byteBufferBitmapDecoder))
registry.append(Registry.BUCKET_BITMAP_DRAWABLE, InputStream.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, streamBitmapDecoder))
registry.append(Registry.BUCKET_BITMAP_DRAWABLE, ParcelFileDescriptor.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, parcelFileDescriptorVideoDecoder))

registry.append(BUCKET_APPEND_ALL,Uri.class, Drawable.class, resourceDrawableDecoder)
registry.append(BUCKET_APPEND_ALL,Uri.class, Bitmap.class, new ResourceBitmapDecoder(resourceDrawableDecoder, bitmapPool))
registry.append(BUCKET_APPEND_ALL,File.class, File.class, new FileDecoder())
registry.append(BUCKET_APPEND_ALL,Drawable.class, Drawable.class, new UnitDrawableDecoder())
registry.append(BUCKET_APPEND_ALL,ByteBuffer.class, Bitmap.class, byteBufferVideoDecoder);
registry.append(BUCKET_APPEND_ALL,ByteBuffer.class, BitmapDrawable.class, new BitmapDrawableDecoder<>(resources, byteBufferVideoDecoder));





transcoderRegistry保存的数据
.register(Bitmap.class, BitmapDrawable.class, new BitmapDrawableTranscoder(resources))
.register(Bitmap.class, byte[].class, bitmapBytesTranscoder)
.register(Drawable.class, byte[].class, new DrawableBytesTranscoder(bitmapPool, bitmapBytesTranscoder, gifDrawableBytesTranscoder))
.register(GifDrawable.class, byte[].class, gifDrawableBytesTranscoder);



完整数据流
网络URL (Model)
↓ ① ModelLoaderRegistry        上游
原始数据流 (Data: InputStream)
↓ ② DataRewinderRegistry       中间件
↓ ③ DecoderRegistry            中间
解码后资源 (Resource: Bitmap)
↓ ④ TranscoderRegistry         下游
最终资源 (Transcode: BitmapDrawable)
↓ 显示到 ImageView

⑤ EncoderRegistry              旁路（缓存写入原始数据）
⑥ ResourceEncoderRegistry      旁路（缓存写入解码后资源）

逐个解释
#	Registry	作用	例子
①	ModelLoaderRegistry	Model → Data，把"要加载什么"变成"原始数据"	URL → InputStream
②	DataRewinderRegistry	给 Data 提供回退能力，不改变数据类型	InputStream 可以 rewind 回起点
③	DecoderRegistry	Data → Resource，把原始数据解码成资源	InputStream → Bitmap
④	TranscoderRegistry	Resource → Transcode，把资源转成最终类型	Bitmap → BitmapDrawable
⑤	EncoderRegistry	把原始数据写入磁盘缓存	InputStream → 缓存文件 (DataCache)
⑥	ResourceEncoderRegistry	把解码后的资源写入磁盘缓存	Bitmap → 缓存文件 (ResourceCache)





































