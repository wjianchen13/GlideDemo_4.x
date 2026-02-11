package com.example.glidedemo_4x.test4;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.glidedemo_4x.R;
import com.example.glidedemo_4x.Utils;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * 测试 Glide 自定义解码器加载 PAG 文件
 * 通过自定义 PAGFileResourceDecoder 将 ByteBuffer 解码为 PAGFile
 * 通过自定义 PAGViewTarget 将 PAGFile 设置到 PAGView 播放
 */
public class TestActivity4 extends AppCompatActivity {

    // 测试用的 PAG 网络资源 URL，请替换为你自己的 URL
    private String mPagUrl = "https://pag.io/file/like.pag";

    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);
        pagView = findViewById(R.id.pag_view);
    }

    /**
     * 使用 Glide 自定义解码器加载网络 PAG 文件
     */
    public void onTest1(View v) {
        Utils.log("TestActivity4 onTest1: 加载 PAG 文件 " + mPagUrl);
        Glide.with(this)
                .as(PAGFile.class)
                .load(mPagUrl)
                .into(new PAGViewTarget(pagView));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pagView != null) {
            pagView.stop();
            pagView.freeCache();
        }
    }
}
