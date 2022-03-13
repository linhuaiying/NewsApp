package com.example.newsapp.View.MainView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.R;
import com.example.newsapp.View.MainView.JuHeNews.FinanceNewsFragment;
import com.example.newsapp.View.MainView.JuHeNews.GameNewsFragment;
import com.example.newsapp.View.MainView.JuHeNews.TopNewsFragment;
import com.example.newsapp.adapter.FrgAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeNewsFragment extends Fragment {
    List<BaseFragment> fragments= new ArrayList<>();
    TabLayout tabLayout;
    ViewPager vp;
    String[] titles = new String[]{"推荐", "财经", "游戏"};
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.home_news_layout, container, false);
        attachTab(view);
        swipeRefreshLayout = view.findViewById(R.id.news_fresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            fragments.get(vp.getCurrentItem()).presenter.fetch();
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

    public void attachTab(View view) {
        //构造适配器
        fragments.add(new TopNewsFragment());
        fragments.add(new FinanceNewsFragment());
        fragments.add(new GameNewsFragment());
        FrgAdapter adapter = new FrgAdapter(getActivity().getSupportFragmentManager(), fragments);

        //设定适配器
        vp = view.findViewById(R.id.viewpaper);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(fragments.size()); //设置缓存的fragment的大小，不用一切换就销毁fragment

        tabLayout = view.findViewById(R.id.tablayout);
        for(int i=0;i<titles.length;i++){
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        //ViewPager点击事件
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //选中ViewPager与TabLayout联动
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //TabLayout点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //选中tab
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中tab时,TabLayout与ViewPager联动
                vp.setCurrentItem(tab.getPosition());
            }
            //未选择tab
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //重复选中tab
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



}
