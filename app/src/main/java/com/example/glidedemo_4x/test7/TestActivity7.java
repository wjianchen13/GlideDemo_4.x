package com.example.glidedemo_4x.test7;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.glidedemo_4x.R;

/**
 */
public class TestActivity7 extends AppCompatActivity {

    private String mUrl = "https://img.ayomet.com/upload/room_img/2024-09-17/100691807_1726566456098.jpeg?imageView2/0/w/160/h/160";
    private String mUrl1 = "https://img.ayomet.com/upload/banner/2024-11-01/db167314bff9f82f722de2c1aeb39162.jpg";
    private ImageView imgvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test7);
        imgvTest = findViewById(R.id.imgv_test);
    }

    /**
     *
     */
    public void onTest1(View v) {
        Glide.with(this)
//                .asFile()
                .load(mUrl1)
//                .apply(requestOptions)
                .into(new CustomTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        if(imgvTest != null)
                            imgvTest.setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
