package com.example.newsapp.Presenter.ConcernNewsPresenter;

import com.example.newsapp.Model.ConcernNewsModels.ConcernNewsModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.NewsView.ConcernNews.IConcernNewsView;
import com.example.newsapp.bean.ConcernNewsbean.ConcernNews;

import java.util.List;

public class ConcernNewsPresenter<T extends IConcernNewsView> extends BasePresenter {
    ConcernNewsModel concernNewsModel = new ConcernNewsModel();

    @Override
    public void fetch() throws InterruptedException {
        if(iView.get() != null && concernNewsModel != null) {
            concernNewsModel.loadNewsData(new ConcernNewsModel.OnLoadListener() {
                @Override
                public void onComplete(List<ConcernNews> concernNewsList) {
                    ((IConcernNewsView)iView.get()).showNewsView(concernNewsList); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
