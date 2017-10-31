package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;

import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Preferences;

/**
 * Created by Rohit Suratekar on 24-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class AppPrefs extends Preferences {

    private final String APP_OPEN = "v52_app_open";
    private final String LAST_OPEN = "v52_last_open";
    private final String APP_MIGRATED = "v52_app_migrated";
    private static final String FAVORITE_ORIGIN = "favorite_origin";
    private static final String FAVORITE_DESTINATION = "favorite_destination";
    private static final String FAVORITE_TYPE = "favorite_type";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String NOTIFICATIONS = "notifications";

    public AppPrefs(Context context) {
        super(context);
    }

    public void clear() {
        clearAll();
    }

    public boolean isFirstTime() {
        return !get(APP_OPEN, false);
    }

    public void appOpenedFirstTime() {
        put(APP_OPEN, true);
    }

    public void appOpened() {
        put(LAST_OPEN, General.timeStamp());
    }

    public String lastOpened() {
        return get(LAST_OPEN, "N/A");
    }

    public void appMigrated() {
        put(APP_MIGRATED, true);
    }

    public boolean isAppMigrated() {
        return get(APP_MIGRATED, false);
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

    public String getUserEmail() {
        return get(USER_EMAIL, "email@domain.com");
    }

    public String getUserName() {
        return get(USER_NAME, "username");
    }

    public void setUserName(String name) {
        put(USER_NAME, name);
    }

    public void setUserEmail(String email) {
        put(USER_EMAIL, email);
    }

    public boolean areNotificationsAllowed() {
        return get(NOTIFICATIONS, true);
    }

    public void toggleNotifications() {
        put(NOTIFICATIONS, !get(NOTIFICATIONS, true));
    }


}
