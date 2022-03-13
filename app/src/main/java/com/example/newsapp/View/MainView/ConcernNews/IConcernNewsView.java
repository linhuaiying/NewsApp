package com.example.newsapp.View.MainView.ConcernNews;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.ConcernNewsbean.ConcernNews;

import java.util.List;

public interface IConcernNewsView extends IBaseView {
    void showNewsView(List<ConcernNews> concernNewsList);
}
