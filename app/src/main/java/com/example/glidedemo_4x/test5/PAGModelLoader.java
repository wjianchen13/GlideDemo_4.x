package com.example.glidedemo_4x.test5;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.signature.ObjectKey;

import java.io.InputStream;

/**
 * 自定义 ModelLoader，拦截 .pag 结尾的 URL，交给 PAGDataFetcher 下载
 * 参考 Base64ModelLoader 的实现方式
 */
public final class PAGModelLoader implements ModelLoader<GlideUrl, InputStream> {

    @Nullable
    @Override
    public LoadData<InputStream> buildLoadData(GlideUrl model, int width, int height, Options options) {
        return new LoadData<>(new ObjectKey(model), new PAGDataFetcher(model));
    }

    @Override
    public boolean handles(GlideUrl model) {
        // 去掉查询参数后，判断 URL 是否以 .pag 结尾
        String url = model.toStringUrl();
        int queryIndex = url.indexOf('?');
        String path = queryIndex > 0 ? url.substring(0, queryIndex) : url;
        return path.endsWith(".pag");
    }
}
