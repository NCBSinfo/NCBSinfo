package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AfterBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //Start daily data fetching
        Intent i = new Intent(context, Alarms.class);
        i.putExtra(Alarms.INTENT, Alarms.RESET_ALL);
        context.startService(i);

        //Fetch data now
        Intent i2 = new Intent(context, Alarms.class);
        i2.putExtra(Alarms.INTENT, Alarms.DAILY_FETCH);
        context.startService(i2);
    }
}
