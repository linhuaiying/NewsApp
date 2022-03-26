package com.example.newsapp.Model.MyUserModels;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Service.PublishService;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyUserModel {
    String userName = "";
    String imagPath = "";
    public MyUserModel(String userName, String imagPath) {
        this.userName = userName;
        this.imagPath = imagPath;
    }
    public void loadUserData(MyUserModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
    }
    public void loadImagUrl(MyUserModel.OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(getImagUrl(imagPath, userName));
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(MyUser myUser); //成功了就拿到数据
        void onError(String msg);
    }
    public interface OnSendListener{
        void onComplete(String imagUrl); //成功了就拿到数据
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
    public String getImagUrl(String imagPath, String userName) throws InterruptedException {
        final String[] imagUrl = new String[1];
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        File file = new File(imagPath);//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);
        builder.addFormDataPart("username", userName);
        List<MultipartBody.Part> parts = builder.build().parts();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        PublishService publishService = retrofit.create(PublishService.class); //Retrofit将这个接口进行实现
        Call<ResponseBody> call = publishService.uploadImag(parts);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    imagUrl[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return imagUrl[0];
    }
}
