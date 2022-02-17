package com.example.newsapp.bean.Newsbean; /**
  * Copyright 2021 bejson.com 
  */
import java.util.List;

/**
 * Auto-generated: 2021-06-16 15:46:43
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class NewsResult {

    private String stat;
    private List<News> data;
    private String page;
    private String pageSize;
    public void setStat(String stat) {
         this.stat = stat;
     }
     public String getStat() {
         return stat;
     }

    public void setData(List<News> data) {
         this.data = data;
     }
     public List<News> getData() {
         return data;
     }

    public void setPage(String page) {
         this.page = page;
     }
     public String getPage() {
         return page;
     }

    public void setPageSize(String pageSize) {
         this.pageSize = pageSize;
     }
     public String getPageSize() {
         return pageSize;
     }

}