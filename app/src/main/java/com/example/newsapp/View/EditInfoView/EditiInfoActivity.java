package com.example.newsapp.View.EditInfoView;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.newsapp.R;

public class EditiInfoActivity extends AppCompatActivity {

    LinearLayout sexLinearLayout;
    int selectItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editi_info);
        sexLinearLayout = findViewById(R.id.sex_linearLayout);
        sexLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              AlertDialog alertDialog = new AlertDialog.Builder(EditiInfoActivity.this)//
                        .setTitle("选择性别")// 标题
                        .setSingleChoiceItems(//
                                new CharSequence[] { "保密", "男", "女" },// 列表显示的项目
                                selectItem,// 默认选中 第一个
                                new DialogInterface.OnClickListener() {// 设置条目
                                    public void onClick(DialogInterface dialog, int which) {// 响应事件
                                        // 选中之后, 执行这里的 代码
                                        selectItem = which;
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
