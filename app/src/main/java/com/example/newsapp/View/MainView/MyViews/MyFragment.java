package com.example.newsapp.View.MainView.MyViews;

import android.Manifest;
import android.content.ContentResolver;
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
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.newsapp.LocalUtils.SaveAccount;
import com.example.newsapp.Presenter.MyUserPresenter.MyUserPresenter;
import com.example.newsapp.R;
import com.example.newsapp.View.EditInfoView.EditiInfoActivity;
import com.example.newsapp.View.MainView.BaseFragment;
import com.example.newsapp.View.MainView.MyFavNews.MyFavNewsFragment;
import com.example.newsapp.View.MainView.MyNews.MyNewsFragment;
import com.example.newsapp.View.MainView.MyViews.IMyView;
import com.example.newsapp.View.PublishView.PublishActivity;
import com.example.newsapp.adapter.FrgAdapter;
import com.example.newsapp.bean.MyUserbean.MyUser;
import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyFragment extends BaseFragment<MyUserPresenter, IMyView> implements IMyView{
    List<BaseFragment> fragments= new ArrayList<>();
    TabLayout tabLayout;
    ViewPager vp;
    String[] titles = new String[]{"全部文章", "收藏"};
    Button editBtn;
    TextView nickNameText;
    TextView nickNameText2;
    TextView userNameText;
    TextView sexText;
    TextView signText;
    TextView moreInfoText;
    String nickName;
    String sex;
    String sign;
    String userName;
    SlidingUpPanelLayout slidingUpPanelLayout;
    TextView close;
    CircleImageView circleImageView;
    //读写权限
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.my_layout, container, false);
        slidingUpPanelLayout = view.findViewById(R.id.sliding_layout);
        nickNameText = view.findViewById(R.id.nick_name);
        nickNameText2 = view.findViewById(R.id.nick_name_2);
        userNameText = view.findViewById(R.id.username);
        sexText = view.findViewById(R.id.sex_text);
        signText = view.findViewById(R.id.sign);
        moreInfoText = view.findViewById(R.id.moreInfo);
        circleImageView = view.findViewById(R.id.user_icon);

        nickName = SaveAccount.getUserInfo(container.getContext()).get("nickName");
        sex = SaveAccount.getUserInfo(container.getContext()).get("sex");
        sign = SaveAccount.getUserInfo(container.getContext()).get("sign");
        userName = SaveAccount.getUserInfo(container.getContext()).get("userName");
        attachTab(view);
        userNameText.setText(userName);
        //如果本地没有缓存就从服务器获取
        if(nickName != null) {
            nickNameText.setText(nickName);
            nickNameText2.setText(nickName);
        } else {
            nickNameText.setText(userName);
            try {
                presenter.fetch(userName);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(sex != null) sexText.setText(sex);
        if(sign != null) signText.setText(sign);
        editBtn = view.findViewById(R.id.exit_btn);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditiInfoActivity.actionStart(container.getContext());
            }
        });
        moreInfoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(0.8f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
        close = view.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setAnchorPoint(0f);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
                    } else {
                        SelectImg();
                    }
                }
            }
        });
        return view;
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
        ContentResolver resolver = getActivity().getContentResolver();

        if (requestCode == 0) {
            try {
                Uri originalUri = data.getData();  //获得图片的uri
                Cursor cursor = resolver.query(originalUri, new String[] { android.provider.MediaStore.Images.ImageColumns.DATA }, null, null, null);
                cursor.moveToFirst();
                String mSelectPath = cursor.getString(0); //图片的路径
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
                Glide.with(getActivity()).load(bm).into(circleImageView);
                presenter.sendImage(userName, mSelectPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        nickName = SaveAccount.getUserInfo(getActivity()).get("nickName");
        sex = SaveAccount.getUserInfo(getActivity()).get("sex");
        sign = SaveAccount.getUserInfo(getActivity()).get("sign");
        if(nickName != null) {
            nickNameText.setText(nickName);
            nickNameText2.setText(nickName);
        }
        if(sex != null) sexText.setText(sex);
        if(sign != null) signText.setText(sign);
    }

    public void attachTab(View view) {
        //构造适配器
        fragments.add(new MyNewsFragment(userName));
        fragments.add(new MyFavNewsFragment());
        FrgAdapter adapter = new FrgAdapter(getChildFragmentManager(), fragments); //要用childFragmentManager，不然显示不出来

        //设定适配器
        vp = view.findViewById(R.id.viewpaper);
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(fragments.size()); //设置缓存的fragment的大小，不用一切换就销毁fragment

        tabLayout = view.findViewById(R.id.tablayout);
        for(int i=0;i<titles.length;i++){
            tabLayout.addTab(tabLayout.newTab());
            tabLayout.getTabAt(i).setText(titles[i]);
        }
        //ViewPager点击事件
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //选中ViewPager与TabLayout联动
                tabLayout.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //TabLayout点击事件
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //选中tab
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中tab时,TabLayout与ViewPager联动
                vp.setCurrentItem(tab.getPosition());
            }
            //未选择tab
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //重复选中tab
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void showErrorMessage(String msg) {

    }

    @Override
    protected MyUserPresenter createPresenter() {
        return new MyUserPresenter();
    }

    @Override
    public void showMyUser(MyUser myUser) {
        SaveAccount.saveExtraUserInfo(getActivity(), myUser.getNickName(), myUser.getSex(), myUser.getSign());
    }

    @Override
    public void showImagUrl(String imagUrl) {
        Log.d("RegisterModel", imagUrl);
    }
}
