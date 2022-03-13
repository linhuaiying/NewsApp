package com.example.newsapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.newsapp.View.MainView.BaseFragment;

import java.util.List;

public class FrgAdapter extends FragmentPagerAdapter {
    List<BaseFragment> fragments;
    public FrgAdapter(@NonNull FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
