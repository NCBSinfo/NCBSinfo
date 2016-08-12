package com.rohitsuratekar.NCBSinfo.utilities;

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
        input = input.replace("(", "");
        input = input.replace(")", "");
        String[] split = input.split(",");
        for (String s : split) {
            s = s.replace("\"", "");
            output.add(s.replaceAll("\\s+", ""));
        }
        return output.toArray(new String[output.size()]);
    }

}
