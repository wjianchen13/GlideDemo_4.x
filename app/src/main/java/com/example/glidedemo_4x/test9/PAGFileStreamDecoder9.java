package com.example.glidedemo_4x.test9;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.example.glidedemo_4x.PAGFileResource;

import org.libpag.PAGFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Glide 自定义解码器，将 InputStream 解码为 PAGFile
 * 与 test4 的 PAGFileResourceDecoder 区别：输入类型从 ByteBuffer 改为 InputStream
 * 不需要自定义 ModelLoader，使用 Glide 内置的网络加载链
 */
public class PAGFileStreamDecoder9 implements ResourceDecoder<InputStream, PAGFile> {

    // PAG 文件头魔数
    private static final byte[] PAG_MAGIC = {0x50, 0x41, 0x47}; // "PAG"

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        // 检查文件头是否为 PAG 格式
        byte[] header = new byte[PAG_MAGIC.length];
        int read = source.read(header);
        if (read < PAG_MAGIC.length) {
            return false;
        }
        for (int i = 0; i < PAG_MAGIC.length; i++) {
            if (header[i] != PAG_MAGIC[i]) {
                return false;
            }
        }
        return true;
        // 注意：这里读了头部字节，Glide 会在调用 decode() 之前通过 DataRewinder.rewindAndGet() 重置流位置
    }

    @Nullable
    @Override
    public Resource<PAGFile> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        // 从 InputStream 中读取全部字节
        byte[] bytes = inputStreamToBytes(source);
        if (bytes == null) {
            throw new IOException("Failed to read InputStream");
        }

        // 使用 PAGFile.Load(byte[]) 解码
        PAGFile pagFile = PAGFile.Load(bytes);
        if (pagFile == null) {
            throw new IOException("Failed to decode PAG file");
        }
        return new PAGFileResource(pagFile, bytes.length);
    }

    private byte[] inputStreamToBytes(@NonNull InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] temp = new byte[8192];
        int len;
        while ((len = is.read(temp)) != -1) {
            buffer.write(temp, 0, len);
        }
        return buffer.toByteArray();
    }
}
