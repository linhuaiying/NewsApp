package com.example.newsapp.View.MainView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.MainView.JuHeNews.EntertainmentNewsFragment;
import com.example.newsapp.View.MainView.JuHeNews.FinanceNewsFragment;
import com.example.newsapp.View.MainView.JuHeNews.GameNewsFragment;
import com.example.newsapp.View.MainView.JuHeNews.HealthyNewsFragment;
import com.example.newsapp.View.MainView.JuHeNews.TopNewsFragment;
import com.example.newsapp.adapter.FrgAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class HomeNewsFragment extends Fragment {
    List<BaseFragment> fragments= new ArrayList<>();
    TabLayout tabLayout;
    ViewPager vp;
    List<String> titles = new ArrayList<String>();
    SwipeRefreshLayout swipeRefreshLayout;
    AlertDialog alertDialog;
    Boolean isFirstStart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.home_news_layout, container, false);
        titles.add("推荐");
        isFirstStart = SaveAccount.isFistStart(getActivity());
        if(isFirstStart) {
            showMutilAlertDialog(view);
        } else {
            //取缓存中的titles
            titles = SaveAccount.getTitles(getActivity());
            attachTab(view);
        }
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
        fragments.add(new TopNewsFragment());
        tabLayout = view.findViewById(R.id.tablayout);
        for(int i=0;i<titles.size();i++){
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.getTabAt(i).setText(titles.get(i));
            //构造适配器
            if(titles.get(i).equals("财经")) fragments.add(new FinanceNewsFragment());
            if(titles.get(i).equals("游戏")) fragments.add(new GameNewsFragment());
            if(titles.get(i).equals("娱乐")) fragments.add(new EntertainmentNewsFragment());
            if(titles.get(i).equals("健康")) fragments.add(new HealthyNewsFragment());
        }
        FrgAdapter adapter = new FrgAdapter(getActivity().getSupportFragmentManager(), fragments);
        //设定适配器
        vp = view.findViewById(R.id.viewpaper);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(fragments.size()); //设置缓存的fragment的大小，不用一切换就销毁fragment
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

    public void showMutilAlertDialog(View view){
        final String[] items = {"财经", "游戏", "娱乐", "健康"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        alertBuilder.setTitle("选择你感兴趣的新闻类别");
        /**
         *第一个参数:弹出框的消息集合，一般为字符串集合
         * 第二个参数：默认被选中的，布尔类数组
         * 第三个参数：勾选事件监听
         */
        alertBuilder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
                if (isChecked){
                    MyToast.toast("选择");
                    titles.add(items[i]);
                }else {
                    MyToast.toast("取消选择");
                    titles.remove(items[i]);
                }
            }
        });
        alertBuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog.dismiss();
                //存下来
                SaveAccount.saveTitles(getActivity(), titles);
                SaveAccount.saveIsFirstStart(getActivity(), false);
                attachTab(view);
            }
        });
        alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
