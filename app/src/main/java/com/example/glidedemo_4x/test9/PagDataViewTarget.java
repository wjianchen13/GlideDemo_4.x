package com.example.glidedemo_4x.test9;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.transition.Transition;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * 自定义 ViewTarget，将 PagData 设置到 PAGView 并播放
 *
 * 继承 CustomViewTarget 的好处：
 * 1. Glide 自动将 Request 存入 View.tag，重复调用 into() 时自动取消旧请求
 * 2. 不需要手动保存 target 引用，也不需要手动 clear
 * 3. Activity 销毁时，RequestManager 通过 View.tag 找到请求并自动取消
 *
 * 通过 OnPagFileReadyListener 回调，让调用方在设置到 PAGView 之前自定义处理逻辑
 * 比如替换图片、替换文字等，每个页面的替换逻辑可以不同
 */
public class PagDataViewTarget extends CustomViewTarget<PAGView, PagData> {

    /**
     * PAGFile 就绪回调，在设置到 PAGView 之前调用
     * 调用方可以在此回调中做替换图片、文字等自定义处理
     */
    public interface OnPagFileReadyListener {
        void onReady(PAGFile pagFile);
    }

    private final OnPagFileReadyListener listener;

    /**
     * 不需要自定义处理时使用
     */
    public PagDataViewTarget(@NonNull PAGView view) {
        this(view, null);
    }

    /**
     * 需要在设置到 PAGView 之前做自定义处理时使用
     * @param listener PAGFile 就绪回调，可在回调中替换图片、文字等
     */
    public PagDataViewTarget(@NonNull PAGView view, @Nullable OnPagFileReadyListener listener) {
        super(view);
        this.listener = listener;
    }

    @Override
    public void onResourceReady(@NonNull PagData pagData, @Nullable Transition<? super PagData> transition) {
        // 先让调用方做自定义处理（替换图片、文字等）
        if (listener != null) {
            listener.onReady(pagData.pagFile);
        }
        // 处理完再设置到 PAGView 并播放
        PAGView pagView = getView();
        pagView.setComposition(pagData.pagFile);
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
