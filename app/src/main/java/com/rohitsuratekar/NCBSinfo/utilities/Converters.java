package com.rohitsuratekar.NCBSinfo.utilities;

import com.rohitsuratekar.NCBSinfo.constants.DateFormats;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class Converters {

    private final String TAG = getClass().getSimpleName();

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

    //Converts "HH:mm" format to "hh:mm a" format
    public String[] convertToReadableTime(String[] trips) {
        String[] finalStrings = new String[trips.length];
        for (int i = 0; i < trips.length; i++) {
            finalStrings[i] = new DateConverters().convertFormat(trips[i], DateFormats.TIME_12_HOURS_STANDARD);
        }
        return finalStrings;
    }

}
