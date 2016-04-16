package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;

import java.util.Calendar;

public class Alarms  extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        new LogEntry(context, StatusCodes.STATUS_ALARMCALL);

        String currentSwitch = intent.getExtras().getString(General.GEN_ALARM_INTENT,General.GEN_ALARM_DATAFETCH);
        switch (currentSwitch) {
            case General.GEN_ALARM_DATAFETCH:
                startDataFetch(context);
                break;
            case General.GEN_ALARM_DAILYRESET:
                resetDaily(context);
                break;
            case General.GEN_START_ALARMS:
                resetDataFetch(context);
                break;

            default:
                new LogEntry(context, StatusCodes.STATUS_ALARM_FAILED);
                Log.i("Wrong code", "in alarm");
                break;
        }

    }

    private void startDataFetch(Context context){
        Intent service = new Intent(context, DataFetch.class);
        service.putExtra(General.GEN_SERIVICE_SWITCH, Network.NET_START_FETCHING);
        context.startService(service);
    }

    private void resetDaily (Context context){
        Intent myIntent = new Intent(context , Notifications.class);
        myIntent.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);
        new LogEntry(context, StatusCodes.STATUS_DAILYNOTE_RESET);
    }

    private void resetDataFetch(Context context){
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
        String loogdetails = "Data fetching frequency is set to :"+min + " min from current time";
        new LogEntry(context, StatusCodes.STATUS_ALARMCHANGED,loogdetails);
    }


}
