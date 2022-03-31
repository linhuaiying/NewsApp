package com.example.newsapp.Model.MyUserModels;

import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.bean.MyUserbean.MyUser;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public void loadConcernUser(MyUserModel.OnLoadConcernUserListener onLoadConcernUserListener) throws InterruptedException {
        onLoadConcernUserListener.onComplete(getConcernUsers());
    }
    public void loadMyFans(MyUserModel.OnLoadFansListener onLoadFansListener) throws InterruptedException {
        onLoadFansListener.onComplete(getMyFans());
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(MyUser myUser); //成功了就拿到数据
        void onError(String msg);
    }
    public interface OnLoadConcernUserListener{ //判断数据是否成功接收
        void onComplete(List<MyUser> myUserList); //成功了就拿到数据
        void onError(String msg);
    }
    public interface OnLoadFansListener{ //判断数据是否成功接收
        void onComplete(Map<String, List<MyUser>> map); //成功了就拿到数据
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
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public List<MyUser> getConcernUsers() throws InterruptedException {
        List<MyUser>[] myUserList = new List[]{new ArrayList<>()};
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UserService userService = retrofit.create(UserService.class); //Retrofit将这个接口进行实现
        Call<List<MyUser>> call = userService.getConcernUser(userName);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<MyUser>> response = call.execute();
                    myUserList[0] = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return myUserList[0];
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public Map<String, List<MyUser>> getMyFans() throws InterruptedException {
        Map<String, List<MyUser>>[] map = new Map[]{new HashMap()};
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UserService userService = retrofit.create(UserService.class); //Retrofit将这个接口进行实现
        Call<Map<String, List<MyUser>>> call = userService.getMyFans(userName);
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
