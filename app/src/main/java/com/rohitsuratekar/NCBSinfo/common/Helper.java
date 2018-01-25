package com.rohitsuratekar.NCBSinfo.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 15-01-18 for NCBSinfo.
 * All code is released under MIT License.
 */

public class Helper {
    public static String timestamp() {
        return String.valueOf(Calendar.getInstance().getTimeInMillis());
    }

    public static List<String> convertToList(String string) {
        List<String> list = new ArrayList<>();
        string = string.replace("{", "").replace("}", "");
        String[] tempArray = string.split(",");
        for (String aTempArray : tempArray) {
            list.add(aTempArray.trim());
        }
        return list;
    }


}
