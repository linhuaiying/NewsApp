package com.example.newsapp.Service;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.adapter.rxjava3.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface PublishService {
    @Multipart
    @POST("/upload")
    Call<ResponseBody> uploadImags(@Part List<MultipartBody.Part> partList);

    @POST("news/sendNewsContent") //必须用post请求
    @FormUrlEncoded
    Call<ResponseBody> uploadNewsContent(@Field("title") String title, @Field("newscontent") String newsContent, @Field("username") String username);
}
