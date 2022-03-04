package com.example.newsapp.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.newsapp.R;
import com.example.newsapp.View.NewsContentView.NewsContentActivity;

public class showPublishContentActivity extends AppCompatActivity {

    WebView webView;
    String data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_publish_content);
        webView = findViewById(R.id.newscontent);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        data = getIntent().getStringExtra("data");
        webView.loadDataWithBaseURL(null, data, "text/html" , "utf-8", null);
    }

    public static void actionStart(Context context, String data) {
        Intent intent = new Intent(context, showPublishContentActivity.class);
        intent.putExtra("data", data);
        context.startActivity(intent);
    }
}
