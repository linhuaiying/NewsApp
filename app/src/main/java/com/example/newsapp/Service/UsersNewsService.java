package com.example.newsapp.Service;

import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersNewsService {
    @GET("news/getNewsList")
    Call<List<UsersNews>> getUsersNewsList();

    @GET("news/getMyNewsList")
    Call<List<UsersNews>> getMyNewsList(@Query("username") String username);
}
