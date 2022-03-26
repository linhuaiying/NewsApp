package com.example.newsapp.View.MainView.MyViews;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.MyUserbean.MyUser;

public interface IMyView extends IBaseView {
    void showMyUser(MyUser myUser);
    void showImagUrl(String imagUrl);
}
