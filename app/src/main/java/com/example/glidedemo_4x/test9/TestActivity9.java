package com.example.glidedemo_4x.test9;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.glidedemo_4x.R;

import org.libpag.PAGView;

/**
 * 加载Pag添加转码
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
        Glide.with(this)
                .as(PagData.class)    // transcodeClass = PagData.class
                .load(mPagUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 加上这个
                .into(new CustomTarget<PagData>() {
                    @Override
                    public void onResourceReady(@NonNull PagData pagData, @Nullable Transition<? super PagData> transition) {
                        if (pagView != null && pagData.pagFile != null) {
                            pagView.setComposition(pagData.pagFile);
                            pagView.setRepeatCount(0);
                            pagView.play();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
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
