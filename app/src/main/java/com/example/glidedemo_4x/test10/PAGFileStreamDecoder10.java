package com.example.glidedemo_4x.test10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;

import org.libpag.PAGFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PAGFileStreamDecoder10 implements ResourceDecoder<InputStream, PAGFile> {

    private static final byte[] PAG_MAGIC = {0x50, 0x41, 0x47};

    @Override
    public boolean handles(@NonNull InputStream source, @NonNull Options options) throws IOException {
        byte[] header = new byte[PAG_MAGIC.length];
        int read = source.read(header);
        if(read < PAG_MAGIC.length) {
            return false;
        }
        for(int i = 0; i < PAG_MAGIC.length; i ++) {
            if(header[i] != PAG_MAGIC[i]) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    @Override
    public Resource<PAGFile> decode(@NonNull InputStream source, int width, int height, @NonNull Options options) throws IOException {
        byte[] bytes = inputStreamToBytes(source);
        if(bytes == null) {
            throw new IOException("Failed to read InputStream");
        }
        PAGFile pagFile = PAGFile.Load(bytes);
        if(pagFile == null) {
            throw new IOException("Failed to decode PAG File");
        }
        return new PAGFileResource10(pagFile, bytes.length);
    }

    private byte[] inputStreamToBytes(@NonNull InputStream is) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] temp = new byte[8192];
        int len;
        while((len = is.read(temp)) != -1) {
            buffer.write(temp, 0, len);
        }
        return buffer.toByteArray();
    }

}





















