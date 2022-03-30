package com.example.newsapp.Service;

import com.example.newsapp.bean.MyUserbean.MyUser;
import com.example.newsapp.bean.Userbean.UserResponse;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {
    @POST("user/register") //必须用post请求
    @FormUrlEncoded
    Call<UserResponse> register(@Field("username") String username, @Field("password") String password, @Field("repassword") String repassword);

    @POST("user/login") //必须用post请求
    @FormUrlEncoded
    Call<UserResponse> login(@Field("username") String username, @Field("password") String password);

    @POST("user/save") //必须用post请求
    @FormUrlEncoded
    Call<ResponseBody> saveUser(@Field("username") String username, @Field("password") String password);

    @POST("user/update") //必须用post请求
    @FormUrlEncoded
    Call<ResponseBody> updateUser(@Field("username") String username, @Field("nickname") String nickName, @Field("sex") String sex, @Field("sign") String sign, @Field("userIcon") String userIcon);

    @POST("user/get") //必须用post请求
    @FormUrlEncoded
    Call<MyUser> getUser(@Field("username") String username);

    @POST("user/getUsers") //必须用post请求
    @FormUrlEncoded
    Call<Map<String, List<MyUser>>> getUserList(@Field("userName") String userName, @Field("keyWords") String keyWords);

    @POST("user/concern") //必须用post请求
    @FormUrlEncoded
    Call<ResponseBody> concernUser(@Field("username") String userName, @Field("concernUsername") String concernUsername);

    @POST("user/getConcernUser") //必须用post请求
    @FormUrlEncoded
    Call<List<MyUser>> getConcernUser(@Field("username") String userName);
}
