package com.example.newsapp.View.PublishView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.newsapp.Presenter.NewsContentPresenter.NewsContentPresenter;
import com.example.newsapp.Presenter.PublishPresenter.PublishPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.BaseActivity;
import com.example.newsapp.View.IBaseView;
import com.example.newsapp.View.NewsContentView.INewsContentView;
import com.example.newsapp.View.Activity.showPublishContentActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class PublishActivity extends BaseActivity<PublishPresenter, IPublishView> implements IPublishView {

    ScrollView scrollView;
    EditText et;
    LinearLayout ll;
    Button photos;
    TextView cancel;
    TextView publish;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    int screenHeight;
    int screenWidth;
    List<String> imagPaths = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        //2、通过Resources获取
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenHeight = dm.heightPixels;
        screenWidth = dm.widthPixels;
        scrollView = findViewById(R.id.scrollView);
        et = findViewById(R.id.et);
        ll = findViewById(R.id.ll);
        publish = findViewById(R.id.publish);
        cancel = findViewById(R.id.cancel);
        photos = findViewById(R.id.photos);
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectImg();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PublishActivity.this.finish();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    presenter.fetch(imagPaths);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }
    }

    @Override
    protected PublishPresenter createPresenter() {
        return new PublishPresenter();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

    public static void actionStart(Context context) {
        Intent intent = new Intent(context, PublishActivity.class);
        context.startActivity(intent);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();

        if (requestCode == 0) {
            try {
                Uri originalUri = data.getData();  //获得图片的uri
                Cursor cursor = resolver.query(originalUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                cursor.moveToFirst();
                String mSelectPath = cursor.getString(0); //图片的路径
                imagPaths.add(mSelectPath);
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
                if(bitMapWidth > screenWidth) {
                    matrix.postScale(screenWidth * 1.0f / bitMapWidth * 1.0f,screenWidth * 1.0f / bitMapWidth * 1.0f);
                }
                //显得到bitmap图片
                bm = Bitmap.createBitmap(bm, 0, 0, bitMapWidth, bitMapHeight, matrix, true);
                ImageSpan imageSpan = new ImageSpan(this, bm);

                String tempUrl = "<img src=\"" + mSelectPath + "\" />";
                SpannableString spannableString = new SpannableString(tempUrl);
                spannableString.setSpan(imageSpan, 0, tempUrl.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                // 将选择的图片追加到EditText中光标所在位置
                int index = et.getSelectionStart();
                // 获取光标所在位置
                Editable edit_text = et.getEditableText();
                if(index > 0 && edit_text.charAt(index - 1) == '\n'){ //位于除第一行以外的行首
                    edit_text.insert(index, spannableString);
                    edit_text.insert(index + spannableString.length(), "\n");
                } else if (index > 0) { //位于行中
                    edit_text.append("\n");
                    edit_text.append(spannableString);
                    edit_text.append("\n");
                } else { //位于第一行
                    edit_text.append(spannableString);
                    edit_text.append("\n");
                }
                ll.clearFocus();
                ll.setFocusable(true);
                ll.setFocusableInTouchMode(true);
                ll.requestFocus();
                handler.sendEmptyMessage(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    break;
                default:

                    break;
            }
        }
    };

    @Override
    public void showErrorMessage(String msg) {

    }

    //拿到服务器返回的图片地址
    @Override
    public void showImagUrls(String[] imagUrls) {
       showPublishContentActivity.actionStart(this, getEditText(imagUrls));
    }

    /**
     * 用网络路径替换本地路径
     * @param imagUrls 上传图片后得到的网络路径集合
     * @return editText 替换后的文本内容(用于最终上传)
     */
    public String getEditText(String[] imagUrls) {
        Editable edit = et.getText();
        String cont = edit.toString();
        ImageSpan[] spans = edit.getSpans(0,edit.length(),
                ImageSpan.class);

        int size = spans.length;
        for (int i = 0; i < size ; i++) {
            int start = edit.getSpanStart(spans[i]) + 9; //src的位置
            int end = edit.getSpanEnd(spans[i]) - 3;
            String path = edit.toString().substring(start, end);
            cont = cont.replace(path, imagUrls[i]);
        }
        String head="<style>\n" +
                "  \n" +
                "img{\n" +
                " max-width:100%;\n" +
                " height:auto;\n" +
                "}\n" +
                "  \n" +
                "</style>";
        return head + cont;
    }
}
