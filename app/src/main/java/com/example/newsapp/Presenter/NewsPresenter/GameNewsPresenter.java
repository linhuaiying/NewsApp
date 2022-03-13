package com.example.newsapp.Presenter.NewsPresenter;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.example.newsapp.Model.NewsModels.GameNewsModel;
import com.example.newsapp.Model.NewsModels.NewsModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.MainView.JuHeNews.INewsView;
import com.example.newsapp.bean.Newsbean.News;

import java.util.List;

public class GameNewsPresenter<T extends INewsView> extends BasePresenter {
    //2.持有Model接口
    NewsModel gameNewsModel = new GameNewsModel();

   @Override
    //3.执行业务逻辑
    public void fetch() throws InterruptedException {
        if(iView.get() != null && gameNewsModel != null) {
            gameNewsModel.loadNewsData(new NewsModel.OnLoadListener() {
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
