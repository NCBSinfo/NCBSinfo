package com.rohitsuratekar.NCBSinfo.background.alarms;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.background.services.RouteSyncService;
import com.rohitsuratekar.NCBSinfo.background.services.UserPreferenceService;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.Log;

public class Alarms extends BroadcastReceiver implements AlarmIDs {

    public static final String START_ALARMS = "startAlarms";
    public static final String USER_SYNC_ALARM = "userSyncAlarm";
    public static final String ROUTE_SYNC_ALARM = "routeSyncAlarm";

    private Context context;
    private AlarmManager alarmManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        AppPrefs prefs = new AppPrefs(context);
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        String action = intent.getAction();
        if (action != null) {
            if (prefs.isUserLoggedIn()) {
                switch (action) {
                    case START_ALARMS:
                        startAlarms();
                        break;
                    case USER_SYNC_ALARM:
                        syncUsers();
                        break;
                    case ROUTE_SYNC_ALARM:
                        syncRoutes();
                        break;
                }
            } else {
                Log.inform("Alarms cancelled because user is not logged in");
            }

        } else {
            Log.inform("Unknown alarm action");
        }


    }

    private void syncUsers() {
        Log.inform("UserSync Alarm started");
        Intent intent = new Intent(context, UserPreferenceService.class);
        intent.setAction(UserPreferenceService.SYNC_USER_PREFERENCES);
        context.startService(intent);

    }

    private void syncRoutes() {
        Log.inform("RouteSync Alarm started");
        Intent intent = new Intent(context, RouteSyncService.class);
        intent.setAction(RouteSyncService.SYNC_ALL);
        context.startService(intent);
    }

    private void startAlarms() {
        setRouteSyncAlarm();
        setUserSyncAlarm();
    }

    private void setUserSyncAlarm() {

        Intent intent = new Intent(context, Alarms.class);
        intent.setAction(USER_SYNC_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, PROFILE_SYNC, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                , AlarmManager.INTERVAL_DAY, pendingIntent);
        Log.inform("User sync alarm is set");
    }

    private void setRouteSyncAlarm() {
        Intent intent = new Intent(context, Alarms.class);
        intent.setAction(ROUTE_SYNC_ALARM);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ROUTE_SYNC, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                , AlarmManager.INTERVAL_DAY * 3, pendingIntent);
        Log.inform("Route sync alarm is set");
    }
}
