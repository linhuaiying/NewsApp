package com.example.newsapp.Model.PublishModels;

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
    public PublishModel(List<String> imagPaths) {
        this.imagPaths = imagPaths;
    }
    public void loadImagUrls(PublishModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(sendImags(imagPaths));
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(String[] imagUrls); //成功了就拿到数据
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
            builder.addFormDataPart("file", file.getName(), imageBody);//"imgfile"+i 后台接收图片流的参数名
        }

        List<MultipartBody.Part> parts = builder.build().parts();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.15:8025/")
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
                    imagUrls[0] = content.substring(1, content.length() - 1).split(",");
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
}
