package com.example.newsapp.View.MainView.MyFavNews;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public interface IMyFavNewsView extends IBaseView {
    void showFavNews(List<UsersNews> myFavNewsList);
}
