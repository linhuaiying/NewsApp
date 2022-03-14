package com.example.newsapp.View.MainView.UsersNews;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public interface IUsersNewsView extends IBaseView {
    void showNewsView(List<UsersNews> usersNewsList);
}
