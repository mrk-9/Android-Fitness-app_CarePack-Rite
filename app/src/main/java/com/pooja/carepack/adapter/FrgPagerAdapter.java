package com.pooja.carepack.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


public class FrgPagerAdapter extends FragmentPagerAdapter {

    private final Fragment[] frgs;

    public FrgPagerAdapter(FragmentManager fm, Fragment[] frgs) {
        super(fm);
        this.frgs = frgs;
    }

    @Override
    public int getCount() {
        return frgs.length;
    }

    @Override
    public Fragment getItem(int pos) {
        return frgs[pos];
    }
}