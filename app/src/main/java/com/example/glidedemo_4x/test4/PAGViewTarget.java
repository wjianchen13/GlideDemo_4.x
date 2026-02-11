package com.example.glidedemo_4x.test4;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * Glide 自定义 Target，将 PAGFile 设置到 PAGView 并播放
 */
public class PAGViewTarget extends CustomViewTarget<PAGView, PAGFile> {

    public PAGViewTarget(@NonNull PAGView view) {
        super(view);
    }

    @Override
    protected void onResourceCleared(@Nullable Drawable placeholder) {
        PAGView pagView = getView();
        pagView.stop();
        pagView.setComposition(null);
    }

    @Override
    public void onLoadFailed(@Nullable Drawable errorDrawable) {
        PAGView pagView = getView();
        pagView.stop();
        pagView.setComposition(null);
    }

    @Override
    public void onResourceReady(@NonNull PAGFile resource, @Nullable Transition<? super PAGFile> transition) {
        PAGView pagView = getView();
        pagView.setComposition(resource);
        pagView.setRepeatCount(0); // 无限循环
        pagView.play();
    }
}
