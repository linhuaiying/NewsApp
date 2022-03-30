package com.example.newsapp.View.MyConcernUserView;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.View.MainView.MyViews.IMyView;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;

public interface IMyConcernUserView extends IMyView {
    void showConcernUsers(List<MyUser> myUserList);
}
