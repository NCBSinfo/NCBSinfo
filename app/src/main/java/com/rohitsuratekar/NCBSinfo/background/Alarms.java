package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
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

public class Alarms extends BroadcastReceiver implements AlarmConstants, UserInformation {

    public static final String INTENT = "alarmIntent";

    private final String TAG = getClass().getSimpleName();

    Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        String currentIntent = intent.getStringExtra(INTENT);

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
                if(intent.getStringExtra(NOTIFICATION_CODE)!=null) {
                    new NotificationService(context).sendNotification(Integer.parseInt(intent.getStringExtra(NOTIFICATION_CODE)));
                }
        }
    }

    private void startDataFetch(Context context) {
        Intent service = new Intent(context, NetworkOperations.class);
        service.putExtra(NetworkOperations.INTENT, NetworkOperations.ALL_DATA);
        context.startService(service);
    }

    public void resetAll() {
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

    private void sendUpcomingAlarms(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,1);
        Date targetDate = new Date(calendar.getTimeInMillis());
        Intent intent = new Intent(context, Alarms.class);
        intent.putExtra(INTENT, SEND_NOTIFICATION);

        List<TalkModel> list = new Utilities().getUpcomigTalks(context,targetDate);
        for (TalkModel talk : list){
            intent.putExtra(NOTIFICATION_CODE,talk.getDataID());
            int requestID = new Utilities().getMilliseconds(talk.getTimestamp());
            Date tempDate = new Utilities().convertToDate(talk.getDate(),talk.getTime());
            AlarmManager alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //Compatibility
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                alarmMgr.setExact(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent,requestID));
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmMgr.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,timeLeft(tempDate), getIndent(intent,requestID));
            }
            else
            {
                alarmMgr.set(AlarmManager.RTC_WAKEUP, timeLeft(tempDate), getIndent(intent,requestID));
            }
        }
    }

    private long timeLeft(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int onsetTime = PreferenceManager.getDefaultSharedPreferences(context).getInt(preferences.NOTIFICANTION_ONSET,10);
        long time = calendar.getTimeInMillis() - onsetTime*60000; //Onset will be in min
        calendar.setTimeInMillis(time);
        if(calendar.before(Calendar.getInstance())){
            calendar = Calendar.getInstance(); //If onset time is already past. Send notification immediately
        }
        return calendar.getTimeInMillis();
    }
}
