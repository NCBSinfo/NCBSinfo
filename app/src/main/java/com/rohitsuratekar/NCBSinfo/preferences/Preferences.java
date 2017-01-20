package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public abstract class Preferences {

    private final static String PREF_VERSION = "n1_";

    public Preferences(Context context) {
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private SharedPreferences pref;

    public SharedPreferences getPref() {
        return pref;
    }

    protected void put(String key, String value) {
        pref.edit().putString(PREF_VERSION + key, value).apply();
    }

    protected void put(String key, int value) {
        pref.edit().putInt(PREF_VERSION + key, value).apply();
    }

    protected void put(String key, boolean value) {
        pref.edit().putBoolean(PREF_VERSION + key, value).apply();
    }

    protected void put(String key, float value) {
        pref.edit().putFloat(PREF_VERSION + key, value).apply();
    }

    protected String get(String key, String defaultValue) {
        return pref.getString(PREF_VERSION + key, defaultValue);
    }

    protected int get(String key, int defaultValue) {
        return pref.getInt(PREF_VERSION + key, defaultValue);
    }

    protected boolean get(String key, boolean defaultValue) {
        return pref.getBoolean(PREF_VERSION + key, defaultValue);
    }

    protected float get(String key, float defaultValue) {
        return pref.getFloat(PREF_VERSION + key, defaultValue);
    }

    protected void delete(String key) {
        pref.edit().remove(PREF_VERSION + key).apply();
    }

    protected void clearAll() {
        pref.edit().clear().apply();
    }
}
