package com.example.newsapp.View.PublishView;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.newsapp.R;


public class PublishActivity extends AppCompatActivity {

    ScrollView scrollView;
    EditText et;
    LinearLayout ll;
    Button photos;
    TextView cancel;
    TextView publish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();

        if (requestCode == 0) {
            try {
                Uri originalUri = data.getData();  //获得图片的uri
                //bitmapUri = originalUri;
                bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                //显得到bitmap图片
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight());
                ImageSpan imageSpan = new ImageSpan(this, bm);

                String tempUrl = "<img src=\"" + "http://travel.shm.com.cn/files/2022-01/17/5253162_ff37a774-764f-4311-91af-5cb906aeb414.jpg" + "\" />";
                SpannableString spannableString = new SpannableString(tempUrl);
                /* 加载服务器返回的图片
                final Bitmap[] bitmap = new Bitmap[1];
                Runnable runnable = new Runnable() {
                    public void run() {
                        try {
                            bitmap[0] = Glide.with(MyApplication.getContext()).asBitmap().load("http://travel.shm.com.cn/files/2022-01/17/5253162_ff37a774-764f-4311-91af-5cb906aeb414.jpg").submit().get();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                };
                Thread thread = new Thread(runnable);
                thread.start();
                thread.join();
                ImageSpan imageSpan2 = new ImageSpan(this, bitmap[0]);
                 */
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

}
