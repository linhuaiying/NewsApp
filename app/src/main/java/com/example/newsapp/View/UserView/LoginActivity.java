package com.example.newsapp.View.UserView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.UserPresenter.LoginPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.Activity.MainActivity;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.bean.Userbean.User;

import java.util.Map;


public class LoginActivity extends BaseActivity<LoginPresenter, IUserView> implements View.OnClickListener, IUserView {

    String phone;
    String password;
    Map<String,String> userInfo;

    // 手机号输入框
    private EditText inputPhoneEt;

    // 登录按钮
    private Button commitBtn;

    //密码
    private EditText inputPasswordEt;

    //注册选项
    private TextView register;

    ProgressBar mProBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(isLogined()) {
            User user = new User();
            user.setUsername(phone);
            user.setPassword(password);
            MainActivity.actionStart(this, user, 0);
            this.finish();
        }
        init();
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

    @Override
    public void init() {
        inputPhoneEt = findViewById(R.id.login_input_phone_et);
        commitBtn = findViewById(R.id.login_commit_btn);
        inputPasswordEt = findViewById(R.id.login_input_password_et);
        register = findViewById(R.id.register);
        register.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    } //与LoginPresenter绑定

    //打开自己这个Activity
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        String phoneNums = inputPhoneEt.getText().toString();
        if(view.getId() == R.id.login_commit_btn) {
            // 1. 通过规则判断手机号
            if (!judgePhoneNums(phoneNums)) {
                return;
            }
            //2.判断账号密码是否有误
            try {
                jdge();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(view.getId() == R.id.register) {
            RegisterActivity.actionStart(this); //打开注册面板
            this.finish();
        }
    }

    public void jdge() throws InterruptedException {
        phone = inputPhoneEt.getText().toString();
        password = inputPasswordEt.getText().toString();

        if(phone.equals("")) {
            MyToast.toast("请输入手机号码");
        }
        else if(password.equals("")) {
            MyToast.toast("请输入密码");
        }
        else {
            presenter.fetch(phone, password);
        }
    }

    private boolean judgePhoneNums(String phoneNums) {
        if (isMatchLength(phoneNums, 11)
                && isMobileNO(phoneNums)) {
            return true;
        }
        Toast.makeText(this, "手机号码输入有误！", Toast.LENGTH_SHORT).show();
        return false;
    }

    public static boolean isMatchLength(String str, int length) {
        if (str.isEmpty()) {
            return false;
        } else {
            return str.length() == length ? true : false;
        }
    }

    public static boolean isMobileNO(String mobileNums) {
        /*
         * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
         * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
         * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
         */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex); //正则
    }

    //登录进度条
    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    //回调显示错误信息
    @Override
    public void showErrorMessage(String msg) {
        if(!msg.equals("")) MyToast.toast(msg);
    }

    //回调接收后台返回的结果
    @Override
    public void getUser(User user) {

        if(user != null) {    //跳转到主页
            //保存用户信息到本地
            if(!isLogined()) SaveAccount.saveUserInfo(this, user.getUsername(), user.getPassword());
            createProgressBar();
            MainActivity.actionStart(this, user, 0);
            this.finish();
        }

    }
}
