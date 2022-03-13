package com.example.newsapp.View.MainView;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.IBaseView;

public abstract class BaseFragment<T extends BasePresenter, V extends IBaseView> extends Fragment {
    protected T presenter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //定义一个选择表示层的功能
        presenter = createPresenter();
        presenter.attachView((V)this);
        registerSDK();
        init();
    }

    protected  void init() {

    }

    protected void registerSDK() {

    }

    protected abstract T createPresenter();

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.distachView();
        unregisterSDK();
    }

    protected  void unregisterSDK() {

    }


}
