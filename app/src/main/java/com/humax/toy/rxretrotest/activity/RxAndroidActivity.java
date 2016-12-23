package com.humax.toy.rxretrotest.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.humax.toy.rxretrotest.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by dong on 16/11/1
 * 注: 订阅是使用的是Observer需要手动取消订阅,使用Subscriber则会在订阅回调执行后自动取消订阅
 */

public class RxAndroidActivity extends Activity{

    @BindView(R.id.list_view)
    ListView listView;

    private List<String> list;
    private ProgressDialog pd;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_list);
        ButterKnife.bind(this);
        pd = new ProgressDialog(this);
        //显示本地数据
        //useObservableInLocal();
        //显示网络数据
        //useObservableInNetwork();
        //使用Single(轻量级的Observable)
        useSingle();

    }

    private void useSingle() {

        pd.show();
        subscription = Single.fromCallable(this::getNetWorkData)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<List<String>>() {
                    @Override
                    public void onSuccess(List<String> value) {
                        pd.dismiss();
                        listView.setAdapter(getListAdapter(value));
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });
    }

    private void useObservableInNetwork() {
        /**
         * 使用Observable.fromCallable(callBack)而不是Observable.just()
         * 1.获取要发送的数据的代码只会在有Observer订阅之后执行。
         * 2.获取数据的代码可以在子线程中执行。
         */
        pd.show();
        subscription = Observable.fromCallable(this::getNetWorkData)
                //在io线程中执行
                .subscribeOn(Schedulers.io())
                //UI线程观察Observable,就是让onNext()方法在主线程执行
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<String> strings) {
                        pd.dismiss();
                        listView.setAdapter(getListAdapter(strings));
                    }
                });

    }

    private List<String> getNetWorkData() {
        try {
            Thread.sleep(5000);
            list = getColorList();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void useObservableInLocal() {
        //得到一个Observable对象(getColorList()为非耗时操作)
        Observable<List<String>> observable = Observable.just(getColorList());
        //订阅
        subscription = observable.subscribe(new Observer<List<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<String> strings) {
                //返回订阅结果
                listView.setAdapter(getListAdapter(strings));
            }
        });
    }

    @NonNull
    private ListAdapter getListAdapter(List<String> strings) {
        return new ArrayAdapter<>(getApplicationContext(),
                            android.R.layout.simple_list_item_1,android.R.id.text1,strings);
    }

    private List<String> getColorList() {
        list = new ArrayList<>();
        list.add("red");
        list.add("yellow");
        list.add("green");
        list.add("blue");
        list.add("black");
        return list;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();//取消订阅
    }
}
