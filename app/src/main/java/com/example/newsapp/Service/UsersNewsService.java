package com.example.newsapp.Service;

import com.example.newsapp.bean.Commentbean.Comment;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UsersNewsService {
    @GET("news/getNewsList")
    Call<List<UsersNews>> getUsersNewsList();

    @GET("news/getMyNewsList")
    Call<List<UsersNews>> getMyNewsList(@Query("username") String username);

    @POST("news/deleteNews") //必须用post请求
    @FormUrlEncoded
    Call<ResponseBody> deleteNews(@Field("newsId") int newsId);
}
