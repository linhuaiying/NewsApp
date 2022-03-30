package com.example.newsapp.Model.SearchUserModels;

import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Model.SearchModels.SearchModel;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.Service.UsersNewsService;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchUserModel {
    String keyWords;
    String userName;
    public SearchUserModel(String userName, String keyWords) {
        this.keyWords = keyWords;
        this.userName = userName;
    }
    public void loadSearchUserData(SearchUserModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(Map<String, List<MyUser>> map); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public Map<String, List<MyUser>> getData() throws InterruptedException {
        Map<String, List<MyUser>>[] map = new Map[]{new HashMap()};
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UserService userService = retrofit.create(UserService.class);
        Call<Map<String, List<MyUser>>> call = userService.getUserList(userName, keyWords);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<Map<String, List<MyUser>>> response = call.execute();
                    map[0] = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return map[0];
    }
}
