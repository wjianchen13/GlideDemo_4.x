package com.example.glidedemo_4x.test10;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.libpag.PAGFile;
import org.libpag.PAGView;

public class PAGFileViewTarget10 extends CustomViewTarget<PAGView, PAGFile> {

    /**
     * PAGFile 就绪回调，在设置到 PAGView 之前调用
     * 调用方可以在此回调中做替换图片、文字等自定义处理
     */
    public interface OnPagFileReadyListener {
        void onReady(PAGFile pagFile);
    }

    private final OnPagFileReadyListener listener;

    public PAGFileViewTarget10(@NonNull PAGView view) {
        this(view, null);
    }

    public PAGFileViewTarget10(@NonNull PAGView view,  @Nullable OnPagFileReadyListener listener) {
        super(view);
        this.listener = listener;
    }

    @Override
    public void onResourceReady(@NonNull PAGFile resource, @Nullable Transition<? super PAGFile> transition) {
        if(listener != null) {
            listener.onReady(resource);
        }
        PAGView pagView = getView();
        pagView.setComposition(resource);
        pagView.setRepeatCount(0);
        pagView.play();
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


}
