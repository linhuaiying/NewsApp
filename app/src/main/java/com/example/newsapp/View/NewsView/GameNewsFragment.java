package com.example.newsapp.View.NewsView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.Presenter.NewsPresenter.GameNewsPresenter;
import com.example.newsapp.R;
import com.example.newsapp.adapter.NewsAdapter;
import com.example.newsapp.bean.Newsbean.News;

import java.util.List;

public class GameNewsFragment extends BaseFragment<GameNewsPresenter, INewsView> implements INewsView {
    RecyclerView recyclerView;
    NewsAdapter newsAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.game_news_layout, container, false);
        recyclerView = view.findViewById(R.id.gamenewsList);
        try {
            presenter.fetch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return view;
    }
    @Override
    protected GameNewsPresenter createPresenter() {
        return new GameNewsPresenter();
    }

    @Override
    public void showNewsView(List<News> newsList) {
        newsAdapter = new NewsAdapter(newsList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(newsAdapter);
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    protected void init() {
        super.init();
        getLifecycle().addObserver(presenter); //添加观察者
    }
}
