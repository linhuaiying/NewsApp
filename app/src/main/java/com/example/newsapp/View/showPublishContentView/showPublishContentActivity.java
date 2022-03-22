package com.example.newsapp.View.showPublishContentView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.ShowPublishContentPresenter.ShowPublishContentPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.adapter.CommentAdapter;
import com.example.newsapp.bean.Commentbean.Comment;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class showPublishContentActivity extends BaseActivity<ShowPublishContentPresenter, IShowPublishContentView> implements IShowPublishContentView {

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
    String newsNickName;
    String newsUserName;
    String time;
    String userName;
    String nickName;
    int newsId;
    SlidingUpPanelLayout slidingUpPanelLayout;
    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    List<Comment> commentList = new ArrayList<>();
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
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
        newsNickName = getIntent().getStringExtra("nickName");
        newsUserName = getIntent().getStringExtra("userName");
        newsId = getIntent().getIntExtra("newsId", -1);
        userName = SaveAccount.getUserInfo(this).get("userName");
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        titleTex.setText(title);
        if(newsNickName != null) {
            nickNameText.setText(newsNickName);
        } else {
            nickNameText.setText(newsUserName);
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
        recyclerView = findViewById(R.id.recyclerView);
        try {
            presenter.fetch(newsId); //获取评论列表
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment(userName, nickName, commentText.getText().toString(), newsId);
                try {
                    presenter.fetch(comment); //上传评论
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                commentList.add(comment);
                commentAdapter = new CommentAdapter(commentList);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(showPublishContentActivity.this);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(commentAdapter);
                commentAdapter.notifyItemInserted(commentList.size() - 1);
                recyclerView.scrollToPosition(commentList.size() - 1);
                commentText.setText("");
            }
        });
    }

    @Override
    protected ShowPublishContentPresenter createPresenter() {
        return new ShowPublishContentPresenter();
    }

    public static void actionStart(Context context, UsersNews usersNews) {
        Intent intent = new Intent(context, showPublishContentActivity.class);
        intent.putExtra("data", usersNews.getNewsContent());
        intent.putExtra("title", usersNews.getTitle());
        intent.putExtra("nickName", usersNews.getNickName());
        intent.putExtra("userName", usersNews.getUserName());
        intent.putExtra("time", usersNews.getDate());
        intent.putExtra("newsId", usersNews.getId());
        context.startActivity(intent);
    }

    @Override
    public void showCommments(List<Comment> commentList) throws InterruptedException {
        Log.d("RegisterModel", String.valueOf(commentList));
        this.commentList = commentList;
        commentAdapter = new CommentAdapter(this.commentList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(commentAdapter);
    }

    @Override
    public void showSuccessMsg(String msg) {
        Log.d("RegisterModel", msg);
        if(msg.equals("success")) {
            MyToast.toast("评论成功！");
        } else {
            MyToast.toast("评论失败, 请检查网络设置");
        }
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
