package com.rohitsuratekar.NCBSinfo.maplist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Custom CardView class that intercepts touch events that would otherwise be passed to its
 * children. Useful when one of the children is a MapView, which independently receives touch event
 * to markers and info windows.
 */
public class MapCardView extends CardView {
    public MapCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
