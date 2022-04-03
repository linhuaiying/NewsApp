package com.example.newsapp.View.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.R;
import com.example.newsapp.View.PublishView.PublishActivity;
import com.example.newsapp.View.UserView.LoginActivity;
import com.example.newsapp.bean.Userbean.User;

import java.util.Map;

import site.gemus.openingstartanimation.LineDrawStrategy;
import site.gemus.openingstartanimation.OpeningStartAnimation;

public class LaucherActivity extends AppCompatActivity {
    String phone;
    String password;
    Map<String,String> userInfo;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laucher);
        OpeningStartAnimation openingStartAnimation = new OpeningStartAnimation.Builder(this)
                .setAppName("今日资讯") //设置app名称
                .setColorOfAppName(Color.argb(255,0, 191, 255))
                .setAppStatement("看见更大的世界") //设置一句话描述
                .create();
        openingStartAnimation.show(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(!isLogined()) LoginActivity.actionStart(LaucherActivity.this);
                else {
                    User user = new User();
                    user.setUsername(phone);
                    user.setPassword(password);
                    MainActivity.actionStart(LaucherActivity.this, user, 0);
                    LaucherActivity.this.finish();
                }
            }
        }).start();
    }

    public boolean isLogined() {
        userInfo= SaveAccount.getUserInfo(this);
        phone = userInfo.get("userName");
        password = userInfo.get("password");
        if(phone != null && password != null) {
            return true;
        }
        return false;
    }
}
