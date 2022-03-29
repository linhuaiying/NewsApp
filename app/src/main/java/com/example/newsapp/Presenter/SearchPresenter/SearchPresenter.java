package com.example.newsapp.Presenter.SearchPresenter;

import com.example.newsapp.Model.SearchModels.SearchModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.SearchView.ISearchView;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class SearchPresenter<T extends ISearchView> extends BasePresenter {
    SearchModel searchModel;

    @Override
    public void fetch(String keyWords) throws InterruptedException {
        searchModel = new SearchModel(keyWords);
        if(iView.get() != null && searchModel != null) {
            searchModel.loadSearchNewsData(new SearchModel.OnLoadListener() {
                @Override
                public void onComplete(List<UsersNews> usersNewsList) {
                    ((ISearchView)iView.get()).showSearchNews(usersNewsList); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
