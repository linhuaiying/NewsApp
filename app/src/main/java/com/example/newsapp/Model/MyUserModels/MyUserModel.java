package com.example.newsapp.Model.MyUserModels;

import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyUserModel {
    String userName = "";
    public MyUserModel(String userName) {
        this.userName = userName;
    }
    public void loadUserData(MyUserModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(MyUser myUser); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public MyUser getData() throws InterruptedException {
        final MyUser[] myUser = new MyUser[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UserService userService = retrofit.create(UserService.class); //Retrofit将这个接口进行实现
        Call<MyUser> call = userService.getUser(userName);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<MyUser> response = call.execute();
                    myUser[0] = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return myUser[0];
    }
}
