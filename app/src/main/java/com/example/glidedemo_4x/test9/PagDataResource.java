package com.example.glidedemo_4x.test9;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.Resource;

import org.libpag.PAGFile;

/**
 * PagData 的 Resource 包装类
 * 持有原始 Resource<PAGFile> 的引用，recycle() 时委托给原始 Resource
 * 参考 Glide 内置的 LazyBitmapDrawableResource 的设计
 */
public class PagDataResource implements Resource<PagData> {

    private final Resource<PAGFile> originalResource;
    private final PagData pagData;

    public PagDataResource(@NonNull Resource<PAGFile> originalResource) {
        this.originalResource = originalResource;
        this.pagData = new PagData();
        this.pagData.pagFile = originalResource.get();
    }

    @NonNull
    @Override
    public Class<PagData> getResourceClass() {
        return PagData.class;
    }

    @NonNull
    @Override
    public PagData get() {
        return pagData;
    }

    @Override
    public int getSize() {
        return originalResource.getSize();
    }

    @Override
    public void recycle() {
        // 委托给原始 Resource，由 PAGFileResource.recycle() 负责释放 PAGFile
        originalResource.recycle();
    }
}
