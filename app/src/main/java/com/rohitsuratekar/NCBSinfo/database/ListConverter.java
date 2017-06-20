package com.rohitsuratekar.NCBSinfo.database;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ListConverter {

    @TypeConverter
    public static List<String> toStringList(String listString) {
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return new Gson().fromJson(listString, type);
    }

    @TypeConverter
    public static String toString(List<String> listStirng) {
        return new Gson().toJson(listStirng);
    }
}