package com.bbb.bbdev1.run;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabsFragmentPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> fragments;
    int mNumTabs;
    public TabsFragmentPagerAdapter(@NonNull FragmentManager fm, ArrayList<Fragment> fragments, int numTabs) {
        super(fm);
        this.fragments = fragments;
        this.mNumTabs = numTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return mNumTabs;
    }

}
