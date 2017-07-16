package com.rohitsuratekar.NCBSinfo.common;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * Created by Rohit Suratekar on 16-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class Helper {

    public static int getImage(String destination) {
        switch (destination.toLowerCase().trim()) {
            case "ncbs":
                return R.drawable.home_ncbs_image;
            case "iisc":
                return R.drawable.home_iisc_image;
            default:
                return R.drawable.home_ncbs_image;
        }
    }

    public static int getTypeColor(String type) {
        switch (type.trim()) {
            case "shuttle":
                return R.color.teal;
            case "buggy":
                return R.color.lime;
            default:
                return R.color.blue;
        }
    }
}
