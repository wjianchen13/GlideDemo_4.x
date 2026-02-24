package com.example.glidedemo_4x.test5;

import androidx.annotation.NonNull;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.GlideUrl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 自定义 DataFetcher，通过 HTTP 下载 PAG 文件，返回 InputStream
 * 参考 Base64DataFetcher 的实现方式
 */
public class PAGDataFetcher implements DataFetcher<InputStream> {

    private final GlideUrl model;
    private HttpURLConnection connection;
    private InputStream stream;
    private volatile boolean isCancelled;

    PAGDataFetcher(GlideUrl model) {
        this.model = model;
    }

    @Override
    public void loadData(@NonNull Priority priority, @NonNull DataCallback<? super InputStream> callback) {
        try {
            if (isCancelled) {
                return;
            }
            URL url = new URL(model.toStringUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(15000);
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                stream = connection.getInputStream();
                callback.onDataReady(stream);
            } else {
                callback.onLoadFailed(new IOException("HTTP request failed, response code: " + responseCode));
            }
        } catch (IOException e) {
            callback.onLoadFailed(e);
        }
    }

    @Override
    public void cleanup() {
        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            // Ignored
        }
        if (connection != null) {
            connection.disconnect();
        }
    }

    @Override
    public void cancel() {
        isCancelled = true;
    }

    @NonNull
    @Override
    public Class<InputStream> getDataClass() {
        return InputStream.class;
    }

    @NonNull
    @Override
    public DataSource getDataSource() {
        return DataSource.REMOTE;
    }
}
