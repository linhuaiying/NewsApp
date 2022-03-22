package com.example.newsapp.Service;

import com.example.newsapp.bean.Commentbean.Comment;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CommentService {
    @Headers({"Content-Type: application/json","Accept: application/json"})//需要添加头
    @POST("comment/sendComment") //必须用post请求
    Call<ResponseBody> sendComment(@Body RequestBody comment);

    @POST("comment/getComments") //必须用post请求
    @FormUrlEncoded
    Call<List<Comment>> getComments(@Field("newsId") int newsId);
}
