package com.example.glidedemo_4x.test5;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

import java.io.InputStream;

/**
 * PAGModelLoader 的工厂类
 * 参考 Base64ModelLoaderFactory 的实现方式
 */
public class PAGModelLoaderFactory implements ModelLoaderFactory<GlideUrl, InputStream> {

    @Override
    public ModelLoader<GlideUrl, InputStream> build(MultiModelLoaderFactory multiFactory) {
        return new PAGModelLoader();
    }

    @Override
    public void teardown() {
        // Do nothing.
    }
}
