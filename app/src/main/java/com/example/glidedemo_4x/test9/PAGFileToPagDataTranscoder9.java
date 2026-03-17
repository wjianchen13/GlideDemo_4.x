package com.example.glidedemo_4x.test9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

import org.libpag.PAGFile;

/**
 * 转码器：PAGFile → PagData
 * 类似 BitmapDrawableTranscoder 把 Bitmap 转成 BitmapDrawable
 */
public class PAGFileToPagDataTranscoder9 implements ResourceTranscoder<PAGFile, PagData> {

    @Nullable
    @Override
    public Resource<PagData> transcode(@NonNull Resource<PAGFile> toTranscode, @NonNull Options options) {
        // 把原始 Resource 传进去，由 PagDataResource 负责管理生命周期
        return new PagDataResource(toTranscode);
    }
}
