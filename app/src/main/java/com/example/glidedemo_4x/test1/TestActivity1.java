package com.example.glidedemo_4x.test1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.glidedemo_4x.R;
import com.example.glidedemo_4x.Utils;

public class TestActivity1 extends AppCompatActivity {

    private String mUrl = "https://img.ayomet.com/upload/room_img/2024-09-17/100691807_1726566456098.jpeg?imageView2/0/w/160/h/160";
    private String mUrl1 = "https://img.ayomet.com/upload/banner/2024-11-01/db167314bff9f82f722de2c1aeb39162.jpg";
    private ImageView imgvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1);
        imgvTest = findViewById(R.id.imgv_test);
    }


    public void onTest1(View v) {
        Utils.log("MainActivity onTest1");
//        Glide.with(this).load(mUrl).into(imgvTest);
        Glide.with(this)
//                .asBitmap()
                .load(mUrl1)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imgvTest);
    }


}