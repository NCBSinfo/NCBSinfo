package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.utilities.General;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class OnNetworkChanged extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Services started after network state changed");
        if (new General().isNetworkAvailable(context)) {
            if (new General().isOnProxy()) {
                Log.i(TAG, "Skipping Firebase network calls because user is on proxy");
            } else {
                Log.i(TAG, "Network call started");
            }
        } else {
            Log.i(TAG, "Skipping network calls because of no network available");
        }
    }
}
