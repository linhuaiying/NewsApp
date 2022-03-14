package com.example.newsapp.Presenter.MyNewsPresenter;

import com.example.newsapp.Model.MyNewsModels.MyNewsModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.MainView.MyNews.IMyNewsView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class MyNewsPresenter<T extends IMyNewsView> extends BasePresenter {
    MyNewsModel myNewsModel;

    @Override
    public void fetch(String username) throws InterruptedException {
        myNewsModel = new MyNewsModel(username);
        if(iView.get() != null && myNewsModel != null) {
            myNewsModel.loadNewsData(new MyNewsModel.OnLoadListener() {
                @Override
                public void onComplete(List<UsersNews> myNewsList) {
                    ((IMyNewsView)iView.get()).showNewsView(myNewsList); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
