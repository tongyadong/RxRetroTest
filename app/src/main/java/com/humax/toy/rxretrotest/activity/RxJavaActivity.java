package com.humax.toy.rxretrotest.activity;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.humax.toy.rxretrotest.R;
import com.humax.toy.rxretrotest.module.Student;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Tony on 16/11/3
 * RxJava 的默认规则中，事件的发出和消费都是在同一个线程的
 */

public class RxJavaActivity extends Activity {

    @BindView(R.id.tv_content)
    TextView content;

    @BindView(R.id.iv)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java);
        ButterKnife.bind(this);
        //创建Observable对象
        //createObservable();
        //自定义回调
        //customCallback();
        //一次综合使用
        //simpleUse();
        //变换
        useMap();
        //

    }

    private void useMap() {
        //map();
        flatMap();
    }

    private void flatMap() {
        //省略for循环代码
        Student student1 = new Student("1", "Tony", 8, new String[]{"语文", "数学", "英语"});
        Student student2 = new Student("2", "Bob", 7, new String[]{"物理", "化学", "生物"});
        Student student3 = new Student("3", "David", 9, new String[]{"政治", "历史", "地理"});

        Observable.from(new Student[]{student1, student2, student3})
                .flatMap(new Func1<Student, Observable<String>>() {
                    @Override
                    public Observable<String> call(Student student) {
                        return Observable.from(student.getCourses());
                    }
                }).subscribe(s -> {
                    Log.i("aaa",s);
                });
    }

    private void map() {
        /**
         * Function和Action类似,但是function有返回值,action没有
         */
        Observable.just("小王").map(new Func1<String, Student>() {
            @Override
            public Student call(String s) {
                return new Student("001", s, 5,new String[]{"Math","English"});
            }
        }).subscribe(new Action1<Student>() {
            @Override
            public void call(Student student) {
                Log.i("aaa", student.getId() + student.getName() + student.getAge());
            }
        });
    }

    private void simpleUse() {
        //创建出 Observable 和 Subscriber ，再用 subscribe() 将它们串起来，一次 RxJava 的基本使用就完成了
        Observable.create(new Observable.OnSubscribe<Drawable>() {
            @Override
            public void call(Subscriber<? super Drawable> subscriber) {
                Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
                subscriber.onNext(drawable);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread()) // 指定 Subscriber 的回调发生在主线程
                .subscribe(new Observer<Drawable>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(RxJavaActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(Drawable drawable) {
                        imageView.setBackground(drawable);
                    }
                });
    }

    private void customCallback() {
        Action0 onCompleteAction = new Action0() {
            @Override
            public void call() {
                Log.i("aaa", "complete");
            }
        };

        Action1<String> onNextAction = new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i("aaa", s);
            }
        };

        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            @Override
            public void call(Throwable o) {
                Log.i("aaa", o.getMessage());
            }
        };
        // 自动创建 Subscriber ，并使用 自定义action
        Observable.just("hhh").subscribe(onNextAction);
        Observable.just("hhh").subscribe(onNextAction, onErrorAction);
        Observable.just("hhh").subscribe(onNextAction, onErrorAction, onCompleteAction);
    }

    private void createObservable() {
        methodCreate();
        methodJustAndFrom();

    }

    private void methodCreate() {
        Observable<String> observable = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("hello");
                subscriber.onNext("world");
                subscriber.onNext("RxJava");
                subscriber.onCompleted();
            }
        });
        //订阅
        observable.subscribe(s -> {
            Log.i("aaa", s);
        });
    }

    private void methodJustAndFrom() {
        Observable.just("h", "e", "l", "l", "0").subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i("aaa", s);
            }
        });

        String[] arrs = {"w", "o", "r", "l", "d"};
        Observable.from(arrs).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                Log.i("aaa", s);
            }
        });
    }
}
