package com.example.glidedemo_4x.test8;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.glidedemo_4x.R;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * 加载Pag资源
 * 使用 CustomTarget 把 PAGFile 回传出来，手动设置到 PAGView 上
 */
public class TestActivity8 extends AppCompatActivity {

    // 测试用的 PAG 网络资源 URL，请替换为你自己的 URL
    private String mPagUrl = "https://pag.io/file/like.pag";

    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test8);
        pagView = findViewById(R.id.pag_view);
    }

    public void onTest1(View v) {
        Glide.with(this)
                .as(PAGFile.class)
                .load(mPagUrl)
                .into(new CustomTarget<PAGFile>() {
                    @Override
                    public void onResourceReady(@NonNull PAGFile pagFile, @Nullable Transition<? super PAGFile> transition) {
                        if (pagView != null) {
                            pagView.setComposition(pagFile);
                            pagView.setRepeatCount(0);
                            pagView.play();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        // Glide 回收资源时调用，停止播放并清除
                        if (pagView != null) {
                            pagView.stop();
                            pagView.setComposition(null);
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (pagView != null) {
            pagView.freeCache();
        }
    }
}
