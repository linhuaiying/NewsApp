package com.example.newsapp.Presenter.MyFavNewsPresenter;

import com.example.newsapp.Model.MyFavNewsModels.MyFavNewsModel;
import com.example.newsapp.Model.MyNewsModels.MyNewsModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.MainView.MyFavNews.IMyFavNewsView;
import com.example.newsapp.View.MainView.MyNews.IMyNewsView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class MyFavNewsPresenter<T extends IMyFavNewsView> extends BasePresenter {
    MyFavNewsModel myFavNewsModel;

    @Override
    public void fetch(String username) throws InterruptedException {
        myFavNewsModel = new MyFavNewsModel(username);
        if(iView.get() != null && myFavNewsModel != null) {
            myFavNewsModel.loadFavNewsData(new MyFavNewsModel.OnLoadListener() {
                @Override
                public void onComplete(List<UsersNews> myFavNewsList) {
                    ((IMyFavNewsView)iView.get()).showFavNews(myFavNewsList); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
