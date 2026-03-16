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
        PAGFile pagFile = toTranscode.get();
        int size = toTranscode.getSize();

        PagData pagData = new PagData();
        pagData.pagFile = pagFile;

        return new PagDataResource(pagData, size);
    }
}
