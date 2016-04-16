package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;

import java.util.Calendar;

public class OnUpgrade extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar cal = Calendar.getInstance();
        int initialStart = PreferenceManager.getDefaultSharedPreferences(context).getInt(Preferences.PREF_ALARM_STARTING, context.getResources().getInteger(R.integer.alarm_start_default));
        int alarmFrequency = PreferenceManager.getDefaultSharedPreferences(context).getInt(Preferences.PREF_ALARM_FREQUENCY, context.getResources().getInteger(R.integer.alarm_frequency_default));
        cal.add(Calendar.SECOND, initialStart);
        Intent intent2 = new Intent(context, Alarms.class);
        intent2.putExtra(General.GEN_ALARM_INTENT, General.GEN_ALARM_DATAFETCH);
        PendingIntent sender = PendingIntent.getBroadcast(context, 1989, intent2, PendingIntent.FLAG_CANCEL_CURRENT); //1989 is ,y birth year :P
        // Get the AlarmManager service
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmFrequency * 1000, sender);
        double min = alarmFrequency/60;
        String loogdetails = "Data fetching is set to :"+min + " min from current time";
        new LogEntry(context, StatusCodes.STATUS_ONUPGRADE,loogdetails);
        //Start daily notification service
        Intent broadcast=new Intent(context,Alarms.class);
        broadcast.putExtra(General.GEN_ALARM_INTENT,General.GEN_ALARM_DAILYRESET);
        context.sendBroadcast(broadcast);
        Intent notservice=new Intent(context,Notifications.class);
        notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
        context.sendBroadcast(notservice);
    }
}