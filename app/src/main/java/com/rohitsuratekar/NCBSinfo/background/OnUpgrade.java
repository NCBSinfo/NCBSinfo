package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.interfaces.AlarmConstants;

public class OnUpgrade extends BroadcastReceiver implements AlarmConstants {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.i(TAG, "Services started after app upgrade");
        //Start daily data fetching
        Intent i = new Intent(context, Alarms.class);
        i.putExtra(Alarms.INTENT, RESET_ALL);
        context.sendBroadcast(i);
    }
}