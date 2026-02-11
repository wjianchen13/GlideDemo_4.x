package com.example.glidedemo_4x.test4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.SimpleResource;

import org.libpag.PAGFile;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Glide 自定义解码器，将 ByteBuffer 解码为 PAGFile
 * PAG 文件头魔数为 "PAG"（0x50 0x41 0x47）
 */
public class PAGFileResourceDecoder implements ResourceDecoder<ByteBuffer, PAGFile> {

    // PAG 文件头魔数
    private static final byte[] PAG_MAGIC = {0x50, 0x41, 0x47}; // "PAG"

    @Override
    public boolean handles(@NonNull ByteBuffer source, @NonNull Options options) throws IOException {
        // 检查文件头是否为 PAG 格式，避免影响其他类型文件的解码
        if (source.remaining() < PAG_MAGIC.length) {
            return false;
        }
        int position = source.position();
        for (byte magicByte : PAG_MAGIC) {
            if (source.get() != magicByte) {
                source.position(position); // 恢复位置
                return false;
            }
        }
        source.position(position); // 恢复位置
        return true;
    }

    @Nullable
    @Override
    public Resource<PAGFile> decode(@NonNull ByteBuffer source, int width, int height, @NonNull Options options) throws IOException {
        // 从 ByteBuffer 中提取 byte[]
        byte[] bytes = new byte[source.remaining()];
        source.get(bytes);

        // 使用 PAGFile.Load(byte[]) 解码，不走 PAG 内部路径缓存，避免与 Glide 缓存重复
        PAGFile pagFile = PAGFile.Load(bytes);
        if (pagFile == null) {
            throw new IOException("Failed to decode PAG file");
        }
        return new SimpleResource<>(pagFile);
    }
}
