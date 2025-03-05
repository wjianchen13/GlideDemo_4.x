package com.example.glidedemo_4x.webp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.glidedemo_4x.R;

/**
 * 测试加载Webp的情况
 */
public class WebpActivity extends AppCompatActivity {

    private String mUrl = "https://img.ayomet.com/upload/app_matter/2025-02-27/8d1652939b3f8cbcb78c17a590c3399b.webp?imageslim";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webp);

    }

    public void onTest1(View v) {
        ImageView imageView = findViewById(R.id.image_view);
        CoreUtils.playWebpAnim(this, imageView, mUrl, 0, 1, null);
    }
}
