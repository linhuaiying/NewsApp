package com.example.newsapp.Presenter.PublishPresenter;

import com.example.newsapp.Model.PublishModels.PublishModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.PublishView.IPublishView;

import java.util.List;

public class PublishPresenter<T extends IPublishView> extends BasePresenter {
    PublishModel publishModel;

    @Override
    public void fetch(List imagPaths) throws InterruptedException {
        publishModel = new PublishModel(imagPaths, null, null);
        if(iView.get() != null && publishModel != null) {
            publishModel.loadImagUrls(new PublishModel.OnLoadListener() {
                @Override
                public void onComplete(String[] imagUrls) throws InterruptedException {
                    ((IPublishView)iView.get()).showImagUrls(imagUrls); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {
                    ((IPublishView)iView.get()).showErrorMessage(msg);
                }
            });
        }
    }

    @Override
    public void fetch(String data, String username) throws InterruptedException {
        publishModel = new PublishModel(null, data, username);
        if(iView.get() != null && publishModel != null) {
            publishModel.loadNewsContent(new PublishModel.OnSendListener() {
                @Override
                public void onComplete(String msg) {
                    ((IPublishView)iView.get()).showSuccessMsg(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {
                    ((IPublishView)iView.get()).showErrorMessage(msg);
                }
            });
        }
    }
}
