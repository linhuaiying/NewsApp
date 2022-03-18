package com.example.newsapp.View.EditInfoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.EditInfoPresenter.EditInfoPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;

public class EditiInfoActivity extends BaseActivity<EditInfoPresenter, IEditInfoView> implements IEditInfoView {

    LinearLayout sexLinearLayout;
    TextView sexText;
    TextView save;
    EditText nickNameEt;
    EditText signEt;
    int selectItem = 0;
    CharSequence[] sexList = new CharSequence[] { "保密", "男", "女" };
    String sex;
    String sign;
    String nickName;
    String userName;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editi_info);
        userName = SaveAccount.getUserInfo(this).get("userName");
        sex = SaveAccount.getUserInfo(this).get("sex");
        sign = SaveAccount.getUserInfo(this).get("sign");
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        sexText = findViewById(R.id.sex_text);
        nickNameEt = findViewById(R.id.nick_name);
        signEt = findViewById(R.id.sign);
        if(sex != null) sexText.setText(sex);
        if(nickName != null) nickNameEt.setText(nickName);
        if(sign != null) signEt.setText(sign);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccount.saveExtraUserInfo(EditiInfoActivity.this, nickNameEt.getText().toString(), sexText.getText().toString(), signEt.getText().toString());
                try {
                    presenter.fetch(userName, nickNameEt.getText().toString(), sexText.getText().toString(), signEt.getText().toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        sexLinearLayout = findViewById(R.id.sex_linearLayout);
        sexLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              AlertDialog alertDialog = new AlertDialog.Builder(EditiInfoActivity.this)//
                        .setTitle("选择性别")// 标题
                        .setSingleChoiceItems(//
                                sexList,// 列表显示的项目
                                selectItem,// 默认选中 第一个
                                new DialogInterface.OnClickListener() {// 设置条目
                                    public void onClick(DialogInterface dialog, int which) {// 响应事件
                                        // 选中之后, 执行这里的 代码
                                        selectItem = which;
                                        sexText.setText(sexList[which]);
                                        // 关闭对话框
                                        dialog.dismiss();
                                    }
                                }
                        )
                        .show();
            }
        });
    }

    @Override
    protected EditInfoPresenter createPresenter() {
        return new EditInfoPresenter();
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, EditiInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void showSuccessMsg(String msg) {
        if(msg.equals("success")) {
            MyToast.toast("修改成功！");
            this.finish();
        } else {
            MyToast.toast("发布失败，请检查网络！");
        }
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
