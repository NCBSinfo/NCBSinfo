package com.rohitsuratekar.NCBSinfo.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Code from : http://stackoverflow.com/questions/7814017/is-it-possible-to-disable-scrolling-on-a-viewpager
 */

public class RestrictedSwipeView extends ViewPager {

    private boolean isPagingEnabled = true;

    public RestrictedSwipeView(Context context) {
        super(context);
    }

    public RestrictedSwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagingEnabled(boolean b) {
        this.isPagingEnabled = b;
    }
}