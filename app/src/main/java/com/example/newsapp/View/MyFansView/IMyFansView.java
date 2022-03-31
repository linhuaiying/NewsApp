package com.example.newsapp.View.MyFansView;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;
import java.util.Map;

public interface IMyFansView extends IBaseView {
    void showFansList(Map<String, List<MyUser>> map);
}
