package com.example.newsapp.Presenter.NewsContentPresenter;

import com.example.newsapp.Model.NewsContentModels.NewsContentModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.NewsContentView.INewsContentView;


public class NewsContentPresenter<T extends INewsContentView> extends BasePresenter {
    NewsContentModel newsContentModel;

    @Override
    public void fetch(String uniquekey) throws InterruptedException {
        newsContentModel = new NewsContentModel(uniquekey);
        if(iView.get() != null && newsContentModel != null) {
            newsContentModel.loadNewsContentData(new NewsContentModel.OnLoadListener() {
                @Override
                public void onComplete(String newsContent) {
                    ((INewsContentView)iView.get()).showNewsContentView(newsContent); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {
                    ((INewsContentView)iView.get()).showErrorMessage(msg);
                }
            });
        }
    }
}
