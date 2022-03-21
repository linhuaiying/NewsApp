package com.example.newsapp.View.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsapp.R;
import com.example.newsapp.adapter.CommentAdapter;
import com.example.newsapp.bean.Commentbean.Comment;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class showPublishContentActivity extends AppCompatActivity {

    WebView newsWebView;
    TextView titleTex;
    TextView editText;
    EditText commentText;
    TextView nickNameText;
    TextView timeText;
    TextView allComments;
    TextView publish;
    String data;
    String title;
    String nickName;
    String userName;
    String time;
    SlidingUpPanelLayout slidingUpPanelLayout;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> commentList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_publish_content);
        getWindow().setStatusBarColor(Color.argb(0, 211,211,211));
        newsWebView = findViewById(R.id.newscontent);
        newsWebView.getSettings().setJavaScriptEnabled(true);
        newsWebView.setWebViewClient(new WebViewClient());
        titleTex = findViewById(R.id.title);
        nickNameText = findViewById(R.id.nick_name);
        timeText = findViewById(R.id.time);
        allComments = findViewById(R.id.allComments);
        publish = findViewById(R.id.publish);
        data = getIntent().getStringExtra("data");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        nickName = getIntent().getStringExtra("nickName");
        userName = getIntent().getStringExtra("userName");
        titleTex.setText(title);
        if(nickName != null) {
            nickNameText.setText(nickName);
        } else {
            nickNameText.setText(userName);
        }
        timeText.setText(time);
        newsWebView.loadDataWithBaseURL(null, data, "text/html" , "utf-8", null);
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
        Comment comment = new Comment("", "lin", "写的真好", 1);
        Comment comment2 = new Comment("", "lin", "写的真好", 1);
        Comment comment3 = new Comment("", "lin", "写的真好", 1);
        commentList.add(comment);
        commentList.add(comment2);
        commentList.add(comment3);
        recyclerView = findViewById(R.id.recyclerView);
        commentAdapter = new CommentAdapter(commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment("", "lin", commentText.getText().toString(), 1);
                commentList.add(comment);
                commentAdapter.notifyItemInserted(commentList.size() - 1);
                recyclerView.scrollToPosition(commentList.size() - 1);
                commentText.setText("");
            }
        });
    }

    public static void actionStart(Context context, UsersNews usersNews) {
        Intent intent = new Intent(context, showPublishContentActivity.class);
        intent.putExtra("data", usersNews.getNewsContent());
        intent.putExtra("title", usersNews.getTitle());
        intent.putExtra("nickName", usersNews.getNickName());
        intent.putExtra("userName", usersNews.getUserName());
        intent.putExtra("time", usersNews.getDate());
        context.startActivity(intent);
    }
}
