package com.example.newsapp.Toast;

import android.widget.Toast;

import com.example.newsapp.Application.MyApplication;

public class MyToast {
    public static void toast(String s) {
        Toast.makeText(MyApplication.getContext(), s, Toast.LENGTH_SHORT).show();
    }
}
