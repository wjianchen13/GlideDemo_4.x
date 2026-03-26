package com.example.glidedemo_4x.test9;

import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.Resource;

import org.libpag.PAGFile;

/**
 * PAGFile 的 Resource 包装类
 * PAGFile 没有显式的 release() 方法，原生资源通过 finalize() 在 GC 时自动释放
 * 这里 recycle() 断开引用，让 GC 可以尽早回收
 */
public class PAGFileResource implements Resource<PAGFile> {
    private PAGFile pagFile;
    private final int size;

    public PAGFileResource(@NonNull PAGFile pagFile, int size) {
        this.pagFile = pagFile;
        this.size = size;
    }

    @NonNull
    @Override
    public Class<PAGFile> getResourceClass() {
        return PAGFile.class;
    }

    @NonNull
    @Override
    public PAGFile get() {
        return pagFile;
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public void recycle() {
        // PAGFile 没有显式释放方法，原生资源通过 finalize() 自动释放
        // 断开引用，让 GC 可以尽早回收 PAGFile 及其原生资源
        pagFile = null;
    }
}
