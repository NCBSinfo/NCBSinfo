package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.pm.PackageManager;

import com.secretbiology.helpers.general.Log;

public class AppPrefs extends Preferences {

    private static final String APP_VERSION_NAME = "app_version_name";
    private static final String APP_VERSION = "app_version";
    private static final String APP_MIGRATED = "app_migrated";
    private static final String FIRST_OPEN = "first_open";
    private static final String FAVORITE_ROUTE = "favorite_route";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String NOTIFICATIONS = "notifications";
    private static final String HURRY_UP_TIME = "hurry_up";

    private Context context;

    public AppPrefs(Context context) {
        super(context);
        this.context = context;
    }

    public boolean isFirstOpen() {
        return get(FIRST_OPEN, true);
    }

    public void appOpened() {
        put(FIRST_OPEN, false);
    }

    public int getFavoriteRoute() {
        return get(FAVORITE_ROUTE, 0);
    }

    public void setFavoriteRoute(int route) {
        put(FAVORITE_ROUTE, route);
    }

    public String getUsername() {
        return get(USER_NAME, "Username");
    }

    public String getCurrentVersionName() {
        return get(APP_VERSION_NAME, "null");
    }

    public int getCurrentVersion() {
        return get(APP_VERSION, 0);
    }

    public void updateVersion() {
        String versionName = "";
        try {
            put(APP_VERSION, context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionCode);
            put(APP_VERSION_NAME, context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.error("Error retrieving app version");
        }
    }

    public void setUserName(String username) {
        put(USER_NAME, username);
    }

    public String getUserEmail() {
        return get(USER_EMAIL, "email@domain.com");
    }

    public void setUserEmail(String email) {
        put(USER_EMAIL, email);
    }

    public void notificationAllowed(boolean value) {
        put(NOTIFICATIONS, value);
    }

    public boolean isNotificationAllowed() {
        return get(NOTIFICATIONS, true);
    }

    public void setHurryUpTime(int time) {
        put(HURRY_UP_TIME, time);
    }

    public int getHurryUpTime() {
        return get(HURRY_UP_TIME, 5);
    }

    public boolean isAppMigrated() {
        return get(APP_MIGRATED, false);
    }

    public void appMigrated() {
        put(APP_MIGRATED, true);
    }
}
