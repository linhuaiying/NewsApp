package com.example.newsapp.View.showPublishContentView;

import com.example.newsapp.View.IBaseView;
import com.example.newsapp.bean.Commentbean.Comment;

import java.util.List;

public interface IShowPublishContentView extends IBaseView {
    void showCommments(List<Comment> commentList) throws InterruptedException; //返回评论列表
    void showSuccessMsg(String msg); //上传评论后返回的信息
}
