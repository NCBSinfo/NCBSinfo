package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.interfaces.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * All alarms should be set and received from this receiver
 * "Offline" mode should not fetch any data.
 */
public class Alarms extends BroadcastReceiver implements AlarmConstants, UserInformation {

    public static final String INTENT = "alarmIntent";

    private final String TAG = getClass().getSimpleName();

    Context context;
    SharedPreferences pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        String currentIntent = intent.getStringExtra(INTENT);
        if (currentIntent != null) {
            Log.i(TAG, "Received intent: " + currentIntent);
            switch (currentIntent) {
                case DAILY_FETCH:
                    startDataFetch(context);
                    break;
                case RESET_ALL:
                    resetAll();
                    break;
                case SEND_UPCOMINGS:
                    sendUpcomingAlarms();
                    break;
                case SEND_NOTIFICATION:
                    if (intent.getStringExtra(NOTIFICATION_CODE) != null) {
                        new NotificationService(context).sendNotification(Integer.parseInt(intent.getStringExtra(NOTIFICATION_CODE)));
                    }
                    break;
            }
        }
    }

    private void startDataFetch(Context context) {
        //Strict policy for data fetch. It will be fetched only when mode is not "offline".
        if (!PreferenceManager.getDefaultSharedPreferences(context).getString(MODE, OFFLINE).equals(OFFLINE)) {
            Intent service = new Intent(context, NetworkOperations.class);
            service.putExtra(NetworkOperations.INTENT, NetworkOperations.ALL_DATA);
            context.startService(service);
        }
    }

    public void resetAll() {

        //Cancel past alarms
        if (!pref.getBoolean(firstTime.CANCELED_PAST_ALARMS, false)) {
            cancelOld();
        }

        Intent intent = new Intent(context, Alarms.class);
        intent.putExtra(INTENT, DAILY_FETCH);
        AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                getCalender(dailyAlarms.earlyMorning.TIME),
                24 * 60 * 60 * 1000,
                getIndent(intent, dailyAlarms.earlyMorning.ID));

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                getCalender(dailyAlarms.morning.TIME),
                24 * 60 * 60 * 1000,
                getIndent(intent, dailyAlarms.morning.ID));

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                getCalender(dailyAlarms.afternoon.TIME),
                24 * 60 * 60 * 1000,
                getIndent(intent, dailyAlarms.afternoon.ID));

        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP,
                getCalender(dailyAlarms.evening.TIME),
                24 * 60 * 60 * 1000,
                getIndent(intent, dailyAlarms.evening.ID));

        Log.i(TAG, "All alarms are reset");
        //Start data fetch after reset alarms
        startDataFetch(context);

    }


    private PendingIntent getIndent(Intent intent, int indentID) {
        return PendingIntent.getBroadcast(context, indentID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    private long getCalender(int hourOfDay) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }

    private void sendUpcomingAlarms() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date targetDate = new Date(calendar.getTimeInMillis());

        List<TalkModel> list = new Utilities().getUpcomigTalks(context, targetDate);
        for (TalkModel talk : list) {
            Log.i(TAG, "sending event " + talk.getDataID() + " : " + talk.getNotificationTitle());
            Intent intent = new Intent(context, Alarms.class);
            intent.putExtra(INTENT, SEND_NOTIFICATION);
            intent.putExtra(NOTIFICATION_CODE, String.valueOf(talk.getDataID()));
            int requestID = new Utilities().getMilliseconds(talk.getTimestamp());
            Date tempDate = new Utilities().convertToTalkDate(talk.getDate(), talk.getTime());
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //Compatibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent, requestID));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent, requestID));
            } else {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent, requestID));
            }
        }
    }

    private long timeLeft(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int onsetTime = PreferenceManager.getDefaultSharedPreferences(context).getInt(preferences.NOTIFICANTION_ONSET, 10);
        long time = calendar.getTimeInMillis() - onsetTime * 60000; //Onset will be in min
        calendar.setTimeInMillis(time);
        if (calendar.before(Calendar.getInstance())) {
            calendar = Calendar.getInstance(); //If onset time is already past. Send notification immediately
        }
        return calendar.getTimeInMillis();
    }

    private void cancelOld() {
        Intent intent = new Intent(context, Alarms.class);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        PendingIntent sender1 = PendingIntent.getBroadcast(context, 2000, intent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent sender2 = PendingIntent.getBroadcast(context, 2003, intent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent sender3 = PendingIntent.getBroadcast(context, 2004, intent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent sender4 = PendingIntent.getBroadcast(context, 2005, intent, PendingIntent.FLAG_NO_CREATE);
        PendingIntent sender5 = PendingIntent.getBroadcast(context, 2006, intent, PendingIntent.FLAG_NO_CREATE);
        if (sender1 != null) {
            alarmManager.cancel(sender1);
        }
        if (sender2 != null) {
            alarmManager.cancel(sender2);
        }
        if (sender3 != null) {
            alarmManager.cancel(sender3);
        }
        if (sender4 != null) {
            alarmManager.cancel(sender4);
        }
        if (sender5 != null) {
            alarmManager.cancel(sender5);
        }
        pref.edit().putBoolean(firstTime.CANCELED_PAST_ALARMS, true).apply();
        Log.i(TAG, "Cancelled all past alarms!");
    }
}
