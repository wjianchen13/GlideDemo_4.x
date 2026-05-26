package com.example.glidedemo_4x.test10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder;

import org.libpag.PAGFile;

public class PAGFileToPAGDataTranscoder10 implements ResourceTranscoder<PAGFile, PAGData10> {

    @Nullable
    @Override
    public Resource<PAGData10> transcode(@NonNull Resource<PAGFile> toTranscode, @NonNull Options options) {
        return new PAGDataResource10(toTranscode);
    }
}
