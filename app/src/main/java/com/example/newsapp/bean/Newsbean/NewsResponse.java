package com.example.newsapp.bean.Newsbean; /**
  * Copyright 2021 bejson.com 
  */

/**
 * Auto-generated: 2021-06-16 15:46:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class NewsResponse {

    private String reason;
    private NewsResult result;
    private int error_code;
    public void setReason(String reason) {
         this.reason = reason;
     }
     public String getReason() {
         return reason;
     }

    public void setResult(NewsResult result) {
         this.result = result;
     }
     public NewsResult getResult() {
         return result;
     }

    public void setError_code(int error_code) {
         this.error_code = error_code;
     }
     public int getError_code() {
         return error_code;
     }

}