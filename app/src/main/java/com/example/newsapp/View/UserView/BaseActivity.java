package com.example.newsapp.View.UserView;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.IBaseView;

public abstract class BaseActivity<T extends BasePresenter, V extends IBaseView> extends AppCompatActivity {
    protected T presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义一个选择表示层的功能
        presenter = createPresenter();
        presenter.attachView((V)this); //让presenter绑定自己
        registerSDK();
    }

    protected  void init() {

    }

    protected void registerSDK() {

    }

    protected abstract T createPresenter(); //绑定presenter

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.distachView();
        unregisterSDK();
    }

    protected  void unregisterSDK() {

    }

}
