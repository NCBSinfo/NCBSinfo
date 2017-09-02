package com.rohitsuratekar.NCBSinfo.background.tasks;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.preferences.MigratePref;
import com.secretbiology.helpers.general.Log;

public class MigrateApp extends AsyncTask<Object, Void, Void> {

    private Context context;
    private OnTaskCompleted taskCompleted;

    public MigrateApp(OnTaskCompleted taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    @Override
    protected Void doInBackground(Object... params) {
        context = (Context) params[0];
        cancelOldAlarms();
        new MigratePref(context);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        taskCompleted.onTaskCompleted();
    }

    /**
     * Deletes all alarms set by this app till date.
     * Only upcoming alarms are not cancelled. They will be ignored by Alarms Service
     */
    private void cancelOldAlarms() {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int[] oldAlarms = new int[]{2000, 2003, 2004, 2005, 2006, 1000, 1001, 1989, 1990, 1991, 1992};
        Intent intent = new Intent(context, Alarms.class);
        for (int id : oldAlarms) {
            PendingIntent sender = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_NO_CREATE);
            if (sender != null) {
                alarmManager.cancel(sender);
            }
        }
        Log.inform("Cancelled all past alarms!");
    }
}
