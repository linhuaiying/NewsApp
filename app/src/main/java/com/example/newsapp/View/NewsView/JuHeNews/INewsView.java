package com.example.newsapp.View.NewsView.JuHeNews;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.Newsbean.News;

import java.util.List;

public interface INewsView extends IBaseView {
    //显示文字、图片
    void showNewsView(List<News> newsList);
    //加载进度条
    //加载动画
    //....
}
