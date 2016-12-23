package com.humax.toy.rxretrotest.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.benny.library.autoadapter.AutoRecyclerAdapter;
import com.benny.library.autoadapter.SimpleAdapterItemAccessor;
import com.benny.library.autoadapter.viewcreator.ViewCreator;
import com.benny.library.autoadapter.viewholder.DataGetter;
import com.humax.toy.rxretrotest.holder.BaseViewHolder;
import com.humax.toy.rxretrotest.R;
import com.humax.toy.rxretrotest.internet.IService;
import com.humax.toy.rxretrotest.module.ResultInstance;
import com.humax.toy.rxretrotest.widget.DividerItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Tony on 16/11/2
 */

public class AutoAdapterActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Context context;
    private ProgressDialog pd;

    SimpleAdapterItemAccessor<ResultInstance> itemAccessor = new SimpleAdapterItemAccessor<>();

    private IService iService;
    private Subscriber<List<ResultInstance>> subscriber = new Subscriber<List<ResultInstance>>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<ResultInstance> resultInstances) {
            pd.dismiss();
            itemAccessor.update(resultInstances);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adapter);
        ButterKnife.bind(this);
        context = this;
        pd = new ProgressDialog(this);
        pd.show();
        initData();
        configRecyclerView();
    }

    private void configRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager
                .VERTICAL,true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        recyclerView.setAdapter(new AutoRecyclerAdapter<>(itemAccessor, new ViewCreator<ResultInstance>(R.layout
                .adapter_item, MovieHolder::new)));
    }

    private void initData() {
        configRetrofit();
        iService.RxVisit("square", "retrofit").
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private void configRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.github.com")
                //添加对返回结果String的支持
                .addConverterFactory(ScalarsConverterFactory.create())
                //添加对返回Json数据格式的支持
                .addConverterFactory(GsonConverterFactory.create())
                //添加对Rx返回结果的支持
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        iService = retrofit.create(IService.class);
    }

    static class MovieHolder extends BaseViewHolder<ResultInstance> {

        @BindView(R.id.tv_id)
        TextView vId;

        @BindView(R.id.tv_login)
        TextView vLogin;

        @BindView(R.id.tv_contribution)
        TextView vContribution;

        @Override
        public void onDataChange(DataGetter<ResultInstance> getter, int position) {
            ResultInstance resultInstance = getter.data;
            vId.setText(String.valueOf(resultInstance.getId()));
            vLogin.setText(resultInstance.getLogin());
            vContribution.setText(String.valueOf(resultInstance.getContributions()));
        }
    }


}
