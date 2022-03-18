package com.example.newsapp.Model.EditInfoModels;

import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Model.UserModels.RegisterModel;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.bean.Userbean.User;
import com.example.newsapp.bean.Userbean.UserResponse;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditInfoModel {
    String userName = "";
    String nickName = "";
    String sex = "";
    String sign = "";
    String msg = "";
    public EditInfoModel(String userName, String nickName, String sex, String sign) {
        this.userName = userName;
        this.nickName = nickName;
        this.sex = sex;
        this.sign = sign;
    }
    public void loadUserData(EditInfoModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
        onLoadListener.onError(msg);
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(String msg); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public String getData() throws InterruptedException {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UserService userService = retrofit.create(UserService.class); //Retrofit将这个接口进行实现
        Call<ResponseBody> call = userService.updateUser(userName, nickName, sex, sign);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg = response.body().string(); //服务器返回的错误信息
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg;
    }
}
