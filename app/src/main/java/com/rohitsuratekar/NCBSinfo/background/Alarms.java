package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.SettingsRelated;
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
        PendingIntent pendingIntent = PendingIntent.getService(context, General.GEN_DAILYNOT_INTENT1, myIntent, 0);
        PendingIntent pendingIntent2 = PendingIntent.getService(context, General.GEN_DAILYNOT_INTENT2, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 7);  //Every day at 7 am, daily notifications will be sent
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);

        calendar.set(Calendar.HOUR_OF_DAY, 13);  //Every day at 1 pm, daily notifications will be sent
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent2);

        new LogEntry(context, StatusCodes.STATUS_DAILYNOTE_RESET);
    }
    private void resetDataFetch(Context context){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        Intent intent1 = new Intent(context, Alarms.class);
        intent1.putExtra(General.GEN_ALARM_INTENT, General.GEN_ALARM_DATAFETCH);
        PendingIntent single_sender = PendingIntent.getBroadcast(context, 1989, intent1, PendingIntent.FLAG_CANCEL_CURRENT); //1989 is ,y birth year :P
        PendingIntent sender1 = PendingIntent.getBroadcast(context, 2000, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent sender2 = PendingIntent.getBroadcast(context, 2003, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent sender3 = PendingIntent.getBroadcast(context, 2004, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent sender4 = PendingIntent.getBroadcast(context, 2005, intent1, PendingIntent.FLAG_CANCEL_CURRENT);
        PendingIntent sender5 = PendingIntent.getBroadcast(context, 2006, intent1, PendingIntent.FLAG_CANCEL_CURRENT);

        if(!PreferenceManager.getDefaultSharedPreferences(context).getBoolean(SettingsRelated.SETTINGS_OPTIMIZED_DATA_SYNC,true)) {
            int initialStart = PreferenceManager.getDefaultSharedPreferences(context).getInt(Preferences.PREF_ALARM_STARTING, context.getResources().getInteger(R.integer.alarm_start_default));
            int alarmFrequency = PreferenceManager.getDefaultSharedPreferences(context).getInt(Preferences.PREF_ALARM_FREQUENCY, context.getResources().getInteger(R.integer.alarm_frequency_default));
            cal.add(Calendar.SECOND, initialStart);
            // Get the AlarmManager service
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), alarmFrequency * 1000, single_sender);
            double min = alarmFrequency / 60;
            String loogdetails = "Data fetching frequency is set to :" + min + " min from current time";
            new LogEntry(context, StatusCodes.STATUS_ALARMCHANGED, loogdetails);

            //Cancel previous alarms
            alarmMgr.cancel(sender1);
            alarmMgr.cancel(sender2);
            alarmMgr.cancel(sender3);
            alarmMgr.cancel(sender4);
            alarmMgr.cancel(sender5);

        }
        {
            //Optimized data fetch frequency
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            calendar.set(Calendar.HOUR_OF_DAY, 6);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , sender1);
            calendar.set(Calendar.HOUR_OF_DAY, 10);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , sender2);
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , sender3);
            calendar.set(Calendar.HOUR_OF_DAY, 15);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , sender4);
            calendar.set(Calendar.HOUR_OF_DAY, 21);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , sender5);
            //Cancel previous alarms
            alarmMgr.cancel(single_sender);
            new LogEntry(context,StatusCodes.STATUS_OPTIMIZED_ALARMS);
        }
    }
}
