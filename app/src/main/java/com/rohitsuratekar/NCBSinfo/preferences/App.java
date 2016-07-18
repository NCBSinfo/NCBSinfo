package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.utilities.General;

/**
 * This class deals with app related shared preferences
 */
public class App implements AppConstants {

    SharedPreferences pref;
    Context context;
    String APP_MODE = "app_mode";
    String APP_VERSION = "latestAppVersion";
    String APP_VERSION_NAME = "latestAppVersionName";
    String APP_OPEN = "firstTimeAppOpen";
    String CANCELED_PAST_ALARMS = "cancelledPastAlarms";
    String FIRST_NOTIFICATION_EVENTS = "sendFirstNotificationEvents"; //Only for newly registered users
    String FIRST_NOTIFICATION_DASHBOARD = "sendFirstNotificationDashboard"; //Only for newly registered users
    String LAST_LOGIN = "lastLogin";
    String OPEN_COUNT = "openCount";
    String NOTIFICATION_OPENED = "notificationOpened";
    String LATEST_APP = "latestApp";

    protected App(SharedPreferences pref, Context context) {
        this.pref = pref;
        this.context = context;
    }

    public modes getMode() {
        for (modes m : modes.values()) {
            if (m.getValue().equals(pref.getString(APP_MODE, modes.OFFLINE.getValue()))) {
                return m;
            }
        }
        return modes.OFFLINE; //Default
    }

    public void setMode(modes mode) {
        switch (mode) {
            case ONLINE:
                pref.edit().putString(APP_MODE, modes.ONLINE.getValue()).apply();
                break;
            case OFFLINE:
                pref.edit().putString(APP_MODE, modes.OFFLINE.getValue()).apply();
                break;
        }
    }

    public int getAppVesion() {
        return pref.getInt(APP_VERSION, 0);
    }

    public void setAppVersion(int currentAppVesion) {
        pref.edit().putInt(APP_VERSION, currentAppVesion).apply();
    }

    public String getAppVersionName() {
        return pref.getString(APP_VERSION_NAME, "1.0");
    }

    public void setAppVersionName(String name) {
        pref.edit().putString(APP_VERSION_NAME, name).apply();
    }

    public boolean isAppOpenedFirstTime() {
        return pref.getBoolean(APP_OPEN, true);
    }

    public void setAppOpenedFirstTime() {
        pref.edit().putBoolean(APP_OPEN, false).apply();
    }

    public boolean arePastAlarmsCancelled() {
        return pref.getBoolean(CANCELED_PAST_ALARMS, false);
    }

    public void setPastAlarmsCancelled() {
        pref.edit().putBoolean(CANCELED_PAST_ALARMS, true).apply();
    }

    public boolean isFirstEventNotificationSent() {
        User user = new User(pref, context);
        return !user.getUserType().getValue().equals(userType.NEW_USER.getValue()) || pref.getBoolean(FIRST_NOTIFICATION_EVENTS, false);
    }

    public void firstEventNotificationSent() {
        pref.edit().putBoolean(FIRST_NOTIFICATION_EVENTS, true).apply();
    }

    public boolean isFirstDashboardNotificationSent() {
        User user = new User(pref, context);
        return !user.getUserType().getValue().equals(userType.NEW_USER.getValue()) || pref.getBoolean(FIRST_NOTIFICATION_DASHBOARD, false);
    }

    public void firstDashboardNotificationSent() {
        pref.edit().putBoolean(FIRST_NOTIFICATION_DASHBOARD, true).apply();
    }

    public String getLastLogin() {
        return pref.getString(LAST_LOGIN, new General().timeStamp());
    }

    public void setLastLogin(String time) {
        pref.edit().putString(LAST_LOGIN, time).apply();
    }

    public void addOpenCount() {
        pref.edit().putInt(OPEN_COUNT, getOpenCount() + 1).apply();
    }

    public int getOpenCount() {
        return pref.getInt(OPEN_COUNT, 1);
    }

    public int getNotificationOpened() {
        return pref.getInt(NOTIFICATION_OPENED, 0);
    }

    public void addNotificationOpened() {
        pref.edit().putInt(NOTIFICATION_OPENED, getNotificationOpened() + 1).apply();
    }

    public boolean isLatestApp() {
        return getAppVesion() >= pref.getInt(LATEST_APP, getAppVesion());
    }

    public void setLatestApp(int version) {
        pref.edit().putInt(LATEST_APP, version).apply();
    }

}
