package com.tsurkis.timdicator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Lightweight indicator class for a ViewPager.
 *
 * Timdicator = (Tim + Indicator)
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

    private LifeCycleObserver lifeCycleObserver;

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

    /*
        ******************************************************************************************

        General life cycle methods

        ******************************************************************************************
     */

    /**
     * Once this method is called all the calculations need to happen again.
     * Therefore, the current array of x coordinates is cleaned.
     */
    @Override
    public void requestLayout() {
        circleLocations = null;
        super.requestLayout();
    }

    /**
     * On detachment the life cycle observer, if one exists, will be notified of the
     * changes and then removed.
     */
    @Override
    protected void onDetachedFromWindow() {
        if (lifeCycleObserver != null) {
            lifeCycleObserver.onDetached();
            lifeCycleObserver = null;
        }

        super.onDetachedFromWindow();
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

        Setters & Getters

        ******************************************************************************************
     */

    /**
     * Updates the current index and redraws the view because the state has changed.
     *
     * Exists only for the usage of a binder class.
     *
     * @param index
     */
    void setIndex(int index) {
       this.currentIndex = index;
       invalidate();
    }

    /**
     * Returns the number of circles.
     *
     * Exists only for the usage of a binder class.
     *
     * @return
     */
    int getNumberOfCircles() {
        return numberOfCircles;
    }

    /**
     * Sets an interface that observers the life cycle of Timdicator.
     *
     * Exists only for the usage of a binder class.
     *
     */
    void setLifeCycleObserver(LifeCycleObserver lifeCycleObserver) {
        this.lifeCycleObserver = lifeCycleObserver;
    }

    /**
     * Setter for the color that represents the current page.
     *
     * @param context
     * @param color
     */
    public void setChosenCircleColor(@NonNull Context context, @ColorRes int color) {
        this.chosenCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.chosenCirclePaint.setColor(ContextCompat.getColor(context, color));
        this.chosenCirclePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Setter for the color that represents any page that is not the current page.
     * @param context
     * @param color
     */
    public void setDefaultCircleColor(@NonNull Context context, @ColorRes int color) {
        this.defaultCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.defaultCirclePaint.setColor(ContextCompat.getColor(context, color));
        this.defaultCirclePaint.setStyle(Paint.Style.FILL);
    }

    /**
     * Setter for the radius of a circle.
     *
     * @param context
     * @param circleRadiusInDp
     */
    public void setCircleRadiusInDp(@NonNull Context context, float circleRadiusInDp) {
        this.circleRadiusInPX = dpToPx(context, circleRadiusInDp);
    }

    /**
     * Setter for the distance between circles.
     *
     * @param context
     * @param distanceBetweenCircleInPX
     */
    public void setDistanceBetweenCircleInDp(@NonNull Context context, float distanceBetweenCircleInPX) {
        this.distanceBetweenCircleInPX = dpToPx(context, distanceBetweenCircleInPX);
    }

    /**
     * Setter for the number of circles the view needs to represent.
     *
     * Please note that this method only updates the variable and is not
     * responsible for redrawing or remeasuring the view.
     *
     * @param numberOfCircles
     */
    public void setNumberOfCircles(int numberOfCircles) {
        this.numberOfCircles = numberOfCircles;
    }
}
