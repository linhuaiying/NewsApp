package com.example.newsapp.View.SearchView;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public interface ISearchView extends IBaseView {
    void showSearchNews(List<UsersNews> userNewsList);
}
