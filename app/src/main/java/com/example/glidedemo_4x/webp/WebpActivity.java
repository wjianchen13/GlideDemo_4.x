package com.example.glidedemo_4x.webp;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.glidedemo_4x.R;
import com.opensource.svgaplayer.SVGAImageView;

/**
 * 测试一定缓存条件下，加载完webp之后大量加载svga，看下webp是否会首先从缓存清除掉
 *
 */
public class WebpActivity extends AppCompatActivity {

    private String mWebpUrl = "https://img.ayomet.com/upload/app_matter/2025-02-27/8d1652939b3f8cbcb78c17a590c3399b.webp?imageslim";
    private ImageView imgvTest;
    private SVGAImageView imgvTest1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webp);

    }

    private void loadSVGAFromNetwork(SVGAImageView v, String url) {
        Glide.with(this)
                .load(url)
                .into(v);
    }

    public void onTest(View v) {
        imgvTest = findViewById(R.id.image_view);
        CoreUtils.playWebpAnim(this, imgvTest, mWebpUrl, 0, 1, null);
    }

    public void onTest1(View v) {

    }
}
