package com.example.glidedemo_4x;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.example.glidedemo_4x.test1.Base64ModelLoaderFactory;

import java.nio.ByteBuffer;

@GlideModule
public class MyAppGlideModule extends AppGlideModule {
  @Override
  public void registerComponents(Context context, Glide glide, Registry registry) {
    registry.prepend(String.class, ByteBuffer.class, new Base64ModelLoaderFactory());
  }
}
