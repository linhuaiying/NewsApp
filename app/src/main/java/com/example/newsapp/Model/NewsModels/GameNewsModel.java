package com.example.newsapp.Model.NewsModels;

public class GameNewsModel extends NewsModel {
    @Override
    public void loadNewsData(NewsModel.OnLoadListener onLoadListener) throws InterruptedException {
         onLoadListener.onComplete(getData("youxi"));
    }
}
