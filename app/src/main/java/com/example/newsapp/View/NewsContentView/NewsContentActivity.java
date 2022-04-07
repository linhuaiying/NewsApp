package com.example.newsapp.View.NewsContentView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.NewsContentPresenter.NewsContentPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.showPublishContentView.showPublishContentActivity;
import com.example.newsapp.adapter.CommentAdapter;
import com.example.newsapp.bean.Commentbean.Comment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class NewsContentActivity extends BaseActivity<NewsContentPresenter, INewsContentView> implements INewsContentView{

    WebView webView;
    TextView titleTex;
    TextView editText;
    EditText commentText;
    TextView allComments;
    TextView publish;
    TextView defaultText;
    Button backBtn;
    Button favBtn;
    Button likeBtn;
    boolean isFav = false;
    boolean isLike = false;
    String uniquekey;
    String title;
    String userName;
    String nickName;
    String userIcon;
    SlidingUpPanelLayout slidingUpPanelLayout;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> commentList = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content);
        getWindow().setStatusBarColor(Color.argb(0, 211,211,211));
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
        backBtn = findViewById(R.id.back);
        favBtn = findViewById(R.id.fav);
        likeBtn = findViewById(R.id.like);
        titleTex = findViewById(R.id.title);
        allComments = findViewById(R.id.allComments);
        defaultText = findViewById(R.id.default_text);
        publish = findViewById(R.id.publish);
        uniquekey = getIntent().getStringExtra("uniquekey");
        title = getIntent().getStringExtra("title");
        userName = SaveAccount.getUserInfo(this).get("userName");
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        userIcon = SaveAccount.getUserInfo(this).get("userIcon");
        titleTex.setText(title);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        commentText = findViewById(R.id.comment);
        editText = findViewById(R.id.edittext);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(1f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                commentText.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) commentText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment(userName, nickName, commentText.getText().toString(), -1, userIcon);
                commentList.add(comment);
                commentAdapter = new CommentAdapter(commentList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewsContentActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(commentAdapter);
                commentAdapter.notifyItemInserted(commentList.size() - 1);
                recyclerView.scrollToPosition(commentList.size() - 1);
                commentText.setText("");
                MyToast.toast("评论成功！");
                defaultText.setVisibility(View.GONE);
            }
        });
        allComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(1f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsContentActivity.this.finish();
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isFav) {
                    favBtn.setBackgroundResource(R.drawable.fav);
                    MyToast.toast("收藏成功！");
                    isFav = true;
                } else {
                    favBtn.setBackgroundResource(R.drawable.cancel_fav);
                    MyToast.toast("取消收藏");
                    isFav = false;
                }
            }
        });
        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isLike) {
                    likeBtn.setBackgroundResource(R.drawable.like);
                    isLike = true;
                }
                else {
                    likeBtn.setBackgroundResource(R.drawable.unlike);
                    isLike = false;
                }
            }
        });
    }

    @Override
    protected NewsContentPresenter createPresenter() {
        return new NewsContentPresenter();
    }

    public static void actionStart(Context context, String uniquekey, String title) {
        Intent intent = new Intent(context, NewsContentActivity.class);
        intent.putExtra("uniquekey", uniquekey);
        intent.putExtra("title", title);
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
