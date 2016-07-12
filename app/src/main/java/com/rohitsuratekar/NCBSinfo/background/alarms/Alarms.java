package com.rohitsuratekar.NCBSinfo.background.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.background.NetworkOperations;
import com.rohitsuratekar.NCBSinfo.background.NotificationService;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * All alarms should be sent with this boardcaster
 * Do not change package of this class as all previous alarms are with this intent.
 * This will also allow other users to send alarm request to app (if in future this app is success :P)
 * All intent should be "Alarms.java" with Triggers. Handle triggers from here.
 */

//TODO: Give notification if tomorrow is holiday

public class Alarms extends BroadcastReceiver implements AlarmConstants, AppConstants, AlarmIDs {

    public static final String INTENT = Alarms.class.getName();
    public static final String ALARM_KEY = "alarmKeys";
    private final String TAG = getClass().getSimpleName();

    Context context;
    Preferences pref;
    AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        pref = new Preferences(context);

        alarmTriggers triggers = new AlarmsHelper().getTrigger(intent.getStringExtra(INTENT));

        //Checking null is important because else it will create unnecessary null point exception for older alarms
        if (triggers != null) {
            Log.i(TAG, "Received intent: " + triggers.name());
            switch (triggers) {
                case DAILY_FETCH:
                    startDataFetch(context);
                    break;
                case RESET_ALL:
                    resetAll();
                    break;
                case SEND_UPCOMING:
                    sendUpcomingAlarms();
                    break;
                case SEND_NOTIFICATION:
                    if (intent.getStringExtra(NotificationService.NOTIFICATION_CODE) != null) {
                        new NotificationService(context).sendNotification(Integer.parseInt(intent.getStringExtra(NotificationService.NOTIFICATION_CODE)));
                    }
                    break;
                case DELETE_ALARM:
                    if (intent.getStringExtra(ALARM_KEY) != null) {
                        AlarmModel alarm = new AlarmData(context).get(Integer.valueOf(intent.getStringExtra(ALARM_KEY)));
                        cancelAlarm(alarm);
                    }
            }
        }
    }

    private void startDataFetch(Context context) {
        //Strict policy for data fetch. It will be fetched only when mode is not "offline".
        if (!pref.app().getMode().getValue().equals(modes.OFFLINE.getValue())) {
            Intent service = new Intent(context, NetworkOperations.class);
            service.putExtra(NetworkOperations.INTENT, NetworkOperations.ALL_DATA);
            context.startService(service);
        }

    }

    public void resetAll() {


        //Cancel past alarms
        if (!pref.app().arePastAlarmsCancelled()) {
            cancelOld();
        }

        List<AlarmModel> allAlarms = new AlarmData(context).getAll();
        for (AlarmModel alarm : allAlarms) {

            //First reset all network operations
            if (alarm.getLevel().equals(alarmLevel.NETWORK.name())) {
                Intent intent = new Intent(context, Alarms.class);
                intent.putExtra(INTENT, alarm.getTrigger());
                long time = new AlarmsHelper().getAlarmMiliseconds(alarm);
                PendingIntent pendingIntent = getIndent(intent, alarm.getAlarmID());

                //All repeating alarms
                if (alarm.getType().equals(alarmType.REPEAT.name())) {

                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, 24 * 60 * 60 * 1000, pendingIntent);

                } else if (alarm.getType().equals(alarmType.SINGLE_SHOT.name())) {

                    //Check compatibility
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                        alarmManager.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    } else {
                        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
                    }
                } //Single Shot
            }//Network

        }
        Log.i(TAG, "All alarms are reset");
    }


    private PendingIntent getIndent(Intent intent, int indentID) {
        return PendingIntent.getBroadcast(context, indentID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }


    private void sendUpcomingAlarms() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date targetDate = new Date(calendar.getTimeInMillis());

        List<TalkModel> list = new General().getUpcomigTalks(context, targetDate);
        for (TalkModel talk : list) {
            Log.i(TAG, "sending event " + talk.getDataID() + " : " + talk.getNotificationTitle());
            Intent intent = new Intent(context, Alarms.class);
            intent.putExtra(INTENT, alarmTriggers.SEND_NOTIFICATION);
            intent.putExtra(NotificationService.NOTIFICATION_CODE, String.valueOf(talk.getDataID()));
            int requestID = new General().getMilliseconds(talk.getTimestamp());
            Date tempDate = new DateConverters().convertToDate(talk.getDate() + " " + talk.getTime());
            //Compatibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent, requestID));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent, requestID));
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent, requestID));
            }
        }
    }

    /**
     * Gets time left for event time notification alarms
     *
     * @param date : Event time
     * @return : Time left for reminder
     */

    private long timeLeft(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int onsetTime = pref.user().getNotificationOnset();
        long time = calendar.getTimeInMillis() - onsetTime * 60000; //Onset will be in min
        calendar.setTimeInMillis(time);
        if (calendar.before(Calendar.getInstance())) {
            calendar = Calendar.getInstance(); //If onset time is already past. Send notification immediately
        }
        return calendar.getTimeInMillis();
    }


    /**
     * Cancels specific alarm and also deletes entry from database
     *
     * @param alarm : Alarm to be canceled
     */
    private void cancelAlarm(AlarmModel alarm) {
        Intent intent = new Intent(context, Alarms.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, alarm.getAlarmID(), intent, PendingIntent.FLAG_NO_CREATE);
        if (sender != null) {
            alarmManager.cancel(sender);
        }
        new AlarmData(context).delete(alarm);
        Log.i(TAG, "Alarm deleted : " + alarm.getAlarmTime());
    }

    /**
     * Cancel all old alarms which were set by versions before this.
     * Remove this method when sufficient number of users uninstalls app version before 21
     * Remember to use "FLAG_NO_CREATE" for pending intent
     */

    private void cancelOld() {
        Intent intent = new Intent(context, Alarms.class);
        for (old id : old.values()) {
            PendingIntent sender = PendingIntent.getBroadcast(context, id.getIdNumber(), intent, PendingIntent.FLAG_NO_CREATE);
            if (sender != null) {
                alarmManager.cancel(sender);
            }
        }
        pref.app().setPastAlarmsCancelled();
        Log.i(TAG, "Cancelled all past alarms!");
    }

}

