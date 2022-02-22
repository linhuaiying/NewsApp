package com.example.newsapp.Model.NewsModels;

import android.util.Log;

import com.example.newsapp.Service.NewsService;
import com.example.newsapp.bean.Newsbean.News;
import com.example.newsapp.bean.Newsbean.NewsResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class NewsModel {
    public void loadNewsData(OnLoadListener onLoadListener) throws InterruptedException {

    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(List<News> newsList); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public List<News> getData(String type) throws InterruptedException {
        List<News>[] newsList = new List[]{new ArrayList<>()};
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://v.juhe.cn/toutiao/")
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        NewsService newsService = retrofit.create(NewsService.class); //Retrofit将这个接口进行实现
        Call<NewsResponse> call = newsService.post(type, "4b3b9c355a55f676605532d37cf0d66c");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<NewsResponse> response = call.execute();
                    NewsResponse baseResponse = response.body();
                    newsList[0] = baseResponse.getResult().getData(); //newsList[0]是一个数组
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        Log.d("lifecycle", String.valueOf(newsList[0].size())); //0?
        return newsList[0];
    }
}
