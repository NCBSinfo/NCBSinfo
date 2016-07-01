package com.rohitsuratekar.NCBSinfo.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.interfaces.User;

public class CurrentUser implements User{

    String name;
    String email;
    String type;
    String token;
    int defaultRoute;
    boolean isAuthorized;
    boolean allowNotifications;
    int notificationOnset;

    public CurrentUser(Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        this.name = pref.getString(profile.NAME, "User Name");
        this.email = pref.getString(profile.EMAIL, "email@domain.com");
        this.type = pref.getString(USER_TYPE, currentUserType.NEW_USER);
        this.token = pref.getString(profile.FIREBASE_TOKEN, "null");
        this.defaultRoute = pref.getInt(preferences.DEFAULT_ROUTE, 0);
        this.isAuthorized = pref.getBoolean(profile.REGISTERED, false);
        this.allowNotifications = pref.getBoolean(preferences.NOTIFICATIONS, true);
        this.notificationOnset = pref.getInt(preferences.NOTIFICANTION_ONSET, 10);

    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getType() {
        return type;
    }

    public String getToken() {
        return token;
    }

    public int getDefaultRoute() {
        return defaultRoute;
    }

    public boolean isAuthorized() {
        return isAuthorized;
    }

    public boolean isAllowNotifications() {
        return allowNotifications;
    }

    public int getNotificationOnset() {
        return notificationOnset;
    }
}
