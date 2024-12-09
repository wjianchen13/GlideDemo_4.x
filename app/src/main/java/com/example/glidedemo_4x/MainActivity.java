package com.example.glidedemo_4x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import com.example.glidedemo_4x.test1.TestActivity1;

public class MainActivity extends AppCompatActivity {

    private String mUrl = "https://img.ayomet.com/upload/room_img/2024-09-17/100691807_1726566456098.jpeg?imageView2/0/w/160/h/160";
    private String mUrl1 = "https://img.ayomet.com/upload/banner/2024-11-01/db167314bff9f82f722de2c1aeb39162.jpg";
    private ImageView imgvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgvTest = findViewById(R.id.imgv_test);
    }


    public void onTest1(View v) {
        Utils.log("MainActivity onTest1");
//        Glide.with(this).load(mUrl).into(imgvTest);
        Glide.with(this)
//                .asBitmap()
                .load(mUrl1)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .override(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .into(imgvTest);
    }

    /**
     * 自定义ModelLoader
     */
    public void onTest2(View v) {
        startActivity(new Intent(this, TestActivity1.class));

    }


}