package com.example.newsapp.View.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.security.keystore.UserNotAuthenticatedException;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newsapp.Presenter.SearchPresenter.SearchPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.adapter.SearchAdapter;
import com.example.newsapp.adapter.UsersNewsAdapter;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class SearchActivity extends BaseActivity<SearchPresenter, ISearchView> implements ISearchView {
    EditText searchEt;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    TextView defaultText;
    TextView searchBtn;
    Button backBtn;
    List<UsersNews> userNewsList;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchEt = findViewById(R.id.serchEt);
        searchEt.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) searchEt.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    presenter.fetch(editable.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        defaultText = findViewById(R.id.default_text);
        searchBtn = findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userNewsList != null && userNewsList.size() > 0) {
                    defaultText.setVisibility(View.GONE);
                    UsersNewsAdapter usersNewsAdapter = new UsersNewsAdapter(userNewsList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SearchActivity.this);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(usersNewsAdapter);
                    recyclerView.setVisibility(View.VISIBLE);
                    userNewsList = null;
                }
                else {
                    defaultText.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    MyToast.toast("找不到相关内容，换个关键词试试！");
                }
            }
        });
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.this.finish();
            }
        });
    }

    @Override
    protected SearchPresenter createPresenter() {
        return new SearchPresenter();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showSearchNews(List<UsersNews> userNewsList) {
        if(userNewsList != null && userNewsList.size() > 0) {
            defaultText.setVisibility(View.GONE);
            this.userNewsList = userNewsList;
            searchAdapter = new SearchAdapter(userNewsList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(searchAdapter);
            recyclerView.setVisibility(View.VISIBLE);
        }
        else {
            defaultText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
