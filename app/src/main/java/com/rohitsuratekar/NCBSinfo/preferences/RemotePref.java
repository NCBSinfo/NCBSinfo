package com.rohitsuratekar.NCBSinfo.preferences;

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


    SharedPreferences pref;

    protected RemotePref(SharedPreferences pref) {
        this.pref = pref;
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


}
