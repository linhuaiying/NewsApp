package com.example.newsapp.Service;

import com.example.newsapp.bean.ConcernNewsbean.ConcernNews;
import com.example.newsapp.bean.Newsbean.NewsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ConcernNewsService {
    @GET("news/getNewsList")
    Call<List<ConcernNews>> getConcernNewsList();
}
