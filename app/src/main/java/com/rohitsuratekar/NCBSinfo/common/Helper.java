package com.rohitsuratekar.NCBSinfo.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SecretAdmin on 10/6/2017 for NCBSinfo.
 * All code is released under MIT License.
 */

public class Helper {

    public static String timestamp() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date());
    }
}
