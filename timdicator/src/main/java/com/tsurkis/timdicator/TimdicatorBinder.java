package com.tsurkis.timdicator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;

/**
 * Created by T.Surkis on 1/27/18.
 *
 * General binding class to bind Timdicator to different types of views.
 */
public class TimdicatorBinder {

    /**
     * Attaches the given views and returns the binder.
     *
     * @param timdicator
     * @param viewPager
     * @return
     */
    public static TimdicatorViewPagerBinder attachViewPager(@NonNull Timdicator timdicator, @NonNull ViewPager viewPager) {
        TimdicatorViewPagerBinder timdicatorViewPagerBinder = new TimdicatorViewPagerBinder();
        timdicatorViewPagerBinder.attach(timdicator, viewPager);
        return timdicatorViewPagerBinder;
    }

    /**
     *
     * Attaches the given views dynamically and returns the binder.
     *
     * @param timdicator
     * @param viewPager
     * @return
     */
    public static TimdicatorViewPagerBinder attachViewPagerDynamically(@NonNull Timdicator timdicator, @NonNull ViewPager viewPager) {
        TimdicatorViewPagerBinder timdicatorViewPagerBinder = new TimdicatorViewPagerBinder();
        timdicatorViewPagerBinder.attachDynamically(timdicator, viewPager);
        return timdicatorViewPagerBinder;
    }

    public static void attachRecyclerView(@NonNull Timdicator timdicator, @NonNull RecyclerView recyclerView, @NonNull SnapHelper snapHelper) {
        TimdicatorRecyclerViewBinder timdicatorRecyclerViewBinder = new TimdicatorRecyclerViewBinder();
        timdicatorRecyclerViewBinder.attach(timdicator, recyclerView, snapHelper);
    }
}
