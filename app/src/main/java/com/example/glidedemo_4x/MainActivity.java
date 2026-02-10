package com.example.glidedemo_4x;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.request.target.Target;
import com.example.glidedemo_4x.test1.TestActivity1;
import com.example.glidedemo_4x.test2.TestActivity2;
import com.example.glidedemo_4x.webp.WebpActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 基础测试
     * @param v
     */
    public void onTest1(View v) {
        startActivity(new Intent(this, TestActivity1.class));
    }

    /**
     * 自定义ModelLoader
     */
    public void onTest2(View v) {
        startActivity(new Intent(this, TestActivity2.class));

    }

    /**
     * 测试加载Webp的情况
     */
    public void onTest3(View v) {
        startActivity(new Intent(this, WebpActivity.class));
    }

}