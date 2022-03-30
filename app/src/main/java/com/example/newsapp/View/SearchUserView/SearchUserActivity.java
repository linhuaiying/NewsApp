package com.example.newsapp.View.SearchUserView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.SearchUserPresenter.SearchUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.adapter.MyConcernUserAdapter;
import com.example.newsapp.adapter.SearchUserAdapter;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;
import java.util.Map;

public class SearchUserActivity extends BaseActivity<SearchUserPresenter, ISearchUserView> implements ISearchUserView {
    EditText searchEt;
    RecyclerView concernRecyclerView;
    RecyclerView noconcernRecyclerView;
    SearchUserAdapter searchAdapter;
    MyConcernUserAdapter myConcernUserAdapter;
    TextView defaultText;
    TextView searchBtn;
    Button backBtn;
    ProgressBar mProBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        searchEt = findViewById(R.id.serchEt);
        searchEt.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) searchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        concernRecyclerView = findViewById(R.id.concernList);
        noconcernRecyclerView = findViewById(R.id.noconcernList);
        defaultText = findViewById(R.id.default_text);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultText.setVisibility(View.GONE);
                createProgressBar();
                InputMethodManager inputMethodManager = (InputMethodManager) searchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchUserActivity.this.finish();
            }
        });
    }

    @Override
    protected SearchUserPresenter createPresenter() {
        return new SearchUserPresenter();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    //进度条
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mProBar.setVisibility(View.GONE);
                try {
                    presenter.fetch(SaveAccount.getUserInfo(SearchUserActivity.this).get("userName"), searchEt.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(700);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                SearchUserActivity.this.runOnUiThread(runnable);
            }
        }).start();
    }

    @Override
    public void showMyUser(Map<String, List<MyUser>> map) {
        List<MyUser> concernUsers = map.get("concern");
        List<MyUser> noconcernUsers = map.get("noconcern");
        if(concernUsers != null && concernUsers.size() > 0) {
            defaultText.setVisibility(View.GONE);
            myConcernUserAdapter = new MyConcernUserAdapter(concernUsers);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            concernRecyclerView.setLayoutManager(linearLayoutManager);
            concernRecyclerView.setAdapter(myConcernUserAdapter);
            concernRecyclerView.setVisibility(View.VISIBLE);
        } else {
            concernRecyclerView.setVisibility(View.GONE);
        }
        if(noconcernUsers != null && noconcernUsers.size() > 0) {
            defaultText.setVisibility(View.GONE);
            searchAdapter = new SearchUserAdapter(noconcernUsers);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            noconcernRecyclerView.setLayoutManager(linearLayoutManager);
            noconcernRecyclerView.setAdapter(searchAdapter);
            noconcernRecyclerView.setVisibility(View.VISIBLE);
        } else {
            noconcernRecyclerView.setVisibility(View.GONE);
        }
        if((concernUsers == null && noconcernUsers == null) || (concernUsers != null && concernUsers.size() == 0 && noconcernUsers != null && noconcernUsers.size() == 0)) {
            defaultText.setVisibility(View.VISIBLE);
            concernRecyclerView.setVisibility(View.GONE);
            noconcernRecyclerView.setVisibility(View.GONE);
            MyToast.toast("没有找到相关用户!");
        }
    }
}
