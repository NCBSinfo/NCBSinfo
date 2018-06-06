package com.rohitsuratekar.NCBSinfo.common;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

    public static Date timeToDate(Date now, String s) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_TIME, Locale.ENGLISH);
        Calendar newCal = Calendar.getInstance();
        newCal.setTimeInMillis(now.getTime());
        Calendar subCal = Calendar.getInstance();
        subCal.setTimeInMillis(format.parse(s).getTime());
        newCal.set(Calendar.HOUR_OF_DAY, subCal.get(Calendar.HOUR_OF_DAY));
        newCal.set(Calendar.MINUTE, subCal.get(Calendar.MINUTE));
        return new Date(newCal.getTimeInMillis());
    }

    public static String displayTime(String string) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT_DISPLAY_TIME, Locale.ENGLISH);
        return format.format(timeToDate(new Date(), string));
    }

    public static String convertToString(Calendar calendar, String format) {
        SimpleDateFormat format1 = new SimpleDateFormat(format, Locale.ENGLISH);
        return format1.format(new Date(calendar.getTimeInMillis()));
    }

    public static void copyToClipBoardWithToast(Context context, String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("NCBSinfo", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
    }

    public static List<String> sortStringsByDate(List<String> input) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        List<String> stringList = new ArrayList<>(input);
        Collections.sort(stringList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                try {
                    return dateFormat.parse(s1).compareTo(dateFormat.parse(s2));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        return stringList;
    }
}
