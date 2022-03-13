package com.example.newsapp.View.MainView.MyView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.newsapp.R;
import com.example.newsapp.Toast.MyToast;


public class MyNewsFragment extends Fragment {
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = LayoutInflater.from(container.getContext()).inflate(R.layout.text, container, false);
       textView = view.findViewById(R.id.text);
       return view;
    }
}
