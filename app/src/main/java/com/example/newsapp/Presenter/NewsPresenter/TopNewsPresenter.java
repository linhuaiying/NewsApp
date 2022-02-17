package com.example.newsapp.Presenter.NewsPresenter;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.newsapp.Model.NewsModels.NewsModel;
import com.example.newsapp.Model.NewsModels.TopNewsModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.NewsView.INewsView;
import com.example.newsapp.bean.Newsbean.News;

import java.util.List;

public class TopNewsPresenter<T extends INewsView> extends BasePresenter {
    //2.持有Model接口
    NewsModel topNewsModel = new TopNewsModel();

    @Override
    //3.执行业务逻辑
    public void fetch() throws InterruptedException {
        if(iView.get() != null && topNewsModel != null) {
            topNewsModel.loadNewsData(new NewsModel.OnLoadListener() {
                @Override
                public void onComplete(List<News> newsList) {
                    ((INewsView)iView.get()).showNewsView(newsList); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    @Override
    public void onCreate(LifecycleOwner lifecycleOwner) {
        super.onCreate(lifecycleOwner);
        Log.d("lifecycle", "oncreate");
    }
}
