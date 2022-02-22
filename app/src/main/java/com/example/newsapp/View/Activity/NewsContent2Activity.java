package com.example.newsapp.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.newsapp.Presenter.NewsContentPresenter.NewsContentPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.UserView.BaseActivity;
import com.example.newsapp.bean.Userbean.User;

public class NewsContent2Activity extends BaseActivity<NewsContentPresenter, INewsContentView> implements INewsContentView{

    TextView textView;
    String uniquekey;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content2);
        textView = findViewById(R.id.newscontent);
        uniquekey = getIntent().getStringExtra("uniquekey");
        try {
            presenter.fetch(uniquekey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected NewsContentPresenter createPresenter() {
        return new NewsContentPresenter();
    }

    public static void actionStart(Context context, String uniquekey) {
        Intent intent = new Intent(context, NewsContent2Activity.class);
        intent.putExtra("uniquekey", uniquekey);
        context.startActivity(intent);
    }

    @Override
    public void showNewsContentView(String data) {
        textView.setText(data);
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
