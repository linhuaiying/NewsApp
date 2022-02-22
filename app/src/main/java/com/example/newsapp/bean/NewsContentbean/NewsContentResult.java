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
public class NewsContentResult {

    private String uniquekey;
    private NewsContentDetail detail;
    private String content;
    public void setUniquekey(String uniquekey) {
         this.uniquekey = uniquekey;
     }
     public String getUniquekey() {
         return uniquekey;
     }

    public void setDetail(NewsContentDetail detail) {
         this.detail = detail;
     }
     public NewsContentDetail getDetail() {
         return detail;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

}