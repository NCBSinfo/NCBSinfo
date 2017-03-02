package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.secretbiology.helpers.general.Log;

public class MigratePref extends Preferences {

    public MigratePref(Context context) {
        super(context);
        SharedPreferences pref = getPref();
        //Get all old preferences
        String name = pref.getString("currentUsername", "Username");
        String email = pref.getString("currentEmail", "email@domain.com");
        int route = pref.getInt("defaultRoute", 0);
        boolean isNotificationAllowed = pref.getBoolean("notification_preference", true);
        int hurry_up = pref.getInt("hurryUp_Time", 5);


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
