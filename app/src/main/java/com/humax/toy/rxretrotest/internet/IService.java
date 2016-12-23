package com.humax.toy.rxretrotest.internet;

import com.humax.toy.rxretrotest.module.ResultInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


/**
 * Created by Tony on 16/9/2
 */
public interface IService {
    //example --> https://api.github.com/repos/square/retrofit/contributors
    @GET("/repos/{owner}/{repo}/contributors")
    Call<List<ResultInstance>> visit(@Path("owner") String owner, @Path("repo") String repo);

    //使用 RxJava 的方法,返回一个 Observable
    @GET("/repos/{owner}/{repo}/contributors")
    Observable<List<ResultInstance>> RxVisit(@Path("owner") String owner, @Path("repo") String
            repo);
}
