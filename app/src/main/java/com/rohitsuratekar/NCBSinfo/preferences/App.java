package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.constants.AppConstants;

/**
 * This class deals with app related shared preferences
 */
public class App implements AppConstants {

    SharedPreferences pref;
    String APP_MODE = "app_mode";
    String APP_VERSION = "latestAppVersion";
    String APP_OPEN = "firstTimeAppOpen";
    String CANCELED_PAST_ALARMS = "cancelledPastAlarms";
    String FIRST_NOTIFICATION_EVENTS = "sendFirstNotificationEvents"; //Only for newly registered users
    String FIRST_NOTIFICATION_DASHBOARD = "sendFirstNotificationDashboard"; //Only for newly registered users

    protected App(SharedPreferences pref) {
        this.pref = pref;
    }

    public String getMode() {
        return pref.getString(APP_MODE, modes.OFFLINE.getValue());
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

    public boolean isAppOpenedFirstTime() {
        return pref.getBoolean(APP_OPEN, true);
    }

    public void setAppOpened() {
        pref.edit().putBoolean(APP_OPEN, false).apply();
    }

    public boolean arePastAlarmsCancelled() {
        return pref.getBoolean(CANCELED_PAST_ALARMS, false);
    }

    public void setPastAlarmsCancelled() {
        pref.edit().putBoolean(CANCELED_PAST_ALARMS, true).apply();
    }

    public boolean isFirstEventNotificationSent() {
        User user = new User(pref);
        return !user.getUserType().equals(userType.NEW_USER.getValue()) || pref.getBoolean(FIRST_NOTIFICATION_EVENTS, false);
    }

    public void firstEventNotificationSent() {
        pref.edit().putBoolean(FIRST_NOTIFICATION_EVENTS, true).apply();
    }

    public boolean isFirstDashboardNotificationSent() {
        User user = new User(pref);
        return !user.getUserType().equals(userType.NEW_USER.getValue()) || pref.getBoolean(FIRST_NOTIFICATION_DASHBOARD, false);
    }

    public void firstDashboardNotificationSent() {
        pref.edit().putBoolean(FIRST_NOTIFICATION_DASHBOARD, true).apply();
    }

}
