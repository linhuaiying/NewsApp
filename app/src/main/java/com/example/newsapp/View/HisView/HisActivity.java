package com.example.newsapp.View.HisView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.LocalUtils.UrlConfig;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Service.UserService;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.MainView.MyNews.MyNewsFragment;
import com.example.newsapp.View.MainView.MyViews.IMyView;
import com.example.newsapp.View.MyFansView.MyFansActivity;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.example.newsapp.bean.Userbean.User;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
    Button concernBtn;
    Button backBtn;
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
        concernBtn = findViewById(R.id.concern_btn);
        List<MyUser> concernUsers = SaveAccount.getConcernUsers(HisActivity.this) == null ? new ArrayList<>() : SaveAccount.getConcernUsers(HisActivity.this);
        for(int i = 0;i < concernUsers.size(); i++) {
            if(myUser.getUserName().equals(concernUsers.get(i).getUserName())) {
                concernBtn.setText("取消关注");
            }
        }
        backBtn = findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HisActivity.this.finish();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MyNewsFragment myNewsFragment = new MyNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("userName", myUser.getUserName());
        myNewsFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.newsFrameLayout, myNewsFragment);
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
        concernBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = new Retrofit.Builder().baseUrl(UrlConfig.baseUrl)
                        .addConverterFactory(GsonConverterFactory.create()) //添加转换器build();
                        .build();
                UserService userService = retrofit.create(UserService.class);
                Call<ResponseBody> call;
                if(concernBtn.getText().toString().equals("关注")) {
                    call = userService.concernUser(SaveAccount.getUserInfo(HisActivity.this).get("userName"), myUser.getUserName());
                } else {
                    call = userService.noconcernUser(SaveAccount.getUserInfo(HisActivity.this).get("userName"), myUser.getUserName());
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            if(response.body().string().equals("success")) {
                                if(concernBtn.getText().toString().equals("关注")) {
                                    MyToast.toast("关注成功！");
                                    concernBtn.setText("取消关注");
                                    MyUser myUser = new MyUser();
                                    myUser.setUserName(myUser.getUserName());
                                    concernUsers.add(myUser);
                                    SaveAccount.saveConcernUsers(HisActivity.this, concernUsers);
                                } else {
                                    MyToast.toast("取消关注成功！");
                                    concernBtn.setText("关注");
                                    for(int i = 0; i < concernUsers.size(); i++) {
                                        if(concernUsers.get(i).getUserName().equals(myUser.getUserName())) {
                                            concernUsers.remove(i);
                                            SaveAccount.saveConcernUsers(HisActivity.this, concernUsers);
                                            break;
                                        }
                                    }
                                }
                            }
                            else MyToast.toast("关注失败！");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        MyToast.toast("关注失败！");
                    }
                });
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
