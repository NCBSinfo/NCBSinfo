package com.rohitsuratekar.NCBSinfo.activities;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;

import java.util.ArrayList;
import java.util.List;

public class Helper {

    public static List<String> convertStringToList(String s) {
        List<String> list = new ArrayList<>();
        String[] arr = s.replace("{", "").replace("}", "").split(",");
        for (String m : arr) {
            list.add(m.trim());
        }
        return list;
    }

    public static TransportType getType(String string) {
        for (TransportType type : TransportType.values()) {
            if (type.toString().equals(string.toUpperCase())) {
                return type;
            }
        }
        return TransportType.SHUTTLE;
    }

}
