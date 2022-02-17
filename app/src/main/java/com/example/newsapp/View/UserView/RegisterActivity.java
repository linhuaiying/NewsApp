package com.example.newsapp.View.UserView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.newsapp.Presenter.RegisterPresenter.RegisterPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.IBaseView;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RegisterActivity extends BaseActivity<RegisterPresenter, IUserView> implements View.OnClickListener, IBaseView {
    String APPKEY = "33b4e2629e772";
    String APPSECRETE = "a5e57b6b9a44e4f4ac83e8daa03bc41a";
    String phone;
    String password;
    String repassword;
    String code;

    // 手机号输入框
    private EditText inputPhoneEt;

    // 验证码输入框
    private EditText inputCodeEt;

    // 获取验证码按钮
    private Button requestCodeBtn;

    // 注册按钮
    private Button commitBtn;

    //密码
    private EditText inputPasswordEt;

    //确认密码
    private EditText inputRePasswordEt;

    ProgressBar mProBar;

    //倒计时
    int i = 30;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        EventBus.getDefault().register(this); //注册为观察者
        init();
    }

    @Override
    public void init() {
        inputPhoneEt = findViewById(R.id.login_input_phone_et);
        inputCodeEt = findViewById(R.id.login_input_code_et);
        requestCodeBtn = findViewById(R.id.login_request_code_btn);
        commitBtn = findViewById(R.id.login_commit_btn);
        inputPasswordEt = findViewById(R.id.login_input_password_et);
        inputRePasswordEt = findViewById(R.id.login_input_repassword_et);
        requestCodeBtn.setOnClickListener(this);
        commitBtn.setOnClickListener(this);
        //requestCodeBtn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        // 启动短信验证sdk
        MobSDK.init(this, APPKEY, APPSECRETE);
        MobSDK.submitPolicyGrantResult(true, null); //回传用户隐私授权结果

        EventHandler eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                EventBus.getDefault().post(msg);
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return new RegisterPresenter();
    }

    @Override
    public void onClick(View v) {
        String phoneNums = inputPhoneEt.getText().toString();
        switch (v.getId()) {
            case R.id.login_request_code_btn:
                // 1. 通过规则判断手机号
                if (!judgePhoneNums(phoneNums)) {
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums); //执行eventhandler回调

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                requestCodeBtn.setClickable(false);
                requestCodeBtn.setText("重新发送(" + i + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() { //倒计时
                        for (; i > 0; i--) {
                            Message msg = new Message();
                            msg.what = -9;
                            EventBus.getDefault().post(msg);
                            if (i <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000); //模拟一秒
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        Message msg = new Message();
                        msg.what = -8;
                        EventBus.getDefault().post(msg);
                    }
                }).start();
                break;

            case R.id.login_commit_btn:
                try {
                    jdge();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    public void jdge() throws InterruptedException {
        phone = inputPhoneEt.getText().toString();
        password = inputPasswordEt.getText().toString();
        repassword = inputRePasswordEt.getText().toString();
        code = inputCodeEt.getText().toString();
        if(phone.equals("")) {
            MyToast.toast("请输入手机号码");
        }
        else if(code.equals("")) {
            MyToast.toast("请输入验证码");
        }
        else if(password.equals("")) {
            MyToast.toast("请输入密码");
        }
        else if(repassword.equals("")) {
            MyToast.toast("请确认密码");
        }
        else if(password.length() < 6) {
            MyToast.toast("密码长度必须6位以上！");
        }
        else if(!password.equals(repassword)) {
            MyToast.toast("两次输入密码不一致");
        }
        else {
            presenter.fetch(phone, password, repassword); //明天把这个注释掉！
            //将收到的验证码和手机号提交再次核对
            SMSSDK.submitVerificationCode("86", phone, inputCodeEt
                    .getText().toString()); //执行eventhandler回调
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Message msg) throws InterruptedException {
        if (msg.what == -9) {
            requestCodeBtn.setText("重新发送(" + i + ")");
        } else if (msg.what == -8) {
            requestCodeBtn.setText("获取验证码");
            requestCodeBtn.setClickable(true);
            i = 30;
        } else {
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            Log.e("event", "event=" + event);
            if (result == SMSSDK.RESULT_COMPLETE) {
                // 短信注册成功后，返回MainActivity,然后提示
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                    presenter.fetch(phone, password, repassword);

                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    MyToast.toast("正在获取验证码");
                } else {
                    ((Throwable) data).printStackTrace();
                    MyToast.toast("验证码输入错误");
                }
            }
            else {
                MyToast.toast("验证码输入错误");
            }
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
            return mobileNums.matches(telRegex);
    }

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

    @Override
    public void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }


    @Override
    public void showErrorMessage(String msg) {
        if(!msg.equals("")) MyToast.toast(msg);
        else //跳转到登录页面
        {
            createProgressBar();
            LoginActivity.actionStart(this);
            this.finish();
        }

    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

}

