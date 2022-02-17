package com.example.newsapp.Service;



import com.example.newsapp.bean.Newsbean.NewsResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NewsService {
    @POST("index") //必须用post请求
    @FormUrlEncoded
    Call<NewsResponse> post(@Field("type") String type, @Field("key") String key);

    @GET("index")
    Call<ResponseBody> get(@Query("type") String type, @Query("key") String key);
}
