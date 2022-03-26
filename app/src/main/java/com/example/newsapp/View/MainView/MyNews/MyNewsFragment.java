package com.example.newsapp.View.MainView.MyNews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.MyNewsPresenter.MyNewsPresenter;
import com.example.newsapp.Presenter.UsersNewsPresenter.UsersNewsPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.MainView.BaseFragment;
import com.example.newsapp.View.MainView.UsersNews.IUsersNewsView;
import com.example.newsapp.adapter.UsersNewsAdapter;
import com.example.newsapp.bean.Userbean.User;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;


public class MyNewsFragment extends BaseFragment<MyNewsPresenter, IMyNewsView> implements IMyNewsView {
    RecyclerView recyclerView;
    UsersNewsAdapter usersNewsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView defaultText;
    String userName;
    public MyNewsFragment(String userName) {
        this.userName = userName;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.users_news_layout, container, false);
        recyclerView = view.findViewById(R.id.usersnewsList);
        defaultText = view.findViewById(R.id.default_text);
        try {
            presenter.fetch(userName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        swipeRefreshLayout = view.findViewById(R.id.news_fresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            presenter.fetch(userName);
                            swipeRefreshLayout.setRefreshing(false);
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
                        getActivity().runOnUiThread(runnable);
                    }
                }).start();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            presenter.fetch(userName);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    protected MyNewsPresenter createPresenter() {
        return new MyNewsPresenter();
    }


    @Override
    public void showNewsView(List<UsersNews> myNewsList) {
        if(myNewsList.size() > 0) defaultText.setVisibility(View.GONE);
        else defaultText.setVisibility(View.VISIBLE);
        usersNewsAdapter = new UsersNewsAdapter(myNewsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(usersNewsAdapter);
    }
}
