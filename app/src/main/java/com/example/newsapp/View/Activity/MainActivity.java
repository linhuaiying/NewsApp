package com.example.newsapp.View.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

import com.example.newsapp.R;
import com.example.newsapp.View.MainView.UsersNews.UsersNewsFragment;
import com.example.newsapp.View.MainView.MyViews.MyFragment;
import com.example.newsapp.View.MainView.HomeNewsFragment;
import com.example.newsapp.View.PublishView.PublishActivity;
import com.example.newsapp.View.SearchView.SearchActivity;
import com.example.newsapp.adapter.MainFragmentAdapter;
import com.example.newsapp.bean.Userbean.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    ViewPager viewPager;
    BottomNavigationView bottomNavigationView;
    EditText editText;
    List<Fragment> fragments = new ArrayList<>();
    User user;
    int fragmentID = 0;
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
    }

    
    @SuppressLint("ClickableViewAccessibility")
    public void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView phoneText = headerView.findViewById(R.id.username);
        phoneText.setText(user.getUsername());
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.person);
        }
        navigationView.setCheckedItem(R.id.nav_friends);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
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
