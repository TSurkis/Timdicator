package com.tsurkis.timdicatorsampleapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by T.Surkis on 12/24/17.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    private List<String> fragmentColors;

    public PagerAdapter(FragmentManager fm, List<String> colors) {
        super(fm);

        this.fragmentColors = colors;
    }

    @Override
    public Fragment getItem(int position) {
        PagerFragment pagerFragment = new PagerFragment();

        Bundle bundle = new Bundle();
        bundle.putString(PagerFragment.KEY_COLOR, fragmentColors.get(position));

        pagerFragment.setArguments(bundle);

        return pagerFragment;
    }

    @Override
    public int getCount() {
        return fragmentColors.size();
    }
}
