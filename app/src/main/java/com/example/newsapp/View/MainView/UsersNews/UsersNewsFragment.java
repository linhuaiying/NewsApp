package com.example.newsapp.View.MainView.UsersNews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.Presenter.UsersNewsPresenter.UsersNewsPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.MainView.BaseFragment;
import com.example.newsapp.adapter.UsersNewsAdapter;
import com.example.newsapp.bean.UsersNewsbean.UsersNews;

import java.util.List;

public class UsersNewsFragment extends BaseFragment<UsersNewsPresenter, IUsersNewsView> implements IUsersNewsView {
    RecyclerView recyclerView;
    UsersNewsAdapter usersNewsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.users_news_layout, container, false);
        recyclerView = view.findViewById(R.id.usersnewsList);
        try {
            presenter.fetch();
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
                            presenter.fetch();
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
    public void showErrorMessage(String msg) {

    }

    @Override
    protected UsersNewsPresenter createPresenter() {
        return new UsersNewsPresenter();
    }

    @Override
    public void showNewsView(List<UsersNews> usersNewsList) {
        usersNewsAdapter = new UsersNewsAdapter(usersNewsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(usersNewsAdapter);
    }
}
