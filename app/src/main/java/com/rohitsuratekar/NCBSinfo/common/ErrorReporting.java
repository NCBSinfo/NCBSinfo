package com.rohitsuratekar.NCBSinfo.common;

import com.secretbiology.helpers.general.Log;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 * <p>
 * This will serve as common gateway for error reporting.
 * TODO : add firebase reporting and analytics
 */

public class ErrorReporting {


    public static void wrongDateFormat(String s) {
        Log.error("Wrong date format " + s);
    }
}
