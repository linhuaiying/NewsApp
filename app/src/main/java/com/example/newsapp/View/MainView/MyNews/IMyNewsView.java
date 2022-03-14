package com.example.newsapp.View.MainView.MyNews;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public interface IMyNewsView extends IBaseView {
    void showNewsView(List<UsersNews> myNewsList);
}
