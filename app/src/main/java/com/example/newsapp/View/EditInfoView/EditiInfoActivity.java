package com.example.newsapp.View.EditInfoView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Model.EditInfoModels.EditInfoModel;
import com.example.newsapp.Presenter.EditInfoPresenter.EditInfoPresenter;
import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;
import com.example.newsapp.View.BaseActivity;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditiInfoActivity extends BaseActivity<EditInfoPresenter, IEditInfoView> implements IEditInfoView {

    LinearLayout sexLinearLayout;
    TextView userNameText;
    TextView sexText;
    TextView save;
    TextView cancel;
    EditText nickNameEt;
    EditText signEt;
    CircleImageView circleImageView;
    int selectItem = 0;
    CharSequence[] sexList = new CharSequence[] { "保密", "男", "女" };
    String sex;
    String sign;
    String nickName;
    String userIcon;
    String userName;
    String mSelectPath;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editi_info);
        userName = SaveAccount.getUserInfo(this).get("userName");
        sex = SaveAccount.getUserInfo(this).get("sex");
        sign = SaveAccount.getUserInfo(this).get("sign");
        nickName = SaveAccount.getUserInfo(this).get("nickName");
        userIcon = SaveAccount.getUserInfo(this).get("userIcon");
        userNameText = findViewById(R.id.username);
        sexText = findViewById(R.id.sex_text);
        nickNameEt = findViewById(R.id.nick_name);
        signEt = findViewById(R.id.sign);
        circleImageView = findViewById(R.id.user_icon);
        userNameText.setText(userName);
        if(sex != null) sexText.setText(sex);
        if(nickName != null) nickNameEt.setText(nickName);
        if(sign != null) signEt.setText(sign);
        if(userIcon != null)  Glide.with(this).load(userIcon).into(circleImageView);
        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditiInfoActivity.this.finish();
            }
        });
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    presenter.fetch(userName, nickNameEt.getText().toString(), sexText.getText().toString(), signEt.getText().toString(), mSelectPath);
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
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(EditiInfoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(EditiInfoActivity.this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    } else {
                        SelectImg();
                    }
                }
            }
        });
    }

    private void SelectImg() {
        boolean isKitKatO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Intent getAlbum;
        if (isKitKatO) {
            getAlbum = new Intent(Intent.ACTION_PICK);
        } else {
            getAlbum = new Intent(Intent.ACTION_PICK);
        }
        getAlbum.setType("image/*");

        startActivityForResult(getAlbum, 0);

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();

        if (requestCode == 0) {
            try {
                Uri originalUri = data.getData();  //获得图片的uri
                Cursor cursor = resolver.query(originalUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                cursor.moveToFirst();
                mSelectPath = cursor.getString(0); //图片的路径
                ExifInterface exifInterface = new ExifInterface(mSelectPath);
                //bitmapUri = originalUri;
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                int bitMapHeight = bm.getHeight();
                int bitMapWidth = bm.getWidth();
                int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                // 定义矩阵对象
                Matrix matrix = new Matrix();
                if (orientation == 6) {
                    matrix.postRotate(90);
                } else if (orientation == 3) {
                    matrix.postRotate(180);
                } else if (orientation == 8) {
                    matrix.postRotate(270);
                }
                //显得到bitmap图片
                bm = Bitmap.createBitmap(bm, 0, 0, bitMapWidth, bitMapHeight, matrix, true);
                Glide.with(this).load(bm).into(circleImageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        if(msg.equals("fail")) {
            MyToast.toast("发布失败，请检查网络！");
        } else {
            MyToast.toast("修改成功!");
            userIcon = msg; //返回图片网络地址
            SaveAccount.saveExtraUserInfo(EditiInfoActivity.this, nickNameEt.getText().toString(), sexText.getText().toString(), signEt.getText().toString(), userIcon);
            this.finish();
        }
    }

    @Override
    public void showErrorMessage(String msg) {

    }
}
