package com.example.glidedemo_4x.test5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;

import org.libpag.PAGFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义解码器，将 InputStream 解码为 PAGFile
 * 配合 PAGModelLoader 使用
 */
public class PAGFileStreamDecoder implements ResourceDecoder<InputStream, PAGFile> {

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        return true;
    }

    @Nullable
    @Override
    public Resource<PAGFile> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        // 将 InputStream 读取为 byte[]
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[8192];
        int len;
        while ((len = source.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        byte[] bytes = baos.toByteArray();

        // 使用 PAGFile.Load(byte[]) 解码
        PAGFile pagFile = PAGFile.Load(bytes);
        if (pagFile == null) {
            throw new IOException("Failed to decode PAG file");
        }
        return new SimpleResource<>(pagFile);
    }
}
