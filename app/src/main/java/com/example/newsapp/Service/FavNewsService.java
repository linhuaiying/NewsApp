package com.example.newsapp.Service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface FavNewsService {
    @POST("fav")
    @FormUrlEncoded
    Call<ResponseBody> addFav(@Field("username") String userName, @Field("newsId") int newsId);

    @POST("deletefav")
    @FormUrlEncoded
    Call<ResponseBody> deleteFav(@Field("username") String userName, @Field("newsId") int newsId);

    @POST("getfav")
    @FormUrlEncoded
    Call<ResponseBody> getFav(@Field("username") String username, @Field("newsId") int newsId);
}
