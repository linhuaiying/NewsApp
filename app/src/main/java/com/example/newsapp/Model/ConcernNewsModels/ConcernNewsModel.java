package com.example.newsapp.Model.ConcernNewsModels;

import android.util.Log;

import com.example.newsapp.Model.NewsModels.NewsModel;
import com.example.newsapp.Service.ConcernNewsService;
import com.example.newsapp.Service.NewsService;
import com.example.newsapp.bean.ConcernNewsbean.ConcernNews;
import com.example.newsapp.bean.Newsbean.News;
import com.example.newsapp.bean.Newsbean.NewsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ConcernNewsModel {
    public void loadNewsData(ConcernNewsModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData());
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(List<ConcernNews> concernNewsList); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public List<ConcernNews> getData() throws InterruptedException {
        List<ConcernNews>[] concernNewsList = new List[]{new ArrayList<>()};
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://192.168.43.15:8087/")
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        ConcernNewsService concernNewsService = retrofit.create(ConcernNewsService.class); //Retrofit将这个接口进行实现
        Call<List<ConcernNews>> call = concernNewsService.getConcernNewsList();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<ConcernNews>> response = call.execute();
                    concernNewsList[0] = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return concernNewsList[0];
    }
}
