package com.rohitsuratekar.NCBSinfo.background.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.experimental.holidays.Holidays;
import com.rohitsuratekar.NCBSinfo.background.DataManagement;
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
 * All alarms should be sent with this broadcaster
 * Do not change package of this class as all previous alarms are with this intent.
 * This will also allow other users to send alarm request to app (if in future this app is success :P)
 * All intent should be "Alarms.java" with Triggers. Handle triggers from here.
 */

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

                //Set Alarm
                case SET_ALARM:
                    if (intent.getStringExtra(ALARM_KEY) != null) {
                        AlarmModel alarm = new AlarmData(context).get(Integer.valueOf(intent.getStringExtra(ALARM_KEY)));
                        setAlarm(alarm);
                    }
                    break;

                //Reset All Alarms
                case RESET_ALL:
                    resetAll();
                    break;

                //Delete Alarm
                case DELETE_ALARM:
                    if (intent.getStringExtra(ALARM_KEY) != null) {
                        AlarmModel alarm = new AlarmData(context).get(Integer.valueOf(intent.getStringExtra(ALARM_KEY)));
                        cancelAlarm(alarm);
                    }
                    break;

                //Delete All Transport Reminder
                case DELETE_REMINDERS:
                    deleteReminders();
                    break;

                //Fetch Daily Events
                case DAILY_FETCH:
                    startDataFetch(context);
                    break;

                //Send Upcoming notifications
                case SEND_UPCOMING:
                    sendUpcomingAlarms();
                    break;

                case SEND_NOTIFICATION:
                    if (intent.getStringExtra(NotificationService.NOTIFICATION_CODE) != null) {
                        new NotificationService(context).sendNotification(Integer.parseInt(intent.getStringExtra(NotificationService.NOTIFICATION_CODE)));
                    }
                    break;

                case TRANSPORT_REMINDER:
                    if (intent.getStringExtra(ALARM_KEY) != null) {
                        AlarmModel alarmModel = new AlarmData(context).get(Integer.parseInt(intent.getStringExtra(ALARM_KEY)));
                        if (alarmModel != null) {
                            new NotificationService(context).sendNotification(alarmModel);
                        }
                    }
                    break;

                case LOW_PRIORITY_ALARMS:
                    startRemoteDataFetch(context);
                    break;

                case PREPARE_STATS:
                    prepareStats();
                    break;

                case SEND_STATS:
                    sendStats();
                    break;

            }
        }
    }

    private void setAlarm(AlarmModel alarm) {

        Intent intent = new Intent(context, Alarms.class);
        intent.putExtra(INTENT, alarm.getTrigger());
        intent.putExtra(ALARM_KEY, String.valueOf(alarm.getId()));
        long time = new AlarmsHelper().getAlarmMiliseconds(alarm);
        PendingIntent pendingIntent = getCustomIntent(intent, alarm.getAlarmID());

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
    }

    private void setInaccurateAlarm(AlarmModel alarm) {
        Intent intent = new Intent(context, Alarms.class);
        intent.putExtra(INTENT, alarm.getTrigger());
        intent.putExtra(ALARM_KEY, String.valueOf(alarm.getId()));
        long StarTime = new DateConverters().convertToCalendar(alarm.getAlarmDate() + " " + alarm.getAlarmTime()).getTimeInMillis();
        PendingIntent pendingIntent = getCustomIntent(intent, alarm.getAlarmID());
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, StarTime, 24 * 60 * 60 * 1000, pendingIntent);
        Log.i(TAG, "Low priority alarm set :" + alarm.getAlarmID());
    }

    private void startDataFetch(Context context) {
        //Strict policy for data fetch. It will be fetched only when mode is not "offline".
        if (!pref.app().getMode().getValue().equals(modes.OFFLINE.getValue())) {
            Intent service = new Intent(context, NetworkOperations.class);
            service.putExtra(NetworkOperations.INTENT, NetworkOperations.RESEARCH_TALKS);
            context.startService(service);
        } else {
            Log.e(TAG, "Network operation cancelled because of Offline mode");
        }

    }

    private void startRemoteDataFetch(Context context) {
        //Strict policy for data fetch. It will be fetched only when mode is not "offline".
        if (!pref.app().getMode().getValue().equals(modes.OFFLINE.getValue())) {
            Intent service = new Intent(context, NetworkOperations.class);
            service.putExtra(NetworkOperations.INTENT, NetworkOperations.REMOTE_DATA);
            context.startService(service);
            Intent dataService = new Intent(context, DataManagement.class);
            dataService.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATA);
            context.startService(dataService);

            //Notify if tomorrow is holiday
            if (new Holidays().isTomorrowHoliday()) {
                int holidayID = new Holidays().whichHoliday();
                if (!pref.app().isHolidayNotificationSent(holidayID)) {
                    new NotificationService(context)
                            .sendNotification("NCBS Holiday Tomorrow", context.getString(R.string.holiday_tomorrow), Holidays.class);
                    pref.app().holidayNotificationSent(holidayID);
                }
            }

        } else {
            Log.e(TAG, "Network operation cancelled because of Offline mode");
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
                //Check if it is inaccurate alarm or regular alarm
                if (alarm.getTrigger().equals(alarmTriggers.LOW_PRIORITY_ALARMS.name())) {
                    setInaccurateAlarm(alarm);
                } else {
                    setAlarm(alarm);
                }
            }//Network

            if (alarm.getLevel().equals(alarmLevel.TRANSPORT.name())) {
                setAlarm(alarm);
            }

        }
        sendUpcomingAlarms();
        Log.i(TAG, "All alarms are reset");
    }


    private PendingIntent getCustomIntent(Intent intent, int indentID) {
        return PendingIntent.getBroadcast(context, indentID, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }


    private void sendUpcomingAlarms() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        Date targetDate = new Date(calendar.getTimeInMillis());

        List<TalkModel> list = new General().getUpcomingTalks(context, targetDate);
        for (TalkModel talk : list) {
            Log.i(TAG, "sending event " + talk.getDataID() + " : " + talk.getNotificationTitle());
            Intent intent = new Intent(context, Alarms.class);
            intent.putExtra(INTENT, alarmTriggers.SEND_NOTIFICATION.name());
            intent.putExtra(NotificationService.NOTIFICATION_CODE, String.valueOf(talk.getDataID()));
            int requestID = (int) new DateConverters().convertToCalendar(talk.getTimestamp()).getTimeInMillis();
            Date tempDate = new DateConverters().convertToDate(talk.getDate() + " " + talk.getTime());
            //Compatibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getCustomIntent(intent, requestID));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getCustomIntent(intent, requestID));
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getCustomIntent(intent, requestID));
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
     * Deletes all reminders set for transports
     */
    private void deleteReminders() {
        List<AlarmModel> all = new AlarmData(context).getAll();
        for (AlarmModel alarm : all) {
            if (alarm.getLevel().equals(alarmLevel.TRANSPORT.name())) {
                cancelAlarm(alarm);
            }
        }
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

    //This will prepare to sync firebase data in next 10 min
    //Randomization is needed to avoid simultaneous connections in Firebase
    private void prepareStats() {
        Intent intent = new Intent(context, Alarms.class);
        intent.putExtra(INTENT, alarmTriggers.SEND_STATS.name());
        long StarTime = Calendar.getInstance().getTimeInMillis();
        PendingIntent pendingIntent = getCustomIntent(intent, lowPriorityAlarms.SEND_STAT.getAlarmID());
        alarmManager.setWindow(AlarmManager.RTC_WAKEUP, StarTime, 10 * 60 * 1000, pendingIntent);
        Log.i(TAG, "Alarm for stats is requested :" + lowPriorityAlarms.SEND_STAT.getAlarmID());
    }

    private void sendStats() {
        Intent service = new Intent(context, DataManagement.class);
        service.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATA);
        context.startService(service);
    }


}

