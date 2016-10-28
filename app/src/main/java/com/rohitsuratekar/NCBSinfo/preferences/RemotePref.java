package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.Date;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 09-07-16.
 */
public class RemotePref {

    private final String TAG = this.getClass().getSimpleName();

    private String LAST_ACCESS = "remoteLastAccess";
    private String LAST_DATA_SYNC = "firebaseLastSync";
    private String LATEST_APP = "latestApp";
    private String MESSAGE = "remoteMessage";
    private String OLD_FIREBASE_DELETED = "remoteOldDeleted";
    private String REGISTRATION_DETAILS_SENT = "registrationSent";


    private SharedPreferences pref;
    private Context context;

    protected RemotePref(SharedPreferences pref, Context c) {
        this.pref = pref;
        this.context = c;
    }

    public boolean isLatestApp() {
        int version = new Preferences(context).app().getAppVesion();
        return version >= pref.getInt(LATEST_APP, version);
    }

    public void setLatestApp(int version) {
        pref.edit().putInt(LATEST_APP, version).apply();
    }

    public Date getLastUpdated() {
        return new DateConverters().convertToDate(pref.getString(LAST_ACCESS, "16/10/1989 00:00:00"));
    }

    public void setLastFirebaseSync(String timestamp) {
        pref.edit().putString(LAST_DATA_SYNC, timestamp).apply();
    }

    public Date getLastFireBaseSync() {
        return new DateConverters().convertToDate(pref.getString(LAST_DATA_SYNC, "16/10/1989 00:00:00"));
    }


    public void setLastUpdated(String timestamp) {
        pref.edit().putString(LAST_ACCESS, timestamp).apply();
    }

    public String getMessage() {
        return pref.getString(MESSAGE, "N/A");
    }

    public void setMessage(String message) {
        pref.edit().putString(MESSAGE, message).apply();
    }

    public boolean isOldDeleted() {
        return pref.getBoolean(OLD_FIREBASE_DELETED, false);
    }

    public void setOldDeleted() {
        pref.edit().putBoolean(OLD_FIREBASE_DELETED, true).apply();
    }

    public boolean isRegistrationDetailsSent() {
        return pref.getBoolean(REGISTRATION_DETAILS_SENT, false);
    }

    public void setRegistrationDetailsSent() {
        pref.edit().putBoolean(REGISTRATION_DETAILS_SENT, true).apply();
    }

}
