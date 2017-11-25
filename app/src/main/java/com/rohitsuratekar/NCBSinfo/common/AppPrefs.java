package com.rohitsuratekar.NCBSinfo.common;

import android.content.Context;

import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Preferences;

/**
 * Created by Rohit Suratekar on 24-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class AppPrefs extends Preferences {

    private static final String APP_OPEN = "v52_app_open";
    private static final String LAST_OPEN = "v52_last_open";
    private static final String APP_MIGRATED = "v52_app_migrated";
    private static final String USER_LOGGED_IN = "user_logged_in";
    private static final String FAVORITE_ORIGIN = "favorite_origin";
    private static final String FAVORITE_DESTINATION = "favorite_destination";
    private static final String FAVORITE_TYPE = "favorite_type";
    private static final String USER_NAME = "user_name";
    private static final String USER_EMAIL = "user_email";
    private static final String NOTIFICATIONS = "notifications";
    private static final String SETTINGS_DEFAULT_SET = "settings_default_set";
    private static final String LAST_SYNC = "last_sync";
    private static final String MIGRATION_ID = "migration_id";
    private static final String LOCATION_SORT = "location_sort";
    private static final String INTRO_SEEN = "intro_seen";
    private static final String EGG_ACTIVE = "egg_active";

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

    public void setNotificationAllowed(boolean value) {
        put(NOTIFICATIONS, value);
    }

    public boolean isDefaultRouteSet() {
        return get(SETTINGS_DEFAULT_SET, false);
    }

    public void defaultRouteSet() {
        put(SETTINGS_DEFAULT_SET, true);
    }

    public boolean isUsedLoggedIn() {
        return get(USER_LOGGED_IN, false);
    }

    public void userLoggedIn() {
        put(USER_LOGGED_IN, true);
    }

    public void setLastSync(String timestamp) {
        put(LAST_SYNC, timestamp);
    }

    public String getLastSync() {
        return get(LAST_SYNC, "N/A");
    }

    public String getMigrationId() {
        return get(MIGRATION_ID, "v6");
    }

    public void setMigrationID(String id) {
        put(MIGRATION_ID, id);
    }

    public void clearPersonal() {
        delete(USER_NAME);
        delete(USER_EMAIL);
        delete(USER_LOGGED_IN);
        delete(LAST_SYNC);
        delete(SETTINGS_DEFAULT_SET);
        delete(NOTIFICATIONS);
    }

    public int getLocationSort() {
        return get(LOCATION_SORT, 0);
    }

    public void setLocationSort(int loc) {
        put(LOCATION_SORT, loc);
    }

    public boolean isIntroSeen() {
        return get(INTRO_SEEN, false);
    }

    public void introSeen() {
        put(INTRO_SEEN, true);
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
}
