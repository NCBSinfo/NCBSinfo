package com.rohitsuratekar.NCBSinfo.utilities;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class Converters {

    private final String TAG = getClass().getSimpleName();

    public Date convertToTalkDate(String Date, String Time) {
        Date returnDate = new Date();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        String DateString = Date + " " + Time;
        try {
            returnDate = eventFormat.parse(DateString);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in convertToDate :" + DateString);
        }
        return returnDate;
    }

    public String[] stringToarray(String input) {
        List<String> output = new ArrayList<>();
        input = input.replace("{", "");
        input = input.replace("}", "");
        String[] split = input.split(",");
        for (String s : split) {
            s = s.replace("\"", "");
            output.add(s.replaceAll("\\s+", ""));
        }
        return output.toArray(new String[output.size()]);
    }

}
