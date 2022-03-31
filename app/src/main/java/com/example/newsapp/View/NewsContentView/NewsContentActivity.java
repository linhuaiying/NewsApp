package com.example.newsapp.View.NewsContentView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.newsapp.Presenter.NewsContentPresenter.NewsContentPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.BaseActivity;

public class NewsContentActivity extends BaseActivity<NewsContentPresenter, INewsContentView> implements INewsContentView{

    WebView webView;
    String uniquekey;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        init();
        try {
            presenter.fetch(uniquekey);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        webView = findViewById(R.id.newscontent);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        uniquekey = getIntent().getStringExtra("uniquekey");
    }

    @Override
    protected NewsContentPresenter createPresenter() {
        return new NewsContentPresenter();
    }

    public static void actionStart(Context context, String uniquekey) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("uniquekey", uniquekey);
        context.startActivity(intent);
    }

    @Override
    public void showNewsContentView(String data) {
       webView.loadDataWithBaseURL(null, data, "text/html" , "utf-8", null);
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
