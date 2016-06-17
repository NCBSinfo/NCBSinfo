package com.rohitsuratekar.NCBSinfo.common;

import java.util.ArrayList;
import java.util.List;

public class Utilities {

    public String[] stringToarray(String input){
        List<String> output = new ArrayList<>();
        input = input.replace("{","");
        input = input.replace("}","");
        String[] split = input.split(",");
        for (String s: split){
            s = s.replace("\"","");
            output.add(s.replaceAll("\\s+",""));
        }
        return output.toArray(new String[output.size()]);
    }
}
