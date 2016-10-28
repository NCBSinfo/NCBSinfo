package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.secretbiology.helpers.general.General;

import java.util.Calendar;

/**
 * This class deals with app related shared preferences
 * TODO: Temporary network query limit is removed. Change this in next update.
 */
public class App implements AppConstants {

    private SharedPreferences pref;
    private Context context;
    //int NETWORK_LIMIT = 100;
    private String APP_MODE = "app_mode";
    private String APP_VERSION = "latestAppVersion";
    private String APP_VERSION_NAME = "latestAppVersionName";
    private String APP_OPEN = "firstTimeAppOpen_v29"; //Changed from Version 29
    private String APP_OPEN_LAST_VERSIONS = "firstTimeAppOpen_01July"; //Used in previous version
    private String CANCELED_PAST_ALARMS = "cancelledPastAlarms";
    private String FIRST_NOTIFICATION_EVENTS = "sendFirstNotificationEvents"; //Only for newly registered users
    private String LAST_LOGIN = "lastLogin";
    private String OPEN_COUNT = "openCount";
    private String NOTIFICATION_OPENED = "notificationOpened";
    private String LAST_EVENT_SYNC = "lastEventSync";
    private String IS_HOLIDAY_SENT = "isHolidayNoteSent_";
    private String OFFLINE_NOTICE = "offline_notice";

    protected App(SharedPreferences pref, Context context) {
        this.pref = pref;
        this.context = context;
    }

    public modes getMode() {
        for (modes m : modes.values()) {
            if (m.getValue().equals(pref.getString(APP_MODE, modes.UNKNOWN.getValue()))) {
                return m;
            }
        }
        return modes.UNKNOWN; //Default
    }

    public void setMode(modes mode) {
        switch (mode) {
            case ONLINE:
                pref.edit().putString(APP_MODE, modes.ONLINE.getValue()).apply();
                break;
            case OFFLINE:
                pref.edit().putString(APP_MODE, modes.OFFLINE.getValue()).apply();
                break;
            case UNKNOWN:
                pref.edit().putString(APP_MODE, modes.UNKNOWN.getValue()).apply();
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

    public String getLastLogin() {
        return pref.getString(LAST_LOGIN, General.timeStamp());
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

    //This will return true only if last versions have not set it to false
    public boolean isPreviouslyUsed() {
        return pref.getBoolean(APP_OPEN_LAST_VERSIONS, true);
    }

    /**
     * Following method is need to avoid network abuse from any app
     *
     * @return : true if app is making network request within limit
     */
    public boolean isWithinNetworkLimit() {

        /*if (lastDate() == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
            int count = getNetworkRequests() + 1;
            pref.edit().putInt(networkCount, count).apply();
            if (count < NETWORK_LIMIT) {
                return true;
            } else {
                Log.e(getClass().getSimpleName(), "Network Limit over. No network calls should be allowed.");
                return false;
            }
        } else {
            pref.edit().putInt(networkDateStamp, Calendar.getInstance().get(Calendar.DAY_OF_YEAR)).apply();
            pref.edit().putInt(networkCount, 0).apply();
            return true;
        }*/

        return true;

    }

    public int getNetworkRequests() {
        return pref.getInt(networkCount, 0);
    }

    private String networkDateStamp = "networkDateStamp";
    private String networkCount = "netwrokCount";

    private int lastDate() {
        return pref.getInt(networkDateStamp, Calendar.getInstance().get(Calendar.DAY_OF_YEAR));
    }

    public void resetNetworkLimit() {
        pref.edit().putInt(networkCount, 0).apply();
    }

    public String getLastEventSync() {
        return pref.getString(LAST_EVENT_SYNC, "Never");
    }

    public void setLastEventSync(String timestamp) {
        pref.edit().putString(LAST_EVENT_SYNC, timestamp).apply();
    }

    //Following methods are needed because inexact alarms were triggering multiple times per day
    public boolean isHolidayNotificationSent(int id) {
        String temp1 = pref.getString(IS_HOLIDAY_SENT + String.valueOf(id), "false");
        if (temp1.equals("true") && id > 0) {
            if (pref.getString(IS_HOLIDAY_SENT + String.valueOf(id - 1), "false").equals("true")) {
                pref.edit().remove(IS_HOLIDAY_SENT + String.valueOf(id - 1)).apply();
            }
        }
        return temp1.equals("true");
    }

    public void holidayNotificationSent(int id) {
        pref.edit().putString(IS_HOLIDAY_SENT + String.valueOf(id), "true").apply();
    }

    public void offlineNoticeSeen() {
        pref.edit().putBoolean(OFFLINE_NOTICE, true).apply();
    }

    public boolean isOfflineNoticeSeen() {
        return pref.getBoolean(OFFLINE_NOTICE, false);
    }


}
