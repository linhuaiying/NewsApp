package com.example.newsapp.View.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

public class showPublishContentActivity extends AppCompatActivity {

    WebView webView;
    TextView title;
    String data;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_publish_content);
        getWindow().setStatusBarColor(Color.argb(0, 211,211,211));
        webView = findViewById(R.id.newscontent);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        title = findViewById(R.id.title);
        data = getIntent().getStringExtra("data");
        title.setText(getIntent().getStringExtra("title"));
        webView.loadDataWithBaseURL(null, data, "text/html" , "utf-8", null);
    }

    public static void actionStart(Context context, UsersNews usersNews) {
        Intent intent = new Intent(context, showPublishContentActivity.class);
        intent.putExtra("data", usersNews.getNewsContent());
        intent.putExtra("title", usersNews.getTitle());
        context.startActivity(intent);
    }
}
