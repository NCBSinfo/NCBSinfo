package com.rohitsuratekar.NCBSinfo.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Helper {

    public static String FORMAT_TIME = "HH:mm";
    public static String FORMAT_DISPLAY_TIME = "hh:mm a";
    public static String FORMAT_TIMESTAMP = "yyyy-MM-dd'T'HH:mm:ssZ";

    public static int randomInt(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt((max - min) + 1);
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

    public static String timestamp() {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIMESTAMP, Locale.ENGLISH);
        return format.format(new Date());
    }
}
