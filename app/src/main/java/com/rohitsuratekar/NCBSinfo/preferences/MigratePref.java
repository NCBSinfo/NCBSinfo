package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.secretbiology.helpers.general.Log;

public class MigratePref extends Preferences {

    private String CURRENT_USER = "currentUsername";
    private String CURRENT_EMAIL = "currentEmail";
    private String DEFAULT_ROUTE = "defaultRoute";
    private String NOTIFICATIONS = "notification_preference";
    private String HURRY_UP_TIME = "hurryUp_Time";


    public MigratePref(Context context) {
        super(context);
        SharedPreferences pref = getPref();
        //Get all old preferences
        String name = pref.getString(CURRENT_USER, "Username");
        String email = pref.getString(CURRENT_EMAIL, "email@domain.com");
        int route = pref.getInt(DEFAULT_ROUTE, 0);
        boolean isNotificationAllowed = pref.getBoolean(NOTIFICATIONS, true);
        int hurry_up = pref.getInt(HURRY_UP_TIME, 5);


        AppPrefs appPrefs = new AppPrefs(context);

        if (!appPrefs.isAppMigrated()) {
            //Clear all old preferences
            clearAll();
            appPrefs.appMigrated();
        }

        //Put all old into new preferences
        appPrefs.setFavoriteRoute(route);
        appPrefs.setUserName(name);
        appPrefs.setUserEmail(email);
        appPrefs.updateVersion();
        appPrefs.notificationAllowed(isNotificationAllowed);
        appPrefs.setHurryUpTime(hurry_up);
        new Helper().legacyDefaultConverter(context, route);
        Log.inform("All shared preferences migrated");
    }
}
