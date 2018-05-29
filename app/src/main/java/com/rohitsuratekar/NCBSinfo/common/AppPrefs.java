package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {

    private static final String FAVORITE_ORIGIN = "favorite_origin";
    private static final String FAVORITE_DESTINATION = "favorite_destination";
    private static final String FAVORITE_TYPE = "favorite_type";


    private Context context;
    private SharedPreferences prefs;

    public AppPrefs(Context context) {
        this.context = context;
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public String getFavoriteOrigin() {
        return get(FAVORITE_ORIGIN, "ncbs");
    }

    public String getFavoriteDestination() {
        return get(FAVORITE_DESTINATION, "iisc");
    }

    public String getFavoriteType() {
        return get(FAVORITE_TYPE, "shuttle");
    }

    public void setFavoriteOrigin(String origin) {
        put(FAVORITE_ORIGIN, origin);
    }

    public void setFavoriteDestination(String destination) {
        put(FAVORITE_DESTINATION, destination);
    }

    public void setFavoriteType(String type) {
        put(FAVORITE_TYPE, type);
    }


    private String get(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    private void put(String key, String value) {
        prefs.edit().putString(key, value);
    }
}
