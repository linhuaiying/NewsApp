package com.example.newsapp.View.UserView;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.Userbean.User;

public interface IUserView extends IBaseView {
    //得到后端传来的用户信息
    void getUser(User user);
}
