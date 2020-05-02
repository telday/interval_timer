package com.emomtimer.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IntervalTimerPagerAdapter extends FragmentPagerAdapter {
    public IntervalTimerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0){
            return new IntervalTimerFragment();
        }else {
            return new SettingsFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
