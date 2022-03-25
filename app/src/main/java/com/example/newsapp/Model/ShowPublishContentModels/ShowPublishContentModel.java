package com.example.newsapp.Model.ShowPublishContentModels;

import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Service.CommentService;
import com.example.newsapp.Service.FavNewsService;
import com.example.newsapp.Service.UsersNewsService;
import com.example.newsapp.bean.Commentbean.Comment;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ShowPublishContentModel {
    Comment comment;
    int newsId;
    String userName;
    public ShowPublishContentModel(Comment comment, int newsId, String userName) {
        this.comment = comment;
        this.newsId = newsId;
        this.userName = userName;
    }
    public void loadComments(ShowPublishContentModel.OnLoadListener onLoadListener) throws InterruptedException {
        onLoadListener.onComplete(getComments(newsId));
    }
    public void loadComment(OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(sendComment(comment));
    }
    public void deletNews(OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(deleteNews(newsId));
    }
    public void addFav(OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(addFav(userName, newsId));
    }
    public void deleteFav(OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(deleteFav(userName, newsId));
    }
    public void getFav(OnSendListener onSendListener) throws InterruptedException {
        onSendListener.onComplete(getFav(userName, newsId));
    }
    public interface OnLoadListener{ //判断数据是否成功接收
        void onComplete(List<Comment> commentList) throws InterruptedException; //成功了就拿到数据
        void onError(String msg);
    }
    public interface OnSendListener{ //判断数据是否成功上传
        void onComplete(String msg); //成功了就拿到返回值
        void onError(String msg);
    }
    public String sendComment(Comment comment) throws InterruptedException {
        final String[] msg = new String[1];
        Gson gson = new Gson();
        String json = gson.toJson(comment);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        CommentService  commentService = retrofit.create(CommentService.class); //Retrofit将这个接口进行实现
        RequestBody body= RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Call<ResponseBody> call = commentService.sendComment(body);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg[0];
    }
    public List<Comment> getComments(int newsId) throws InterruptedException {
        final List<Comment>[] commentList = new List[]{null};
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        CommentService  commentService = retrofit.create(CommentService.class); //Retrofit将这个接口进行实现
        Call<List<Comment>> call = commentService.getComments(newsId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<List<Comment>> response = call.execute();
                    commentList[0] = response.body();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return commentList[0];
    }
    public String deleteNews(int newsId) throws InterruptedException {
        final String[] msg = new String[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        UsersNewsService usersNewsService = retrofit.create(UsersNewsService.class);
        Call<ResponseBody> call = usersNewsService.deleteNews(newsId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg[0];
    }
    public String addFav(String userName, int newsId) throws InterruptedException {
        final String[] msg = new String[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        FavNewsService favNewsService = retrofit.create(FavNewsService.class);
        Call<ResponseBody> call = favNewsService.addFav(userName, newsId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg[0];
    }
    public String deleteFav(String userName, int newsId) throws InterruptedException {
        final String[] msg = new String[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        FavNewsService favNewsService = retrofit.create(FavNewsService.class);
        Call<ResponseBody> call = favNewsService.deleteFav(userName, newsId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg[0];
    }
    public String getFav(String userName, int newsId) throws InterruptedException {
        final String[] msg = new String[1];
        Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                .build();
        FavNewsService favNewsService = retrofit.create(FavNewsService.class);
        Call<ResponseBody> call = favNewsService.getFav(userName, newsId);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    Response<ResponseBody> response = call.execute();
                    msg[0] = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        return msg[0];
    }
}
