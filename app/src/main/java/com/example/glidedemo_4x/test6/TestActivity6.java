package com.example.glidedemo_4x.test6;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.glidedemo_4x.R;
import com.example.glidedemo_4x.Utils;
import com.example.glidedemo_4x.test4.PAGViewTarget;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 *
 */
public class TestActivity6 extends AppCompatActivity {

    // 测试用的 PAG 网络资源 URL，请替换为你自己的 URL
    private String mPagUrl = "https://pag.io/file/like.pag";

    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test6);
        pagView = findViewById(R.id.pag_view);
    }

    /**
     * 使用 Glide 自定义 ModelLoader 方式加载网络 PAG 文件
     */
    public void onTest1(View v) {
        Utils.log("TestActivity5 onTest1: ModelLoader方式加载 PAG 文件 " + mPagUrl);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
