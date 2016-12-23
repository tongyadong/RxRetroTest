package com.humax.toy.rxretrotest.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.humax.toy.rxretrotest.module.ResultInstance;
import com.humax.toy.rxretrotest.internet.IService;
import com.humax.toy.rxretrotest.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RxRetrofitActivity extends AppCompatActivity {

    private Retrofit retrofit;

    private IService gitHub;

    static class MyHandler extends Handler {
        WeakReference mActivity;

        MyHandler(RxRetrofitActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RxRetrofitActivity activity = (RxRetrofitActivity) mActivity.get();
            if (msg.what == 0) {
                Response<List<ResultInstance>> execute = (Response<List<ResultInstance>>) msg.obj;
                activity.Log(execute);
            }
        }
    }

    private MyHandler handler = new MyHandler(this);

    Runnable runnable = () -> {

        Call<List<ResultInstance>> contribute = gitHub.visit("square", "retrofit");
        try {
            Response<List<ResultInstance>> execute = contribute.execute();
            Message message = Message.obtain();
            message.obj = execute;
            message.what = 0;
            handler.sendMessage(message);

        } catch (IOException e) {
            e.printStackTrace();
        }

    };

    private Subscriber<List<ResultInstance>> subscriber = new Subscriber<List<ResultInstance>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<ResultInstance> contributors) {
            Toast.makeText(RxRetrofitActivity.this,"方式1共获取到"+contributors.size()+"条数据" , Toast
                    .LENGTH_SHORT).show();
            for (ResultInstance c : contributors) {
//                Log.i("aaa", "Login:" + c.getLogin() + ", ID:" + c.getId() + ", Con:" + c
//                        .getContributions());
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_retrofit);
        configRetrofit();
    }

    private void configRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.github.com")//设置基础URL
                        //增加返回值为Json的支持(以实体类返回)
                .addConverterFactory(GsonConverterFactory.create())
                        //增加返回值为String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                        //增加返回值为Observable<T>的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        gitHub = retrofit.create(IService.class);

    }

    public void useHandlerToGetData(View view) {
        new Thread(runnable).start();
        //Call<List<ResultInstance>> visit = gitHub.visit("square", "retrofit");
        //visit.enqueue(new Callback<T>());此为异步操作无需单开线程
    }

    public void useRxToGetData(View view) {
        gitHub.RxVisit("square", "retrofit")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public void Log(Response<List<ResultInstance>> execute) {
        List<ResultInstance> body = execute.body();
        Toast.makeText(this, "方式2共获取到"+body.size()+"条数据", Toast.LENGTH_SHORT).show();
        for (ResultInstance c : body) {
//            Log.i("aaa", c.getLogin() + c.getId() + c.getContributions());
        }

    }


}
