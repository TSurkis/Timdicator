package com.tsurkis.timdicator;

import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;

/**
 * Created by T.Surkis on 1/27/18.
 *
 * Class for binding a ViewPager or Timdicator instance.
 *
 * The only reason to use this class outside of its internal usage
 * is if the unbinding event needs to happen before the view is
 * detached.
 */
class TimdicatorViewPagerBinder implements LifeCycleObserver {
    private Timdicator timdicator;

    private ViewPager viewPager;

    /**
     * Observer for the page change events of the ViewPager.
     * When a page changes, the index will be replaced by calling the setIndex method
     * of Timdicator.
     */
    private final ViewPager.OnPageChangeListener pageChangeObserver = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (timdicator != null) {
                timdicator.setIndex(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * General constructor
     */
    TimdicatorViewPagerBinder() {
    }

    /**
     * Attaches Timdicator to the ViewPager page observing events.
     *
     * @param timdicator
     * @param viewPager
     */
     void attach(@NonNull Timdicator timdicator, @NonNull ViewPager viewPager) {
        this.timdicator = timdicator;
        this.timdicator.setLifeCycleObserver(this);

        this.viewPager = viewPager;
        this.viewPager.addOnPageChangeListener(pageChangeObserver);
    }

    /**
     * Attaches Timdicator to the ViewPager page observing events.
     *
     * A dynamic method that allows the ViewPager dictate the number of circles instead of an
     * attribute. Consequently, If the number of circles does not match the number of pages, the view
     * will adjust itself accordingly.
     *
     * @param timdicator
     * @param viewPager
     */
    void attachDynamically(@NonNull Timdicator timdicator, @NonNull ViewPager viewPager) {
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() != timdicator.getNumberOfCircles()) {
            timdicator.setNumberOfCircles(viewPager.getAdapter().getCount());
            timdicator.requestLayout();
        }

        attach(timdicator, viewPager);
    }

    /**
     * Removes all the bindings that were made and frees up memory.
     *
     * This method is called internally when timdicator detaches from
     * the window.
     *
     * Using this method is only recommended if there is a need to unbind
     * Timdicator before it detaches from the window.
     */
    public void unbind() {
        if (timdicator != null) {
            timdicator = null;
        }

        if (viewPager != null) {
            viewPager.removeOnPageChangeListener(pageChangeObserver);
            viewPager = null;
        }
    }

    @Override
    public void onDetached() {
        unbind();
    }
}
