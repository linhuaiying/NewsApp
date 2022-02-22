/**
  * Copyright 2022 bejson.com 
  */
package com.example.newsapp.bean.NewsContentbean;

/**
 * Auto-generated: 2022-02-22 21:40:44
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class NewsContentResponse {

    private String reason;
    private NewsContentResult result;
    private int error_code;
    public void setReason(String reason) {
         this.reason = reason;
     }
     public String getReason() {
         return reason;
     }

    public void setResult(NewsContentResult result) {
         this.result = result;
     }
     public NewsContentResult getResult() {
         return result;
     }

    public void setError_code(int error_code) {
         this.error_code = error_code;
     }
     public int getError_code() {
         return error_code;
     }

}