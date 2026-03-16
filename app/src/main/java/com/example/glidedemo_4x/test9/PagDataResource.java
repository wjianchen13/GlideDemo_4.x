package com.example.glidedemo_4x.test9;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.Resource;

/**
 * PagData 的 Resource 包装类
 */
public class PagDataResource implements Resource<PagData> {

    private PagData pagData;
    private final int size;

    public PagDataResource(@NonNull PagData pagData, int size) {
        this.pagData = pagData;
        this.size = size;
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
        return size;
    }

    @Override
    public void recycle() {
        if (pagData != null) {
            pagData.pagFile = null;
            pagData = null;
        }
    }
}
