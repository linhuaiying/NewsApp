package com.example.newsapp.Presenter.MyUserPresenter;

import com.example.newsapp.Model.MyUserModels.MyUserModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.MainView.MyViews.IMyView;
import com.example.newsapp.View.MyConcernUserView.IMyConcernUserView;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;

public class MyUserPresenter <T extends IMyView> extends BasePresenter {
    MyUserModel myUserModel;

    @Override
    public void fetch(String userName) throws InterruptedException {
        myUserModel = new MyUserModel(userName);
        if(iView.get() != null && myUserModel != null) {
            myUserModel.loadUserData(new MyUserModel.OnLoadListener() {
                @Override
                public void onComplete(MyUser myUser) {
                    ((IMyView)iView.get()).showMyUser(myUser); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    public void getConcernUsers(String userName) throws InterruptedException {
        myUserModel = new MyUserModel(userName);
        if(iView.get() != null && myUserModel != null) {
           myUserModel.loadConcernUser(new MyUserModel.OnLoadConcernUserListener() {
               @Override
               public void onComplete(List<MyUser> myUserList) {
                   ((IMyConcernUserView)iView.get()).showConcernUsers(myUserList); //从model层获取数据，给view传递用户信息
               }

               @Override
               public void onError(String msg) {

               }
           });
        }
    }
}
