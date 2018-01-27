package com.tsurkis.timdicator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

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
}
