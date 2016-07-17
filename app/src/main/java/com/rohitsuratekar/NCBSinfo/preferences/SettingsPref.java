package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 12-07-16.
 */
public class SettingsPref {

    SharedPreferences pref;
    Context context;
    String SUMMARY_DEFAULT_ROUTE = "summaryDefaultRoute";
    String SUMMARY_HURRY_UP = "summaryHurryUp";
    String EVENT_NOTIFICATION = "eventNotificationPref";
    String IMPORTANT_NOTIFICATION = "importantNotificationPref";
    String EMERGENCY_NOTIFICATION = "emergencyNotificationPref";
    String DEVELOPERS = "developersOptions";

    protected SettingsPref(SharedPreferences pref, Context context) {
        this.pref = pref;
        this.context = context;
    }

    public boolean isDefaultRouteUsed() {
        return pref.getBoolean(SUMMARY_DEFAULT_ROUTE, false);
    }

    public void setDefaultRouteUsed(boolean value) {
        pref.edit().putBoolean(SUMMARY_DEFAULT_ROUTE, value).apply();
    }

    public boolean isHurryUpUsed() {
        return pref.getBoolean(SUMMARY_HURRY_UP, false);
    }

    public void setHurryUpUsed(boolean value) {
        pref.edit().putBoolean(SUMMARY_HURRY_UP, value).apply();
    }

    public boolean isEventNotificationON() {
        return pref.getBoolean(EVENT_NOTIFICATION, true);
    }

    public void setEventNotifications(boolean value) {
        pref.edit().putBoolean(EVENT_NOTIFICATION, value).apply();
    }

    public boolean isImportantNotificationON() {
        return pref.getBoolean(IMPORTANT_NOTIFICATION, true);
    }

    public void setImportantNotifications(boolean value) {
        pref.edit().putBoolean(IMPORTANT_NOTIFICATION, value).apply();
    }

    public boolean isEmergencyNotificationON() {
        return pref.getBoolean(EMERGENCY_NOTIFICATION, true);
    }

    public void setEmergencyNotifications(boolean value) {
        pref.edit().putBoolean(EMERGENCY_NOTIFICATION, value).apply();
    }

    public boolean isDevelopersOptionON() {
        return pref.getBoolean(DEVELOPERS, false);
    }

    public void setDevelopersOptions(boolean value) {
        pref.edit().putBoolean(DEVELOPERS, value).apply();
    }

    public String getNotificationPreferenceStatus() {
        if (!new Preferences(context).user().isNotificationAllowed()) {
            return "No";
        } else if (new Preferences(context).user().isNotificationAllowed()
                && isEventNotificationON()
                && isEmergencyNotificationON()
                && isImportantNotificationON()) {
            return "Yes";
        } else return "Partially Allowed";
    }
}
