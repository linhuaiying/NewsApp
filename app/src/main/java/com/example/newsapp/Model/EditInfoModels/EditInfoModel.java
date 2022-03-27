package com.example.newsapp.Model.EditInfoModels;

import com.example.newsapp.Application.MyApplication;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Model.UserModels.RegisterModel;
import com.example.newsapp.Service.PublishService;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.bean.Userbean.User;
import com.example.newsapp.bean.Userbean.UserResponse;

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

public class EditInfoModel {
    String userName = "";
    String nickName = "";
    String sex = "";
    String sign = "";
    String msg = "";
    String userIcon = "";
    String imagPath = "";
    public EditInfoModel(String userName, String nickName, String sex, String sign, String imagPath) throws InterruptedException {
        this.userName = userName;
        this.nickName = nickName;
        this.sex = sex;
        this.sign = sign;
        this.imagPath = imagPath; //获取本地图片地址
        String localUserIcon = SaveAccount.getUserInfo(MyApplication.getContext()).get("userIcon");
        if(localUserIcon == null) localUserIcon = "";
        //userIcon不能为null，不然后端会报400错误
        this.userIcon = imagPath == null ? localUserIcon : getImagUrl(imagPath); //生成网络地址
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
        Call<ResponseBody> call = userService.updateUser(userName, nickName, sex, sign, userIcon);
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
        if(msg.equals("success")) return userIcon;
        return msg;
    }
    public String getImagUrl(String imagPath) throws InterruptedException {
        final String[] imagUrl = new String[1];
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        File file = new File(imagPath);//filePath 图片地址
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart("file", file.getName(), imageBody);
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
