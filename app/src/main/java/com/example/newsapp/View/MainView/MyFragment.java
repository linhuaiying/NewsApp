package com.example.newsapp.View.MainView;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.EditInfoView.EditiInfoActivity;
import com.example.newsapp.View.MainView.MyNews.MyNewsFragment;
import com.example.newsapp.View.MainView.UsersNews.UsersNewsFragment;
import com.example.newsapp.adapter.FrgAdapter;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MyFragment extends BaseFragment<MyUserPresenter, IMyView> implements IMyView{
    List<BaseFragment> fragments= new ArrayList<>();
    TabLayout tabLayout;
    ViewPager vp;
    String[] titles = new String[]{"全部文章", "收藏"};
    SwipeRefreshLayout swipeRefreshLayout;
    Button editBtn;
    TextView textView;
    String nickName;
    String sex;
    String sign;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.my_layout, container, false);
        attachTab(view);
        textView = view.findViewById(R.id.nick_name);
        nickName = SaveAccount.getUserInfo(container.getContext()).get("nickName");
        sex = SaveAccount.getUserInfo(container.getContext()).get("sex");
        sign = SaveAccount.getUserInfo(container.getContext()).get("sign");
        //如果本地没有缓存就从服务器获取
        if(nickName != null) {
            textView.setText(nickName);
        } else {
            try {
                presenter.fetch(SaveAccount.getUserInfo(container.getContext()).get("userName"));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        editBtn = view.findViewById(R.id.exit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditiInfoActivity.actionStart(container.getContext());
            }
        });
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

    @Override
    public void onResume() {
        super.onResume();
        nickName = SaveAccount.getUserInfo(getActivity()).get("nickName");
        if(nickName != null) {
            textView.setText(nickName);
        }
    }

    public void attachTab(View view) {
        //构造适配器
        fragments.add(new MyNewsFragment());
        fragments.add(new MyNewsFragment());
        FrgAdapter adapter = new FrgAdapter(getChildFragmentManager(), fragments); //要用childFragmentManager，不然显示不出来

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

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    protected MyUserPresenter createPresenter() {
        return new MyUserPresenter();
    }

    @Override
    public void showMyUser(MyUser myUser) {
        SaveAccount.saveExtraUserInfo(getActivity(), myUser.getNickName(), myUser.getSex(), myUser.getSign());
    }
}
