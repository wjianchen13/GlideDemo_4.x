package com.example.glidedemo_4x.test9;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.glidedemo_4x.R;

import org.libpag.PAGView;

/**
 * 加载Pag添加转码
 *
 * 数据流：
 * 网络URL → InputStream/ByteBuffer → PAGFile（解码） → PagData（转码） → 设置到 PAGView
 *
 * 使用 PagDataViewTarget（继承 CustomViewTarget）代替 CustomTarget：
 * 1. Glide 自动将 Request 存入 PAGView 的 tag 中
 * 2. 重复点击时，Glide 通过 tag 发现旧请求，自动取消，无需手动管理
 * 3. Activity 销毁时，RequestManager 自动清除，无需在 onDestroy 中手动 clear
 */
public class TestActivity9 extends AppCompatActivity {

    private String mPagUrl = "https://pag.io/file/like.pag";

    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test9);
        pagView = findViewById(R.id.pag_view);
    }

    public void onTest1(View v) {
        // 直接 into(target)，Glide 会：
        // 1. 从 pagView.tag 取出旧请求并取消
        // 2. 存入新请求到 pagView.tag
        // 3. 启动新请求
        // 跟 into(ImageView) 的行为一致，无需手动管理
        Glide.with(this)
                .as(PagData.class)
                .load(mPagUrl)
                .into(new PagDataViewTarget(pagView));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pagView != null) {
            pagView.freeCache();
        }
    }
}
