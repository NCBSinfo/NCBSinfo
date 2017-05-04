package com.rohitsuratekar.NCBSinfo.common;

import android.support.annotation.Nullable;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class Helper {

    @Nullable
    public static Class getClass(int navigationID) {
        for (CurrentActivity currentActivity : CurrentActivity.values()) {
            if (currentActivity.getNavigationID() == navigationID) {
                return currentActivity.getaClass();
            }
        }
        return null;
    }
}
