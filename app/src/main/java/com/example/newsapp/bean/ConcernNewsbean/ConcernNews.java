package com.example.newsapp.bean.ConcernNewsbean;

public class ConcernNews {
    private String userName;
    private String newsContent;
    private String date;
    private String title;
    private int id;
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
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

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
}
