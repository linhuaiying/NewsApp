package com.example.newsapp.View.MyConcernUserView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.SearchUserView.SearchUserActivity;
import com.example.newsapp.adapter.MyConcernUserAdapter;
import com.example.newsapp.adapter.SearchUserAdapter;
import com.example.newsapp.bean.MyUserbean.MyUser;

import java.util.List;

public class MyConcernUserActivity extends BaseActivity<MyUserPresenter, IMyConcernUserView> implements IMyConcernUserView {
    RecyclerView recyclerView;
    MyConcernUserAdapter myConcernUserAdapter;
    TextView defaultText;
    Button backBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_concern_user);
        recyclerView = findViewById(R.id.recyclerView);
        defaultText = findViewById(R.id.default_text);
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyConcernUserActivity.this.finish();
            }
        });
        try {
            presenter.getConcernUsers(SaveAccount.getUserInfo(this).get("userName"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MyConcernUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected MyUserPresenter createPresenter() {
        return new MyUserPresenter();
    }

    @Override
    public void showConcernUsers(List<MyUser> myUserList) {
        if(myUserList != null && myUserList.size() > 0) {
            defaultText.setVisibility(View.GONE);
            myConcernUserAdapter = new MyConcernUserAdapter(myUserList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(myConcernUserAdapter);
            recyclerView.setVisibility(View.VISIBLE);
            SaveAccount.saveConcernUsers(this, myUserList);
        }
        else {
            defaultText.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMyUser(MyUser myUser) {

    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
