package com.example.newsapp.View.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.R;
import com.example.newsapp.View.MainView.UsersNews.UsersNewsFragment;
import com.example.newsapp.View.MainView.MyViews.MyFragment;
import com.example.newsapp.View.MainView.HomeNewsFragment;
import com.example.newsapp.View.MyConcernUserView.MyConcernUserActivity;
import com.example.newsapp.View.MyFansView.MyFansActivity;
import com.example.newsapp.View.PublishView.PublishActivity;
import com.example.newsapp.View.SearchUserView.SearchUserActivity;
import com.example.newsapp.View.SearchView.SearchActivity;
import com.example.newsapp.View.UserView.LoginActivity;
import com.example.newsapp.adapter.MainFragmentAdapter;
import com.example.newsapp.bean.Userbean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    EditText editText;
    List<Fragment> fragments = new ArrayList<>();
    User user;
    int fragmentID = 0;
    String nickName;
    String sign;
    String userIcon;
    TextView nickNameText;
    TextView signText;
    CircleImageView circleImageView;
    View headerView;
    Button loginOutBtn;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(Color.BLACK);
        user = (User) getIntent().getSerializableExtra("user"); //将实体类解析出来
        fragmentID = getIntent().getIntExtra("fragmentID", 0);
        init();
        viewPager.setCurrentItem(fragmentID);
        if(fragmentID == 2) bottomNavigationView.setSelectedItemId(R.id.item_my);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        sign = SaveAccount.getUserInfo(this).get("sign");
        userIcon = SaveAccount.getUserInfo(this).get("userIcon");
        nickNameText = headerView.findViewById(R.id.nick_name);
        if(nickName != null) nickNameText.setText(nickName);
        signText = headerView.findViewById(R.id.sign);
        if(sign != null && !sign.equals("")) signText.setText(sign);
        circleImageView = headerView.findViewById(R.id.user_icon);
        if(userIcon != null) Glide.with(this).load(userIcon).into(circleImageView);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message msg) {
        if(msg.what == 100) {
            nickName = SaveAccount.getUserInfo(this).get("nickName");
            sign = SaveAccount.getUserInfo(this).get("sign");
            userIcon = SaveAccount.getUserInfo(this).get("userIcon");
            nickNameText = headerView.findViewById(R.id.nick_name);
            if(nickName != null) nickNameText.setText(nickName);
            signText = headerView.findViewById(R.id.sign);
            if(sign != null && !sign.equals("")) signText.setText(sign);
            circleImageView = headerView.findViewById(R.id.user_icon);
            if(userIcon != null) Glide.with(this).load(userIcon).into(circleImageView);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        sign = SaveAccount.getUserInfo(this).get("sign");
        userIcon = SaveAccount.getUserInfo(this).get("userIcon");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        nickNameText = headerView.findViewById(R.id.nick_name);
        if(nickName != null) nickNameText.setText(nickName);
        signText = headerView.findViewById(R.id.sign);
        if(sign != null && !sign.equals("")) signText.setText(sign);
        circleImageView = headerView.findViewById(R.id.user_icon);
        if(userIcon != null) Glide.with(this).load(userIcon).into(circleImageView);
        loginOutBtn = navigationView.findViewById(R.id.login_out);
        loginOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccount.deleteUserInfo(MainActivity.this);
                LoginActivity.actionStart(MainActivity.this);
                MainActivity.this.finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.person);
        }
        navigationView.setCheckedItem(R.id.concern_user);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.concern_user:
                        mDrawerLayout.closeDrawers();
                        MyConcernUserActivity.actionStart(MainActivity.this);
                        break;
                    case R.id.fans:
                        MyFansActivity.actionStart(MainActivity.this);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.findFriends:
                        mDrawerLayout.closeDrawers();
                        SearchUserActivity.actionStart(MainActivity.this);
                        break;
                }
                return true;
            }

        });

        viewPager = findViewById(R.id.mainviewpaper);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragments.add(new HomeNewsFragment());
        fragments.add(new UsersNewsFragment());
        fragments.add(new MyFragment());
        MainFragmentAdapter mainFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mainFragmentAdapter);
        viewPager.setOffscreenPageLimit(fragments.size());
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.item_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_find:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.item_my:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });
        editText = findViewById(R.id.serch);
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchActivity.actionStart(MainActivity.this);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.publish_article:
                PublishActivity.actionStart(this);
            default:
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bar_menu, menu);
        return true;
    }

    public static void actionStart(Context context, User user, int fragmentID) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("user", user);
        intent.putExtra("fragmentID", fragmentID);
        context.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
       moveTaskToBack(true);
    }
    
}
