package com.example.newsapp.View.MainView.MyViews;

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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.EditInfoView.EditiInfoActivity;
import com.example.newsapp.View.MainView.BaseFragment;
import com.example.newsapp.View.MainView.MyFavNews.MyFavNewsFragment;
import com.example.newsapp.View.MainView.MyNews.MyNewsFragment;
import com.example.newsapp.adapter.FrgAdapter;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFragment extends BaseFragment<MyUserPresenter, IMyView> implements IMyView{
    List<BaseFragment> fragments= new ArrayList<>();
    TabLayout tabLayout;
    ViewPager vp;
    String[] titles = new String[]{"全部文章", "收藏"};
    Button editBtn;
    TextView nickNameText;
    TextView nickNameText2;
    TextView userNameText;
    TextView sexText;
    TextView signText;
    TextView moreInfoText;
    String nickName;
    String sex;
    String sign;
    String userName;
    String userIcon;
    SlidingUpPanelLayout slidingUpPanelLayout;
    TextView close;
    CircleImageView circleImageView;
    CircleImageView circleImageView2;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.my_layout, container, false);
        slidingUpPanelLayout = view.findViewById(R.id.sliding_layout);
        nickNameText = view.findViewById(R.id.nick_name);
        nickNameText2 = view.findViewById(R.id.nick_name_2);
        userNameText = view.findViewById(R.id.username);
        sexText = view.findViewById(R.id.sex_text);
        signText = view.findViewById(R.id.sign);
        moreInfoText = view.findViewById(R.id.moreInfo);
        circleImageView = view.findViewById(R.id.user_icon);
        circleImageView2 = view.findViewById(R.id.user_icon_2);

        nickName = SaveAccount.getUserInfo(container.getContext()).get("nickName");
        sex = SaveAccount.getUserInfo(container.getContext()).get("sex");
        sign = SaveAccount.getUserInfo(container.getContext()).get("sign");
        userIcon = SaveAccount.getUserInfo(container.getContext()).get("userIcon");
        userName = SaveAccount.getUserInfo(container.getContext()).get("userName");
        attachTab(view);
        userNameText.setText(userName);
        //如果本地没有缓存就从服务器获取
        if(nickName != null) {
            nickNameText.setText(nickName);
            nickNameText2.setText(nickName);
        } else {
            nickNameText.setText(userName);
            try {
                presenter.fetch(userName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(sex != null) sexText.setText(sex);
        if(sign != null) signText.setText(sign);
        if(userIcon != null) {
            Glide.with(getActivity()).load(userIcon).into(circleImageView);
            Glide.with(getActivity()).load(userIcon).into(circleImageView2);
        }
        editBtn = view.findViewById(R.id.exit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditiInfoActivity.actionStart(container.getContext());
            }
        });
        moreInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(0.8f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
        close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(0f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        nickName = SaveAccount.getUserInfo(getActivity()).get("nickName");
        sex = SaveAccount.getUserInfo(getActivity()).get("sex");
        sign = SaveAccount.getUserInfo(getActivity()).get("sign");
        userIcon = SaveAccount.getUserInfo(getActivity()).get("userIcon");
        if(nickName != null) {
            nickNameText.setText(nickName);
            nickNameText2.setText(nickName);
        }
        if(sex != null) sexText.setText(sex);
        if(sign != null) signText.setText(sign);
        if(userIcon != null) {
            Glide.with(getActivity()).load(userIcon).into(circleImageView);
            Glide.with(getActivity()).load(userIcon).into(circleImageView2);
        }
    }

    public void attachTab(View view) {
        //构造适配器
        fragments.add(new MyNewsFragment(userName));
        fragments.add(new MyFavNewsFragment());
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
        SaveAccount.saveExtraUserInfo(getActivity(), myUser.getNickName(), myUser.getSex(), myUser.getSign(), myUser.getUserIcon());
    }
}
