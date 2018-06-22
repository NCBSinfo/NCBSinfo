package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPrefs {

    private static final String FAVORITE_ORIGIN = "favorite_origin";
    private static final String FAVORITE_DESTINATION = "favorite_destination";
    private static final String FAVORITE_TYPE = "favorite_type";
    private static final String EGG_ACTIVE = "egg_active";
    private static final String NOTIFICATIONS = "notifications";
    private static final String SETTINGS_DEFAULT_SET = "settings_default_set";
    private static final String LOCATION_SORT = "location_sort";
    private static final String DEVELOPER_ACTIVE = "developer_active";
    private static final String ADMIN_CODE = "admin_code";
    private static fi


    private SharedPreferences prefs;

    public AppPrefs(Context context) {
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

    public boolean isEggActive() {
        return get(EGG_ACTIVE, false);
    }

    public void eggActivated() {
        put(EGG_ACTIVE, true);
    }

    public void removeEggs() {
        put(EGG_ACTIVE, false);
    }

    public boolean areNotificationsAllowed() {
        return get(NOTIFICATIONS, true);
    }

    public void toggleNotifications() {
        put(NOTIFICATIONS, !get(NOTIFICATIONS, true));
    }

    public void setNotificationAllowed(boolean value) {
        put(NOTIFICATIONS, value);
    }

    public boolean isDefaultRouteSet() {
        return get(SETTINGS_DEFAULT_SET, false);
    }

    public void defaultRouteSet() {
        put(SETTINGS_DEFAULT_SET, true);
    }

    public int getLocationSort() {
        return get(LOCATION_SORT, 0);
    }

    public void setLocationSort(int loc) {
        put(LOCATION_SORT, loc);
    }

    public void setDeveloperActive() {
        put(DEVELOPER_ACTIVE, true);
    }

    public void removeDeveloper() {
        put(DEVELOPER_ACTIVE, false);
    }

    public boolean isDeveloperActive() {
        return get(DEVELOPER_ACTIVE, false);
    }

    public String getAdminCode() {
        return get(ADMIN_CODE, "");
    }

    public void setAdminCode(String code) {
        put(ADMIN_CODE, code);
    }

    private String get(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    private int get(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    private boolean get(String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    private void put(String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    private void put(String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    private void put(String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }


}
