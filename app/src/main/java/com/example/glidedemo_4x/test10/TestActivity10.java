package com.example.glidedemo_4x.test10;

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
 *
 */
public class TestActivity10 extends AppCompatActivity {

    private String mPagUrl = "https://pag.io/file/like.pag";
    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test10);
        pagView = findViewById(R.id.pag_view);
    }

    public void onTest1(View v) {
        Glide.with(this)
                .as(PAGFile.class)
                .load(mPagUrl)
                .into(new PAGFileViewTarget10(pagView));
    }

    /**
     * 重复点击时能正确取消旧请求。
     * @param v
     */
    public void onTest2(View v) {
        CustomTarget target = new CustomTarget<PAGFile>() {
            @Override
            public void onResourceReady(@NonNull PAGFile pagFile, @Nullable Transition<? super PAGFile> transition) {
                if (pagView != null && pagFile != null) {
                    pagView.setComposition(pagFile);
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
        };
        Object tag = v.getTag();
        if(tag != null && tag instanceof CustomTarget) {
            Glide.with(this).clear((CustomTarget) tag);
        }

        Glide.with(this)
                .as(PAGFile.class)    // transcodeClass = PagData.class
                .load(mPagUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 加上这个
                .into(target);
        v.setTag(target);

    }

    /**
     * 重复点击时能正确取消旧请求。
     * @param v
     */
    public void onTest3(View v) {
        Glide.with(this)
                .as(PAGData10.class)
                .load(mPagUrl)
                .into(new PAGDataViewTarget10(pagView));
    }

    /**
     * 重复点击时能正确取消旧请求。
     * @param v
     */
    public void onTest4(View v) {
        CustomTarget target = new CustomTarget<PAGData10>() {
            @Override
            public void onResourceReady(@NonNull PAGData10 pagData, @Nullable Transition<? super PAGData10> transition) {
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
        };
        Object tag = v.getTag();
        if(tag != null && tag instanceof CustomTarget) {
            Glide.with(this).clear((CustomTarget) tag);
        }

        Glide.with(this)
                .as(PAGData10.class)    // transcodeClass = PagData.class
                .load(mPagUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 加上这个
                .into(target);
        v.setTag(target);
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
