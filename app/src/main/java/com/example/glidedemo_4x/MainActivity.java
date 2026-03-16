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
import com.example.glidedemo_4x.test4.TestActivity4;
import com.example.glidedemo_4x.test5.TestActivity5;
import com.example.glidedemo_4x.test6.TestActivity6;
import com.example.glidedemo_4x.test7.TestActivity7;
import com.example.glidedemo_4x.test8.TestActivity8;
import com.example.glidedemo_4x.test9.TestActivity9;
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

    /**
     * Glide自定义解码器加载PAG文件
     */
    public void onTest4(View v) {
        startActivity(new Intent(this, TestActivity4.class));
    }

    /**
     * Glide自定义ModelLoader加载PAG文件
     */
    public void onTest5(View v) {
        startActivity(new Intent(this, TestActivity5.class));
    }

    /**
     * Glide自定义解码器加载PAG文件
     */
    public void onTest6(View v) {
        startActivity(new Intent(this, TestActivity6.class));
    }

    /**
     * 加载Drawable
     */
    public void onTest7(View v) {
        startActivity(new Intent(this, TestActivity7.class));
    }

    /**
     * 加载Drawable
     */
    public void onTest8(View v) {
        startActivity(new Intent(this, TestActivity8.class));
    }

    /**
     * 加载Pag添加转码
     */
    public void onTest9(View v) {
        startActivity(new Intent(this, TestActivity9.class));
    }

}