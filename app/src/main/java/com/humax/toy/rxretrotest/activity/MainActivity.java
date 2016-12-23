package com.humax.toy.rxretrotest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.humax.toy.rxretrotest.R;

/**
 * Created by Tony on 16/11/2
 */

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void jumpToRxRetrofitActivity(View view) {
        startActivity(new Intent(this,RxRetrofitActivity.class));
    }

    public void jumpToRxAndroidActivity(View view) {
        startActivity(new Intent(this,RxAndroidActivity.class));
    }

    public void jumpToRxAutoAdapterActivity(View view) {
        startActivity(new Intent(this,AutoAdapterActivity.class));
    }

    public void jumpToMultiActivity(View view) {
        startActivity(new Intent(this,MultiItemsAdapterActivity.class));
    }

    public void jumpToRxJavaActivity(View view) {
        startActivity(new Intent(this,RxJavaActivity.class));
    }
}
