package com.example.glidedemo_4x.test6;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.glidedemo_4x.R;
import com.example.glidedemo_4x.Utils;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 * 测试 Glide 自定义解码器加载 PAG 文件（InputStream 方式）
 * 与 test4 的区别：
 *   test4: PAGFileResourceDecoder<ByteBuffer, PAGFile>，依赖磁盘缓存将 File 转为 ByteBuffer
 *   test6: PAGFileStreamDecoder2<InputStream, PAGFile>，直接从 InputStream 解码，无论有无缓存都能工作
 * 两者都不需要自定义 ModelLoader，使用 Glide 内置的网络加载链
 */
public class TestActivity6 extends AppCompatActivity {

    // 测试用的 PAG 网络资源 URL，请替换为你自己的 URL
    private String mPagUrl = "https://pag.io/file/like.pag";

    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test6);
        pagView = findViewById(R.id.pag_view);
    }

    /**
     * 使用 Glide 自定义解码器加载网络 PAG 文件（InputStream 方式）
     */
    public void onTest1(View v) {
        Utils.log("TestActivity6 onTest1: InputStream方式加载 PAG 文件 " + mPagUrl);
        Glide.with(this)
                .as(PAGFile.class)
                .load(mPagUrl)
//                .diskCacheStrategy(DiskCacheStrategy.NONE)  // 加上这个
                .skipMemoryCache(true)
                .into(new PAGViewTarget6(pagView));
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
