package com.example.newsapp.Presenter.UsersNewsPresenter;

import com.example.newsapp.Model.UsersNewsModels.UsersNewsModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.MainView.UsersNews.IUsersNewsView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class UsersNewsPresenter<T extends IUsersNewsView> extends BasePresenter {
    UsersNewsModel usersNewsModel = new UsersNewsModel();

    @Override
    public void fetch() throws InterruptedException {
        if(iView.get() != null && usersNewsModel != null) {
            usersNewsModel.loadNewsData(new UsersNewsModel.OnLoadListener() {
                @Override
                public void onComplete(List<UsersNews> usersNewsList) {
                    ((IUsersNewsView)iView.get()).showNewsView(usersNewsList); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
