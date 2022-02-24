package com.example.newsapp.Model.UserModels;

import android.util.Log;

import com.example.newsapp.Service.UserService;
import com.example.newsapp.bean.Userbean.User;
import com.example.newsapp.bean.Userbean.UserResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterModel {
    String username, password, repassword, msg;
    public RegisterModel(String username, String password, String repassword) {
        this.username = username;
        this.password = password;
        this.repassword = repassword;
    }
    public void loadRegisterData(RegisterModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onError(getData());
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(User user); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public String getData() throws InterruptedException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.wanandroid.com/")
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UserService registerService = retrofit.create(UserService.class); //Retrofit将这个接口进行实现
        Call<UserResponse> call = registerService.register(username, password, repassword);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<UserResponse> response = call.execute();
                    UserResponse userResponse =  response.body();
                    msg = userResponse.getErrorMsg(); //服务器返回的错误信息
                    if(msg.equals("")) {
                        //将用户信息保存到自己写的后端
                        saveData();
                    }
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg;
    }
    public void saveData() throws InterruptedException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://172.24.72.70:8080/") //ipv4地址
                .build();
        UserService registerService = retrofit.create(UserService.class); //Retrofit将这个接口进行实现
        Call<ResponseBody> call = registerService.saveUser(username, password);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    Log.d("RegisterModel", response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
    }
}
