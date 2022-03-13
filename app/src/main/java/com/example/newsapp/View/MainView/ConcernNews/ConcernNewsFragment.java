package com.example.newsapp.View.MainView.ConcernNews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.newsapp.Presenter.ConcernNewsPresenter.ConcernNewsPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.MainView.BaseFragment;
import com.example.newsapp.adapter.ConcernNewsAdapter;
import com.example.newsapp.bean.ConcernNewsbean.ConcernNews;

import java.util.List;

public class ConcernNewsFragment extends BaseFragment<ConcernNewsPresenter, IConcernNewsView> implements IConcernNewsView {
    RecyclerView recyclerView;
    ConcernNewsAdapter concernNewsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.concern_news_layout, container, false);
        recyclerView = view.findViewById(R.id.concernnewsList);
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
    protected ConcernNewsPresenter createPresenter() {
        return new ConcernNewsPresenter();
    }

    @Override
    public void showNewsView(List<ConcernNews> concernNewsList) {
        concernNewsAdapter = new ConcernNewsAdapter(concernNewsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(concernNewsAdapter);
    }
}
