package com.example.newsapp.Presenter.SearchUserPresenter;

import com.example.newsapp.Model.SearchUserModels.SearchUserModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.SearchUserView.ISearchUserView;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;
import java.util.Map;

public class SearchUserPresenter<T extends ISearchUserView> extends BasePresenter {
    SearchUserModel searchUserModel;

    @Override
    public void fetch(String userName, String keyWords) throws InterruptedException {
        searchUserModel = new SearchUserModel(userName, keyWords);
        if(iView.get() != null && searchUserModel != null) {
            searchUserModel.loadSearchUserData(new SearchUserModel.OnLoadListener() {
                @Override
                public void onComplete(Map<String, List<MyUser>> map) {
                    ((ISearchUserView)iView.get()).showMyUser(map); //从model层拿到数据传到view层
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
