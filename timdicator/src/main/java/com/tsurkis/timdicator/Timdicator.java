package com.tsurkis.timdicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Lightweight indicator class for ViewPager.
 *
 * Created by T.Surkis.
 */
public class Timdicator extends View {
    private static final int DEFAULT_CHOSEN_CIRCLE_COLOR = Color.parseColor("#ffffff"), DEFAULT_CIRCLE_COLOR = Color.parseColor("#000000");
    private static final int DEFAULT_DISTANCE_BETWEEN_CIRCLES_IN_DP = 5, DEFAULT_CIRCLE_RADIUS_IN_DP = 5, DEFAULT_NUMBER_OF_CIRCLES = 0;

    private Paint defaultCirclePaint, chosenCirclePaint;

    private float circleRadiusInPX, distanceBetweenCircleInPX;

    private int numberOfCircles;

    private float[] circleLocations;
    private float yLocation;

    private int currentIndex = 0;

    public Timdicator(Context context) {
        super(context);

        initializeParameters(context, null);
    }

    public Timdicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initializeParameters(context, attrs);
    }

    public Timdicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initializeParameters(context, attrs);
    }

    @TargetApi(21)
    public Timdicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initializeParameters(context, attrs);
    }

    /**
     * All the constructor parameters are routed to this method for a unified initialization.
     *
     * @param context
     * @param attributes
     */
    private void initializeParameters(Context context, AttributeSet attributes) {
        int chosenCircleColor;
        int circleColor;

        if (attributes != null) {
            /*
                custom attribute extraction
             */
            TypedArray attributeArray = context.obtainStyledAttributes(attributes, R.styleable.Timdicator);

            circleRadiusInPX =
                    attributeArray.getDimension(
                            R.styleable.Timdicator_circle_radius,
                            dpToPx(context, DEFAULT_CIRCLE_RADIUS_IN_DP));

            distanceBetweenCircleInPX =
                    attributeArray.getDimension(
                            R.styleable.Timdicator_distance_between_circles,
                            dpToPx(context, DEFAULT_DISTANCE_BETWEEN_CIRCLES_IN_DP));

            numberOfCircles =
                    attributeArray.getInt(
                            R.styleable.Timdicator_number_of_circles,
                            DEFAULT_NUMBER_OF_CIRCLES);

            chosenCircleColor =
                    attributeArray.getColor(
                            R.styleable.Timdicator_chosen_circle_color,
                            DEFAULT_CHOSEN_CIRCLE_COLOR);

            circleColor =
                    attributeArray.getColor(
                            R.styleable.Timdicator_default_circle_color,
                            DEFAULT_CIRCLE_COLOR);

            attributeArray.recycle();
        } else {
            /*
                default value initialization
             */
            circleRadiusInPX = dpToPx(context, DEFAULT_CIRCLE_RADIUS_IN_DP);
            distanceBetweenCircleInPX = dpToPx(context, DEFAULT_DISTANCE_BETWEEN_CIRCLES_IN_DP);
            numberOfCircles = DEFAULT_NUMBER_OF_CIRCLES;
            chosenCircleColor = DEFAULT_CHOSEN_CIRCLE_COLOR;
            circleColor = DEFAULT_CIRCLE_COLOR;
        }

        /*
            paint objects initialization
         */

        if (defaultCirclePaint == null) {
            defaultCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            defaultCirclePaint.setColor(circleColor);
            defaultCirclePaint.setStyle(Paint.Style.FILL);
        }

        if (chosenCirclePaint == null) {
            chosenCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            chosenCirclePaint.setColor(chosenCircleColor);
            chosenCirclePaint.setStyle(Paint.Style.FILL);
        }
    }

    /*
        ******************************************************************************************

        Drawing

        ******************************************************************************************
     */

    /**
     * Through iteration on our x coordinate array {X1, X2, ... ,Xn} the method draws
     * the circle - one that represents the current page and a default circle.
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        if (circleLocations != null && circleLocations.length != 0) {
            for (int i = 0; i < numberOfCircles; ++i) {
                if (i == currentIndex) {
                    canvas.drawCircle(circleLocations[i], yLocation, circleRadiusInPX, chosenCirclePaint);
                } else {
                    canvas.drawCircle(circleLocations[i], yLocation, circleRadiusInPX, defaultCirclePaint);
                }
            }
        }
    }

    /*
        ******************************************************************************************

        Measurements

        ******************************************************************************************
     */

    /**
     * Measurements.
     *
     * The y coordinate will always be in the middle of the view height.
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean isMatchParentFlagOn = MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY;
        measureIndicesOfIndicatorStations(isMatchParentFlagOn);

        int measuredWidth = numberOfCircles > 0 ? measureWidth(widthMeasureSpec) : 0;
        int measuredHeight = numberOfCircles > 0 ? measureHeight(heightMeasureSpec) : 0;

        yLocation = measuredHeight / 2;

        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    /**
     * Retrieves the desired height by the view.
     *
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (circleRadiusInPX * 2 + getPaddingTop() + getPaddingBottom());
        }

        return result;
    }

    /**
     * Retrieves the desired width by the view.
     *
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result;
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = (int) (circleLocations[numberOfCircles-1] + circleRadiusInPX + getPaddingRight());
        }

        return result;
    }

    /**
     * Measures and saves the X coordinates for each given circle.
     *
     * @param shouldAddScreenOffset - a special parameter in case the view needs to
     *                                be at the width of the entire container.
     *
     *  shouldAddScreenOffset = false
     * *******************************************************************************
     *
     *  padding  radius         space     radius         padding
     * |-------||------*------||-----|...|------*------||-------|
     *                 X1                       Xn
     *
     *
     *
     *  shouldAddScreenOffset = true
     * *******************************************************************************
     *
     *  padding  radius         space     radius         padding
     * |-------||------*------||-----|...|------*------||-------|
     *                 X1                       Xn
     * |----------------------------------------------------------------------------|
     *                              container width
     *                                                             remaining space
     *                                                          |-------------------|
     *
     *
     *  As a choice of aesthetics, if the view needs to match the container width,
     *  it would be centered.
     *  This is done by taking the remaining piece of the width and dividing it by half.
     *  Each half is added to each side of the view.
     *
     *
     *
     *   piece1    padding  radius         space     radius         padding  piece2
     * |--------||-------||------*------||-----|...|------*------||-------||--------|
     *                 X1                       Xn
     * |----------------------------------------------------------------------------|
     *                              container width
     *
     * *******************************************************************************
     *
     * n = number of circles
     *
     * {X1, X2, ..., Xn} = array representing the X coordinate of each circle center
     *
     */
    private void measureIndicesOfIndicatorStations(boolean shouldAddScreenOffset) {
        if (numberOfCircles > 0 && circleLocations == null) {
            circleLocations = new float[numberOfCircles];

            float xCounter = getPaddingLeft() + circleRadiusInPX;
            float spaceBetweenCircles = circleRadiusInPX * 2 + distanceBetweenCircleInPX;

            for (int index = 0; index < numberOfCircles; index++) {
                circleLocations[index] = xCounter;

                xCounter += spaceBetweenCircles;
            }

            if (shouldAddScreenOffset) {
                int screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
                int widthOfView = (int) (circleLocations[numberOfCircles-1] + circleRadiusInPX + getPaddingRight());
                int spaceToAddFromEachSide = (screenWidth - widthOfView) / 2;

                for (int index = 0; index < circleLocations.length; index++) {
                    circleLocations[index] += spaceToAddFromEachSide;
                }
            }
        }
    }

    /**
     * Once this method is called all the calculations need to happen again.
     * Therefore, the current array of x coordinates is cleaned.
     */
    @Override
    public void requestLayout() {
        circleLocations = null;
        super.requestLayout();
    }

    /*
        ******************************************************************************************

        Binding

        ******************************************************************************************
     */

    /**
     * Reference to the corresponding ViewPager is saved to effectively remove the observer
     * when the view dies.
     */
    private WeakReference<ViewPager> boundViewPager;

    /**
     * Observer for the page change events of the ViewPager.
     * When a page changes, the index will be replaced and the entire view will be redrawn by
     * calling invalidate().
     */
    private final ViewPager.OnPageChangeListener pageChangeObserver = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Timdicator.this.currentIndex = position;
            invalidate();
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    /**
     * Attaches the view to the ViewPager page observing events.
     *
     * @param viewPager
     */
    public void attach(@NonNull ViewPager viewPager) {

        if (numberOfCircles != 0) {
            boundViewPager = new WeakReference<>(viewPager);
            viewPager.addOnPageChangeListener(pageChangeObserver);
        }
    }
    /**
     * Attaches the view to the ViewPager page observing events.
     *
     * A dynamic method that allows the ViewPager dictate the number of circles instead of an
     * attribute. Consequently, If the number of circles does not match the number of pages, the view
     * will adjust itself accordingly.
     *
     * @param viewPager
     */
    public void attachDynamically(@NonNull ViewPager viewPager) {
        if (viewPager.getAdapter() != null && viewPager.getAdapter().getCount() != numberOfCircles) {
            numberOfCircles = viewPager.getAdapter().getCount();
            requestLayout();
        }

        attach(viewPager);
    }

    /**
     * On detachment the view will remove its listener from the ViewPager.
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (boundViewPager != null && boundViewPager.get() != null) {
            boundViewPager.get().removeOnPageChangeListener(pageChangeObserver);
        }
    }

    /*
        ******************************************************************************************

        Helper Methods

        ******************************************************************************************
     */

    /**
     * Helper method to convert dp values to pixel values.
     *
     * @param context
     * @param value
     * @return
     */
    private static float dpToPx(final Context context, final float value) {
        return value * context.getResources().getDisplayMetrics().density;
    }

    /*
        ******************************************************************************************

        Setters

        ******************************************************************************************
     */

    public void setDefaultCirclePaint(Paint defaultCirclePaint) {
        this.defaultCirclePaint = defaultCirclePaint;
    }

    public void setChosenCirclePaint(Paint chosenCirclePaint) {
        this.chosenCirclePaint = chosenCirclePaint;
    }

    public void setCircleRadiusInDp(Context context, float circleRadiusInDp) {
        this.circleRadiusInPX = dpToPx(context, circleRadiusInDp);
    }

    public void setDistanceBetweenCircleInDp(Context context, float distanceBetweenCircleInPX) {
        this.distanceBetweenCircleInPX = dpToPx(context, distanceBetweenCircleInPX);
    }

    public void setNumberOfCircles(int numberOfCircles) {
        this.numberOfCircles = numberOfCircles;
    }
}
