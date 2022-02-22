package com.example.newsapp.Model.UserModels;

import com.example.newsapp.Service.LoginService;
import com.example.newsapp.bean.Userbean.User;
import com.example.newsapp.bean.Userbean.UserResponse;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserModel {
    String username, password, msg;
    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public void loadUserData(OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
        onLoadListener.onError(msg);
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(User user); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public User getData() throws InterruptedException {
        final User[] user = new User[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create()) //添加json转换器build();
                .build();
        LoginService loginService = retrofit.create(LoginService.class); //Retrofit将这个接口进行实现
        Call<UserResponse> call = loginService.post(username, password);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<UserResponse> response = call.execute();
                    UserResponse userResponse =  response.body(); //json自动转化成实体类
                    user[0] = userResponse.getData(); //服务器返回的用户信息
                    msg = userResponse.getErrorMsg(); //服务器返回的错误信息
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join(); //等待子线程返回信息再return
        return user[0];
    }
}
