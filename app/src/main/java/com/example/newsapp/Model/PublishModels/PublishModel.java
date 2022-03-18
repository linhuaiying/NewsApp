package com.example.newsapp.Model.PublishModels;

import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Service.PublishService;

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

public class PublishModel {
    List<String> imagPaths;
    String newsContent = "";
    String username = "";
    String title = "";
    public PublishModel(List<String> imagPaths, String title, String newsContent, String username) {
        this.imagPaths = imagPaths;
        this.newsContent = newsContent;
        this.username = username;
        this.title =  title;
    }
    public void loadImagUrls(PublishModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(sendImags(imagPaths));
    }
    public void loadNewsContent(OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(sendNewsContent(title, newsContent, username));
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(String[] imagUrls) throws InterruptedException; //成功了就拿到数据
        void onError(String msg);
    }
    public interface OnSendListener{ //判断数据是否成功上传
        void onComplete(String msg); //成功了就拿到返回值
        void onError(String msg);
    }
    //向服务器发送多张图片,获取图片网络地址
    public String[] sendImags(List<String> imagPaths) throws InterruptedException {
        final String[][] imagUrls = new String[1][1];
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        //多张图片
        for (int i = 0; i < imagPaths.size(); i++) {
            File file = new File(imagPaths.get(i));//filePath 图片地址
            RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), imageBody);
        }
        if(imagPaths.size() == 0) builder.addFormDataPart("", "");
        List<MultipartBody.Part> parts = builder.build().parts();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        PublishService publishService = retrofit.create(PublishService.class); //Retrofit将这个接口进行实现
        Call<ResponseBody> call = publishService.uploadImags(parts);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    String content = response.body().string();
                    if(!content.equals("")) imagUrls[0] = content.substring(1, content.length() - 1).split(",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return imagUrls[0];
    }
    public String sendNewsContent(String title, String newsContent, String username) throws InterruptedException {
        final String[] msg = {""};
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        PublishService publishService = retrofit.create(PublishService.class); //Retrofit将这个接口进行实现
        Call<ResponseBody> call = publishService.uploadNewsContent(title, newsContent, username);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg[0];
    }
}
