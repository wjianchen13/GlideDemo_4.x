package com.example.glidedemo_4x.test10;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.Resource;

import org.libpag.PAGFile;

public class PAGDataResource10 implements Resource<PAGData10> {

    private final Resource<PAGFile> originalResource;
    private final PAGData10 pagData;

    public PAGDataResource10(Resource<PAGFile> originalResource) {
        this.originalResource = originalResource;
        this.pagData = new PAGData10();
        this.pagData.pagFile = originalResource.get();
    }

    @NonNull
    @Override
    public Class<PAGData10> getResourceClass() {
        return PAGData10.class;
    }

    @NonNull
    @Override
    public PAGData10 get() {
        return pagData;
    }

    @Override
    public int getSize() {
        return originalResource.getSize();
    }

    @Override
    public void recycle() {
        originalResource.recycle();
    }
}
