package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;

public class AfterBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Start daily notification service
        Intent broadcast=new Intent(context,Alarms.class);
        broadcast.putExtra(General.GEN_ALARM_INTENT,General.GEN_ALARM_DAILYRESET);
        context.sendBroadcast(broadcast);
        Intent alarmBroadcast = new Intent(context,Alarms.class);
        alarmBroadcast.putExtra(General.GEN_ALARM_INTENT,General.GEN_START_ALARMS);
        context.sendBroadcast(alarmBroadcast);
        Intent notservice=new Intent(context,Notifications.class);
        notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
        context.sendBroadcast(notservice);
        new LogEntry(context, StatusCodes.STATUS_AFTERBOOT);
    }
}
