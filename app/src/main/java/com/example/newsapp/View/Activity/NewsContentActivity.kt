package com.example.newsapp.View.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.example.newsapp.R

class NewsContentActivity : AppCompatActivity() {
   lateinit var webView : WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_content)
        webView = findViewById(R.id.webview)
        val url = intent.getStringExtra("url")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        if (url != null) {
            webView.loadUrl(url)
        }
    }
    companion object {
        fun actionStart(context: Context, data: String) {
            val intent: Intent = Intent()
            intent.setClass(context, NewsContentActivity().javaClass)
            intent.putExtra("url", data)
            context.startActivity(intent)
        }
    }
}
