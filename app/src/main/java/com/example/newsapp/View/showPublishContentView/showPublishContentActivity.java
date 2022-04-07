package com.example.newsapp.View.showPublishContentView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.ShowPublishContentPresenter.ShowPublishContentPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.Activity.MainActivity;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.HisView.HisActivity;
import com.example.newsapp.adapter.CommentAdapter;
import com.example.newsapp.bean.Commentbean.Comment;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.example.newsapp.bean.Userbean.User;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class showPublishContentActivity extends BaseActivity<ShowPublishContentPresenter, IShowPublishContentView> implements IShowPublishContentView {

    WebView newsWebView;
    PopupWindow mPopWindow;
    CircleImageView circleImageView;
    TextView titleTex;
    TextView editText;
    EditText commentText;
    TextView nickNameText;
    TextView timeText;
    TextView allComments;
    TextView publish;
    TextView defaultText;
    Button moreBtn;
    Button backBtn;
    Button favBtn;
    Button likeBtn;
    String data;
    String title;
    String newsNickName;
    String newsUserName;
    String newsUserIcon;
    String time;
    String userName;
    String nickName;
    String userIcon;
    int newsId;
    boolean isFav = false;
    boolean isLike = false;
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
        circleImageView = findViewById(R.id.user_icon);
        moreBtn = findViewById(R.id.more);
        backBtn = findViewById(R.id.back);
        favBtn = findViewById(R.id.fav);
        likeBtn = findViewById(R.id.like);
        titleTex = findViewById(R.id.title);
        nickNameText = findViewById(R.id.nick_name);
        timeText = findViewById(R.id.time);
        allComments = findViewById(R.id.allComments);
        defaultText = findViewById(R.id.default_text);
        publish = findViewById(R.id.publish);
        data = getIntent().getStringExtra("data");
        title = getIntent().getStringExtra("title");
        time = getIntent().getStringExtra("time");
        newsNickName = getIntent().getStringExtra("nickName");
        newsUserName = getIntent().getStringExtra("userName");
        newsUserIcon = getIntent().getStringExtra("userIcon");
        newsId = getIntent().getIntExtra("newsId", -1);
        userName = SaveAccount.getUserInfo(this).get("userName");
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        userIcon = SaveAccount.getUserInfo(this).get("userIcon");
        titleTex.setText(title);
        if(newsNickName != null) {
            nickNameText.setText(newsNickName);
        } else {
            nickNameText.setText(newsUserName);
        }
        timeText.setText(time);
        if(newsUserIcon != null) Glide.with(this).load(newsUserIcon).into(circleImageView);
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
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyUser myUser = new MyUser();
                myUser.setUserName(newsUserName);
                myUser.setNickName(newsNickName);
                if(!newsUserName.equals(userName)) HisActivity.actionStart(showPublishContentActivity.this, myUser);
                else {
                    User user = new User();
                    user.setUsername(userName);
                    MainActivity.actionStart(showPublishContentActivity.this, user, 2);
                }
            }
        });
        if(!userName.equals(newsUserName)) moreBtn.setVisibility(View.GONE); //非本用户的文章不可操作
        else {
            moreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupWindow();
                }
            });
        }
        recyclerView = findViewById(R.id.recyclerView);
        try {
            presenter.fetch(newsId); //获取评论列表
            presenter.getFav(userName, newsId); //初始化该条新闻的收藏状态
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Comment comment = new Comment(userName, nickName, commentText.getText().toString(), newsId, userIcon);
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
                showPublishContentActivity.this.finish();
            }
        });
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                   if(!isFav) {
                       presenter.addFavNews(userName, newsId);
                   } else {
                       presenter.deleteFavNews(userName, newsId);
                   }
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
        intent.putExtra("userIcon", usersNews.getUserIcon());
        context.startActivity(intent);
    }

    @Override
    public void showCommments(List<Comment> commentList) {
        if(commentList.size() > 0) defaultText.setVisibility(View.GONE);
        else defaultText.setVisibility(View.VISIBLE);
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
            defaultText.setVisibility(View.GONE);
        } else {
            MyToast.toast("评论失败, 请检查网络设置");
        }
    }

    @Override
    public void showDeleteMsg(String msg) {
        if(msg.equals("success")) {
            MyToast.toast("删除成功！");
            showPublishContentActivity.this.finish();
        } else {
            MyToast.toast("删除失败, 请检查网络设置");
        }
    }

    //初始化fav
    @Override
    public void showFav(String msg) {
        if(msg.equals("success")) {
            isFav = true;
            favBtn.setBackgroundResource(R.drawable.fav);
        } else {
            isFav = false;
            favBtn.setBackgroundResource(R.drawable.cancel_fav);
        }
    }

    //设置fav
    @Override
    public void setFav(String msg) {
        if(msg.equals("success")) {
           if(isFav) {
               favBtn.setBackgroundResource(R.drawable.cancel_fav);
               isFav = false;
               MyToast.toast("取消收藏");
           } else {
               favBtn.setBackgroundResource(R.drawable.fav);
               isFav = true;
               MyToast.toast("收藏成功！");
           }
        } else {

        }
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    private void showPopupWindow() {
        //设置contentView
        View contentView = LayoutInflater.from(this).inflate(R.layout.delete_layout, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //显示PopupWindow
        View rootview = LayoutInflater.from(this).inflate(R.layout.activity_show_publish_content, null);
        mPopWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp=getWindow().getAttributes();
                lp.alpha=1.0f;
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp=getWindow().getAttributes();
        lp.alpha=0.5f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);
        TextView delete = contentView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    presenter.deleteNews(newsId);
                    mPopWindow.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        TextView cancel = contentView.findViewById(R.id.cancel);
         cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 mPopWindow.dismiss();
             }
         });
    }
}
