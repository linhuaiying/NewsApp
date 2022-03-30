package com.example.newsapp.View.SearchUserView;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;
import java.util.Map;

public interface ISearchUserView extends IBaseView {
    void showMyUser(Map<String, List<MyUser>> map);
}
