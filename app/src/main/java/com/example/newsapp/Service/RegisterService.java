package com.example.newsapp.Service;

import com.example.newsapp.bean.Userbean.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RegisterService {
    @POST("user/register") //必须用post请求
    @FormUrlEncoded
    Call<UserResponse> post(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);
}
