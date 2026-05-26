package com.example.glidedemo_4x.test10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.example.glidedemo_4x.test9.PAGFileResource;

import org.libpag.PAGFile;

import java.io.IOException;
import java.nio.ByteBuffer;

public class PAGFileBufferDecoder10 implements ResourceDecoder<ByteBuffer, PAGFile> {

    private static final byte[] PAG_MAGIC = {0x50, 0x41, 0x47};

    @Override
    public boolean handles(@NonNull ByteBuffer source, @NonNull Options options) throws IOException {
        if(source.remaining() < PAG_MAGIC.length) {
            return false;
        }
        int position = source.position();
        for(byte magicByte : PAG_MAGIC) {
            if(source.get() != magicByte) {
                source.position(position);
                return false;
            }
        }
        source.position(position);
        return true;
    }

    @Nullable
    @Override
    public Resource<PAGFile> decode(@NonNull ByteBuffer source, int width, int height, @NonNull Options options) throws IOException {
        byte[] bytes = new byte[source.remaining()];
        source.get(bytes);
        PAGFile pagFile = PAGFile.Load(bytes);
        if(pagFile == null) {
            throw new IOException("Failed to decode PAG file");
        }

        return new PAGFileResource(pagFile, bytes.length);
    }

}
