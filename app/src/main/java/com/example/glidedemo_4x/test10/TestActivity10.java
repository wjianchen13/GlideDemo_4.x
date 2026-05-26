package com.example.glidedemo_4x.test10;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.glidedemo_4x.R;
import com.example.glidedemo_4x.test9.PagData;
import com.example.glidedemo_4x.test9.PagDataViewTarget;

import org.libpag.PAGFile;
import org.libpag.PAGView;

/**
 *
 */
public class TestActivity10 extends AppCompatActivity {

    private String mPagUrl = "https://pag.io/file/like.pag";
    private PAGView pagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test10);
        pagView = findViewById(R.id.pag_view);
    }

    public void onTest1(View v) {
        Glide.with(this)
                .as(PAGFile.class)
                .load(mPagUrl)
                .into(new PAGFileViewTarget10(pagView));
    }

    /**
     * 重复点击时能正确取消旧请求。
     * @param v
     */
    public void onTest2(View v) {


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
