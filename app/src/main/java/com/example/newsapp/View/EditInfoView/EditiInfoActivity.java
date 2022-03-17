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
import com.example.newsapp.R;

public class EditiInfoActivity extends AppCompatActivity {

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editi_info);
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
                //presenter.fetch
                SaveAccount.saveExtraUserInfo(EditiInfoActivity.this, nickNameEt.getText().toString(), sexText.getText().toString(), signEt.getText().toString());
                EditiInfoActivity.this.finish();
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

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, EditiInfoActivity.class);
        context.startActivity(intent);
    }
}
