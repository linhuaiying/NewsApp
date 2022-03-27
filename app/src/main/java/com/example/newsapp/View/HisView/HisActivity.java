package com.example.newsapp.View.HisView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.MainView.MyNews.MyNewsFragment;
import com.example.newsapp.View.MainView.MyViews.IMyView;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.example.newsapp.bean.Userbean.User;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class HisActivity extends BaseActivity<MyUserPresenter, IMyView> implements IMyView {
    MyUser myUser;
    TextView nickNameText;
    TextView nickNameText2;
    TextView sexText;
    TextView signText;
    TextView moreInfoText;
    CircleImageView circleImageView;
    CircleImageView circleImageView2;
    String nickName;
    String sex;
    String sign;
    String userIcon;
    SlidingUpPanelLayout slidingUpPanelLayout;
    TextView close;
    FrameLayout frameLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_his_layout);
        myUser = (MyUser) getIntent().getSerializableExtra("user");
        nickNameText = findViewById(R.id.nick_name);
        slidingUpPanelLayout = findViewById(R.id.sliding_layout);
        nickNameText2 = findViewById(R.id.nick_name_2);
        sexText = findViewById(R.id.sex_text);
        signText = findViewById(R.id.sign);
        circleImageView = findViewById(R.id.user_icon);
        circleImageView2 = findViewById(R.id.user_icon_2);
        moreInfoText = findViewById(R.id.moreInfo);
        frameLayout = findViewById(R.id.newsFrameLayout);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.newsFrameLayout, new MyNewsFragment(myUser.getUserName()));
        fragmentTransaction.commit();
        if(myUser.getNickName() != null) nickNameText.setText(myUser.getNickName());
        else nickNameText.setText(myUser.getUserName());
        try {
            presenter.fetch(myUser.getUserName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        moreInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(0.8f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
    }

    @Override
    protected MyUserPresenter createPresenter() {
        return new MyUserPresenter();
    }

    public static void actionStart(Context context, MyUser myUser) {
        Intent intent = new Intent(context, HisActivity.class);
        intent.putExtra("user", myUser);
        context.startActivity(intent);
    }

    @Override
    public void showMyUser(MyUser myUser) {
       this.myUser = myUser;
       nickName = myUser.getNickName();
       sex = myUser.getSex();
       sign = myUser.getSign();
       userIcon = myUser.getUserIcon();
       if(nickName != null) {
           nickNameText.setText(nickName);
           nickNameText2.setText(nickName);
       }
       if(sex != null) sexText.setText(sex);
       if(sign != null) signText.setText(sign);
       if(userIcon != null) {
           Glide.with(this).load(userIcon).into(circleImageView);
           Glide.with(this).load(userIcon).into(circleImageView2);
       }
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
