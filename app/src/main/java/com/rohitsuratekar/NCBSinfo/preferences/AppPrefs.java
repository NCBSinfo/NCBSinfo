package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.pm.PackageManager;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.secretbiology.helpers.general.Log;

public class AppPrefs extends Preferences {

    private static final String APP_VERSION_NAME = "app_version_name";
    private static final String APP_VERSION = "app_version";
    private static final String APP_MIGRATED = "app_migrated";
    private static final String FIRST_OPEN = "first_open";
    private static final String FAVORITE_ROUTE = "favorite_route";
    private static final String FAVORITE_ORIGIN = "favorite_origin";
    private static final String FAVORITE_DESTINATION = "favorite_destination";
    private static final String FAVORITE_TYPE = "favorite_type";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String NOTIFICATIONS = "notifications";
    private static final String HURRY_UP_TIME = "hurry_up";
    private static final String IS_USED_LOGGED_IN = "isUserLoggedIn";
    private static final String LAST_SYNC = "last_sync";

    private Context context;

    public AppPrefs(Context context) {
        super(context);
        this.context = context;
    }

    public boolean isUserLoggedIn() {
        return get(IS_USED_LOGGED_IN, false);
    }

    public void userLoggedIn() {
        put(IS_USED_LOGGED_IN, true);
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

    public String getFavoriteOrigin() {
        return get(FAVORITE_ORIGIN, "ncbs");
    }

    public String getFavoriteDestination() {
        return get(FAVORITE_DESTINATION, "iisc");
    }

    public String getFavoriteType() {
        return get(FAVORITE_TYPE, TransportType.SHUTTLE.toString());
    }

    public void setFavoriteOrigin(String origin) {
        put(FAVORITE_ORIGIN, origin);
    }

    public void setFavoriteDestination(String destination) {
        put(FAVORITE_DESTINATION, destination);
    }

    public void setFavoriteType(TransportType type) {
        put(FAVORITE_TYPE, type.toString());
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

    public String getLastSync() {
        return get(LAST_SYNC, "Unknown");
    }

    public void setLastSync(String time) {
        put(LAST_SYNC, time);
    }

    public void clear() {
        clearAll();
    }
}
