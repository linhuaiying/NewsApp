package com.example.newsapp.View.MyFansView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.MyConcernUserView.IMyConcernUserView;
import com.example.newsapp.View.SearchUserView.SearchUserActivity;
import com.example.newsapp.adapter.MyConcernUserAdapter;
import com.example.newsapp.adapter.SearchUserAdapter;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyFansActivity extends BaseActivity<MyUserPresenter, IMyFansView> implements IMyFansView {
    RecyclerView concernRecyclerView;
    RecyclerView noconcernRecyclerView;
    SearchUserAdapter searchAdapter;
    MyConcernUserAdapter myConcernUserAdapter;
    TextView defaultText;
    Button backBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fans);
        concernRecyclerView = findViewById(R.id.concernList);
        noconcernRecyclerView = findViewById(R.id.noconcernList);
        defaultText = findViewById(R.id.default_text);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFansActivity.this.finish();
            }
        });
        try {
            presenter.getMyFans(SaveAccount.getUserInfo(this).get("userName"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected MyUserPresenter createPresenter() {
        return new MyUserPresenter();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyFansActivity.class);
        context.startActivity(intent);
    }


    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    public void showFansList(Map<String, List<MyUser>> map) {
        if(map == null) {
            defaultText.setVisibility(View.VISIBLE);
            concernRecyclerView.setVisibility(View.GONE);
            noconcernRecyclerView.setVisibility(View.GONE);
            return;
        }
        List<MyUser> concernUsers = map.get("concern");
        List<MyUser> noconcernUsers = map.get("noconcern");
        List<MyUser> total = new ArrayList<>();
        if(concernUsers != null && concernUsers.size() > 0) {
            defaultText.setVisibility(View.GONE);
            myConcernUserAdapter = new MyConcernUserAdapter(concernUsers);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            concernRecyclerView.setLayoutManager(linearLayoutManager);
            concernRecyclerView.setAdapter(myConcernUserAdapter);
            concernRecyclerView.setVisibility(View.VISIBLE);
            total.addAll(concernUsers);
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
            total.addAll(noconcernUsers);
        } else {
            noconcernRecyclerView.setVisibility(View.GONE);
        }
        if((concernUsers == null && noconcernUsers == null) || (concernUsers != null && concernUsers.size() == 0 && noconcernUsers != null && noconcernUsers.size() == 0)) {
            defaultText.setVisibility(View.VISIBLE);
            concernRecyclerView.setVisibility(View.GONE);
            noconcernRecyclerView.setVisibility(View.GONE);
        }
        SaveAccount.saveFans(this, total);
    }
}
