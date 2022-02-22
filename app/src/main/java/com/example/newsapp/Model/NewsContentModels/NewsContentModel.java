package com.example.newsapp.Model.NewsContentModels;

import com.example.newsapp.Service.NewsService;
import com.example.newsapp.bean.NewsContentbean.NewsContentResponse;
import com.example.newsapp.bean.Newsbean.News;


import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsContentModel {
    String uniquekey;
    public NewsContentModel(String uniquekey) {
        this.uniquekey = uniquekey;
    }
    public void loadNewsContentData(OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getData(uniquekey));
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(String newsContent); //成功了就拿到数据
        void onError(String msg);
    }
    //该方法想象成数据是从网络或者数据库或者服务、、、地方来的
    public String getData(String uniquekey) throws InterruptedException {
        final String[] newsContent = new String[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://v.juhe.cn/toutiao/")
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        NewsService newsService = retrofit.create(NewsService.class); //Retrofit将这个接口进行实现
        Call<NewsContentResponse> call = newsService.getNewsContent(uniquekey, "4b3b9c355a55f676605532d37cf0d66c");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<NewsContentResponse> response = call.execute();
                    NewsContentResponse baseResponse = response.body();
                    newsContent[0] = baseResponse.getResult().getContent(); //newsList[0]是一个数组
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return newsContent[0];
    }
}
