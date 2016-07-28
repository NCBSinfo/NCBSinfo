package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class AfterBoot extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Services started after reboot");
        Intent service = new Intent(context, ServiceCentre.class);
        service.putExtra(ServiceCentre.INTENT, ServiceCentre.APP_REBOOTED);
        context.startService(service);
    }
}
