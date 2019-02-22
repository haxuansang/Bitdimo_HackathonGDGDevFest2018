package com.appproteam.sangha.bitdimo.Retrofit;
import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.CurrentUser;
import com.appproteam.sangha.bitdimo.Retrofit.ObjectRetrofit.RequestUser;
import com.appproteam.sangha.bitdimo.Singleton.OutstandingPost;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;


public interface SOService {
    @GET("/users/{path}/")
    Call<List<RequestUser>> getCurrentUser(@Path("path") String path);
    @POST("/users/")
    Call<List<RequestUser>> registerAccount(@Body CurrentUser currentUser);
    @GET("/users/post/{path}/")
    Call<List<ExplorePost>> getExplorePosts(@Path("path") String path);
    @GET("/{path}/")
    Call<List<OutstandingPost>> getOutstandingPosts(@Path("path") String path);




}
