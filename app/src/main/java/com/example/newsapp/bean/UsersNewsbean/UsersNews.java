package com.example.newsapp.bean.UsersNewsbean;

public class UsersNews {
    private String userName;
    private String nickName;
    private String newsContent;
    private String date;
    private String title;
    private String userIcon;
    private int id;
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setNewsContent(String newsContent) {
        this.newsContent = newsContent;
    }
    public String getNewsContent() {
        return newsContent;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserIcon() {
        return this.userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
