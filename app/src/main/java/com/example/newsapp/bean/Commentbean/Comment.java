package com.example.newsapp.bean.Commentbean;

public class Comment {
    String userName;
    String nickName;
    String content;
    String userIcon;
    int newsId; //所对应新闻id
    int id; //该条评论的id

    public Comment(String userName, String nickName, String content, int newsId, String userIcon) {
        this.userName = userName;
        this.nickName = nickName;
        this.content = content;
        this.newsId = newsId;
        this.userIcon = userIcon;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return nickName;
    }

    public String getContent() {
        return content;
    }

    public int getNewsId() {
        return newsId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setNewsId(int newsId) {
        this.newsId = newsId;
    }
}
