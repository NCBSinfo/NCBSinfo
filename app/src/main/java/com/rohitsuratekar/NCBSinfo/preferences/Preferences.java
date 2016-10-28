package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class Preferences {

    private Context context;
    private SharedPreferences pref;


    public Preferences(Context context) {
        this.context = context;
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public User user() {
        return new User(pref, context);
    }

    public App app() {
        return new App(pref, context);
    }

    public TransportPref transport() {
        return new TransportPref(pref, context);
    }

    public RemotePref network() {
        return new RemotePref(pref, context);
    }


    public SettingsPref settings() {
        return new SettingsPref(pref, context);
    }

    public void clearAll() {
        pref.edit().clear().apply();
    }

}
