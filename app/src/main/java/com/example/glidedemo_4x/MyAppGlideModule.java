package com.example.glidedemo_4x;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.load.model.GlideUrl;
import com.example.glidedemo_4x.test2.Base64ModelLoaderFactory;
import com.example.glidedemo_4x.test4.PAGFileResourceDecoder;
import com.example.glidedemo_4x.test5.PAGFileStreamDecoder;
import com.example.glidedemo_4x.test5.PAGModelLoaderFactory;

import org.libpag.PAGFile;

import java.io.InputStream;
import java.nio.ByteBuffer;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
  @Override
  public void registerComponents(Context context, Glide glide, Registry registry) {
    registry.prepend(String.class, ByteBuffer.class, new Base64ModelLoaderFactory());
    // test4: 注册 PAGFile 解码器（ResourceDecoder 方式）：ByteBuffer -> PAGFile
    registry.prepend(ByteBuffer.class, PAGFile.class, new PAGFileResourceDecoder());
    // test5: 注册 PAG ModelLoader（ModelLoader 方式）：GlideUrl -> InputStream
    registry.prepend(GlideUrl.class, InputStream.class, new PAGModelLoaderFactory());
    // test5: 注册 PAGFile 解码器：InputStream -> PAGFile
    registry.prepend(InputStream.class, PAGFile.class, new PAGFileStreamDecoder());
  }
}
