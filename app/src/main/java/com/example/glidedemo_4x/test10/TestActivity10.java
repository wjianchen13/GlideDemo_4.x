package com.example.glidedemo_4x.test10;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.glidedemo_4x.R;

/**
 *
 */
public class TestActivity10 extends AppCompatActivity {

    private String mPagUrl = "https://pag.io/file/like.pag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test10);

    }

    public void onTest1(View v) {

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
