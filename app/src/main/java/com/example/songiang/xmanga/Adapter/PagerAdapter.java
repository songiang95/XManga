package com.example.songiang.xmanga.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.songiang.xmanga.View.ViewImageFragment;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private ArrayList<String> imgsUrl;
    public PagerAdapter(FragmentManager fm, ArrayList<String> imgsUrl) {

        super(fm);
        this.imgsUrl = imgsUrl;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = new ViewImageFragment();
        Bundle bundle = new Bundle();
        bundle.putString("URL",imgsUrl.get(i));
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public int getCount() {
        return imgsUrl.size();
    }
}
