package com.example.newsapp.Model.MyNewsModels;

import com.example.newsapp.Service.UsersNewsService;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyNewsModel {
    String username = "";
    public MyNewsModel(String username) {
        this.username = username;
    }
    public void loadNewsData(MyNewsModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(List<UsersNews> myNewsList); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public List<UsersNews> getData() throws InterruptedException {
        List<UsersNews>[] myNewsList = new List[]{new ArrayList<>()};
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.15:8088/")
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UsersNewsService usersNewsService = retrofit.create(UsersNewsService.class); //Retrofit将这个接口进行实现
        Call<List<UsersNews>> call = usersNewsService.getMyNewsList(username);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<UsersNews>> response = call.execute();
                    myNewsList[0] = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return myNewsList[0];
    }
}
