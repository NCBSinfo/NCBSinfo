package com.rohitsuratekar.NCBSinfo.background.alarms;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Rohit Suratekar on 24-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class Alarms extends BroadcastReceiver {

    private AlarmManager alarmManager;


    @Override
    public void onReceive(Context context, Intent intent) {
        //This method exists just the sake of deleting old alarms
    }
}