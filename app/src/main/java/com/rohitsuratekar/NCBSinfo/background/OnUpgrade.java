package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class OnUpgrade extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Services started after application upgraded");
        Preferences pref = new Preferences(context);

        //To avoid clashes with previous version, do not directly reset it
        //Specially from version 29 because Alarm Database is introduced after this update
        if (!pref.app().isAppOpenedFirstTime()) {
            Intent service = new Intent(context, ServiceCentre.class);
            service.putExtra(ServiceCentre.INTENT, ServiceCentre.APP_UPGRADED);
            //TODO context.startService(service);
        }
    }
}
