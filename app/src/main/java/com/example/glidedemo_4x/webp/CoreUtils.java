package com.example.glidedemo_4x.webp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.webp.decoder.WebpDecoder;
import com.bumptech.glide.integration.webp.decoder.WebpDrawable;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.lang.reflect.Field;

public class CoreUtils {

    /**
     * 播放Webp动画
     * @param context
     * @param svgaImageView
     * @param url 资源url
     * @param times
     */
    public static void playWebpAnim(Context context, ImageView svgaImageView, String url, int version, int times, PlayWebpListener callBack){
        playWebpAnim(context, svgaImageView, 0, url, version, times, callBack);
    }

    /**
     * 播放Webp动画
     * @param context
     * @param svgaImageView
     * @param resId 本地资源id
     * @param times
     */
    public static void playWebpAnim(Context context, ImageView svgaImageView, int resId, int times, PlayWebpListener callBack){
        playWebpAnim(context, svgaImageView, resId, "", 0, times, callBack);
    }

    /**
     * 播放Webp动画
     * @param context
     * @param svgaImageView
     * @param resId 本地资源id
     * @param url 网络图片
     * @param version 图片版本
     * @param times
     */
    public static void playWebpAnim(Context context, ImageView svgaImageView, int resId, String url, int version, int times, PlayWebpListener callBack){
        if(svgaImageView == null || (resId == 0 && TextUtils.isEmpty(url)) || context == null) {
            if(svgaImageView != null) {
                svgaImageView.setVisibility(View.GONE);
            }
            if(callBack != null) {
                callBack.onPlayEnd(0);
            }
            return ;
        }
        svgaImageView.setImageDrawable(null);
        RequestListener listener = new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Drawable> target, boolean isFirstResource) {
                if(svgaImageView != null) {
                    svgaImageView.setVisibility(View.GONE);
                }
                if(callBack != null) {
                    callBack.onPlayEnd(0);
                }
                return false;
            }

            @Override
            public boolean onResourceReady(@NonNull Drawable res, @NonNull Object model, Target<Drawable> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                if (res instanceof WebpDrawable) {
                    WebpDrawable resource = (WebpDrawable)res;
//                        if(resId != 0)
                    modifyDuration(resource);
                    resource.setLoopCount(times);
                    Animatable2Compat.AnimationCallback a = new Animatable2Compat.AnimationCallback() {

                        @Override
                        public void onAnimationStart(Drawable drawable) {
                            super.onAnimationStart(drawable);

                        }

                        @Override
                        public void onAnimationEnd(Drawable drawable) {
                            super.onAnimationEnd(drawable);
                            System.out.println("===========================> onAnimationEnd");
                            try {
                                if (drawable != null && drawable instanceof WebpDrawable) {
                                    WebpDrawable webpDrawable = (WebpDrawable) drawable;
                                    if (!webpDrawable.isRunning()) {
                                        webpDrawable.startFromFirstFrame();
                                        webpDrawable.stop();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            if(svgaImageView != null) {
//                                svgaImageView.setVisibility(View.GONE);
//                            }
                            if(callBack != null) {
                                callBack.onPlayEnd(1);
                            }
                        }
                    };
                    resource.clearAnimationCallbacks();
                    // 在动画播放完成后的监听
                    resource.registerAnimationCallback(a);
                    if(svgaImageView != null) {
                        svgaImageView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                svgaImageView.setVisibility(View.VISIBLE);
                            }
                        }, 10);

                    }
                    if(callBack != null) {
                        callBack.onPlayStart(version);
                    }
                    resource.startFromFirstFrame();
                    return false;
                } else {
                    if(callBack != null) {
                        callBack.onPlayEnd(1);
                    }
                }

                return false;
            }
        };
        svgaImageView.clearAnimation();
        if(resId > 0) {
            Glide.with(context)
                    .load(resId)
                    .listener(listener)
                    .into(svgaImageView);
        } else if(!TextUtils.isEmpty(url)) {
            Glide.with(context)
                    .load(url)
                    .listener(listener)
                    .into(svgaImageView);
        }
    }

    public static void modifyDuration(Drawable res) {
        if(res == null || !(res instanceof WebpDrawable))
            return ;
        try {
            //已知三方库的bug，webp的动图每一帧的时间间隔于实际的有所偏差，需要反射三方库去修改
            //https://github.com/zjupure/GlideWebpDecoder/issues/33
            Field gifStateField = ((WebpDrawable)res).getClass().getDeclaredField("state");
            gifStateField.setAccessible(true);//开放权限
            Class gifStateClass = Class.forName("com.bumptech.glide.integration.webp.decoder.WebpDrawable$WebpState");
            Field gifFrameLoaderField = gifStateClass.getDeclaredField("frameLoader");
            gifFrameLoaderField.setAccessible(true);

            Class gifFrameLoaderClass = Class.forName("com.bumptech.glide.integration.webp.decoder.WebpFrameLoader");
            Field gifDecoderField = gifFrameLoaderClass.getDeclaredField("webpDecoder");
            gifDecoderField.setAccessible(true);

            WebpDecoder webpDecoder = (WebpDecoder) gifDecoderField.get(gifFrameLoaderField.get(gifStateField.get(res)));
            Field durations = webpDecoder.getClass().getDeclaredField("mFrameDurations");
            durations.setAccessible(true);
            int[] args = (int[]) durations.get(webpDecoder);
            if (args.length > 0) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] > 30) {
                        //加载glide会比ios慢 这边把gif的间隔减少15s
                        args[i] = args[i] - 10;
                    }
                }
            }
            durations.set(webpDecoder, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
