/**
 * Copyright 2021 bejson.com
 */
package com.example.newsapp.bean.Userbean;;

/**
 * Auto-generated: 2021-06-27 18:14:55
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UserResponse {

    private User data;
    private int errorCode;
    private String errorMsg;
    public void setData(User data) {
        this.data = data;
    }
    public User getData() {
        return data;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    public String getErrorMsg() {
        return errorMsg;
    }

}