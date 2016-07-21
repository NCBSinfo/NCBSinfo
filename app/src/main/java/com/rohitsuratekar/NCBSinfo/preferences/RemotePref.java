package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 09-07-16.
 */
public class RemotePref {

    private final String TAG = this.getClass().getSimpleName();

    String LAST_ACCESS = "remoteLastAccess";
    String LATEST_APP = "latestApp";
    String MESSAGE = "remoteMessage";
    String OLD_FIREBASE_DELETED = "remoteOldDeleted";


    SharedPreferences pref;
    Context context;

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

    public Date getLastAccess() {
        Date date = new Date();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yyyy hh:mm", Locale.getDefault());
        try {
            date = dateFormat1.parse(pref.getString(LAST_ACCESS, "16/10/1989 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Error parsing date, trying next format");
            try {
                date = dateFormat2.parse(pref.getString(LAST_ACCESS, "16/10/1989 00:00"));
            } catch (ParseException e1) {
                e1.printStackTrace();
                Log.e(TAG, "Error parsing date, Invalid format");
            }
        }
        return date;
    }


    public void setLastAccess(String timestamp) {
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


}
