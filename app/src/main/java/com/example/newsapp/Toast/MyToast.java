package com.example.newsapp.Toast;

import android.widget.Toast;

import com.example.newsapp.Application.MyApplication;

public class MyToast {
    public static void toast(String s) {
        Toast toast = Toast.makeText(MyApplication.getContext(), null, Toast.LENGTH_SHORT);
        toast.setText(s);
        toast.show();
    }
}
