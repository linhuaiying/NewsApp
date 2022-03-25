package com.example.newsapp.Presenter.ShowPublishContentPresenter;

import com.example.newsapp.Model.ShowPublishContentModels.ShowPublishContentModel;
import com.example.newsapp.Presenter.BasePresenter;
import com.example.newsapp.View.showPublishContentView.IShowPublishContentView;
import com.example.newsapp.bean.Commentbean.Comment;

import java.util.List;

public class ShowPublishContentPresenter<T extends IShowPublishContentView> extends BasePresenter {
    ShowPublishContentModel showPublishContentModel;

    @Override
    public void fetch(Comment comment) throws InterruptedException {
        showPublishContentModel = new ShowPublishContentModel(comment, -1, "");
        if (iView.get() != null && showPublishContentModel != null) {
            showPublishContentModel.loadComment(new ShowPublishContentModel.OnSendListener() {
                @Override
                public void onComplete(String msg) {
                    ((IShowPublishContentView)iView.get()).showSuccessMsg(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    @Override
    public void fetch(int newsId) throws InterruptedException {
        showPublishContentModel = new ShowPublishContentModel(null, newsId, "");
        if (iView.get() != null && showPublishContentModel != null) {
            showPublishContentModel.loadComments(new ShowPublishContentModel.OnLoadListener() {
                @Override
                public void onComplete(List<Comment> commentList) throws InterruptedException {
                    ((IShowPublishContentView)iView.get()).showCommments(commentList); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    public void deleteNews(int newsId) throws InterruptedException {
        showPublishContentModel = new ShowPublishContentModel(null, newsId, "");
        if (iView.get() != null && showPublishContentModel != null) {
            showPublishContentModel.deletNews(new ShowPublishContentModel.OnSendListener() {
                @Override
                public void onComplete(String msg) {
                    ((IShowPublishContentView)iView.get()).showDeleteMsg(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    public void getFav(String userName, int newsId) throws InterruptedException {
        showPublishContentModel = new ShowPublishContentModel(null, newsId, userName);
        if (iView.get() != null && showPublishContentModel != null) {
            showPublishContentModel.getFav(new ShowPublishContentModel.OnSendListener() {
                @Override
                public void onComplete(String msg) {
                    ((IShowPublishContentView)iView.get()).showFav(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    public void addFavNews(String userName, int newsId) throws InterruptedException {
        showPublishContentModel = new ShowPublishContentModel(null, newsId, userName);
        if (iView.get() != null && showPublishContentModel != null) {
            showPublishContentModel.addFav(new ShowPublishContentModel.OnSendListener() {
                @Override
                public void onComplete(String msg) {
                    ((IShowPublishContentView)iView.get()).setFav(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }

    public void deleteFavNews(String userName, int newsId) throws InterruptedException {
        showPublishContentModel = new ShowPublishContentModel(null, newsId, userName);
        if (iView.get() != null && showPublishContentModel != null) {
            showPublishContentModel.deleteFav(new ShowPublishContentModel.OnSendListener() {
                @Override
                public void onComplete(String msg) {
                    ((IShowPublishContentView)iView.get()).setFav(msg); //从model层获取数据，给view传递用户信息
                }

                @Override
                public void onError(String msg) {

                }
            });
        }
    }
}
