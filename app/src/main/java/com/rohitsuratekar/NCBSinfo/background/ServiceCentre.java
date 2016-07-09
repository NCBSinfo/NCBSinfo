package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.activities.contacts.ContactList;
import com.rohitsuratekar.NCBSinfo.background.alarms.AlarmIDs;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.background.alarms.AlarmsHelper;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.List;

/**
 * This service will handle all background tasks and will act as relay hub
 * All local broadcasts will be sent from this service
 */
public class ServiceCentre extends IntentService implements AlarmIDs, AlarmConstants {

    public static final String INTENT = ServiceCentre.class.getName();
    public final String TAG = getClass().getSimpleName();

    public static final String RESET_APP_DATA = "resetAppData";

    public ServiceCentre() {
        super(ServiceCentre.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Service Centre trigger requested at " + new General().timeStamp());
        //Do not add default field here
        String trigger = intent.getStringExtra(INTENT);
        if (trigger != null) {
            switch (trigger) {
                case RESET_APP_DATA:
                    resetAppData();  //Warning : This will reset all data base, including personalized data
                    break;
            }
        }

    }

    /**
     * This will reset all app data to its defaults
     * Following things will be reset
     * (1) Transport Values
     * (2) Contact Data
     * (3) Alarms
     * Try to start new services at the end for fast performance
     */
    private void resetAppData() {

        //Reset Contact Data
        //TODO: Save favorite contacts on remote data and retrieve them on login
        new ContactsData(getBaseContext()).clearAll();

        String[][] clist2 = new ContactList().allContacts();
        for (String[] aClist2 : clist2) {
            new ContactsData(getBaseContext()).add(new ContactModel(1, aClist2[0], aClist2[1], aClist2[2], aClist2[3], "0"));
        }

        //Reset Alarms
      //  makeDefaultAlarms();

        //Reset Transport values
        Intent transport = new Intent(ServiceCentre.this, TransportHandler.class);
        transport.putExtra(TransportHandler.INTENT, TransportHandler.RESET);
        startService(transport);

    }

    /**
     * Removes all alarms including personalized alarms set buy users.
     * Use this method only for newly installed app.
     * If you want to cancel or modify single or daily alarm, use background.Alarms.class
     */
    private void makeDefaultAlarms() {

        Intent intent = new Intent(getBaseContext(), Alarms.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Cancel all previous alarms
        List<AlarmModel> listAllPast = new AlarmData(getBaseContext()).getAll();
        if (listAllPast.size() != 0) {
            for (AlarmModel alarmModel : listAllPast) {
                PendingIntent sender = PendingIntent.getBroadcast(getBaseContext(), alarmModel.getAlarmID(), intent, PendingIntent.FLAG_NO_CREATE);
                if (sender != null) {
                    alarmManager.cancel(sender);
                }
            }
        }

        //Removes all alarms from database
        new AlarmData(getBaseContext()).clearAll();

        //Create daily alarms to network operations based on alarmIDs and HourOfDay
        for (daily d : daily.values()) {
            AlarmModel temp = new AlarmModel();
            temp.setAlarmID(d.getAlarmID());
            temp.setType(alarmType.REPEAT.name());
            temp.setTrigger(triggers.DAILY_FETCH.name());
            temp.setLevel(alarmLevel.NETWORK.name());
            temp.setExtraParameter("null");
            temp.setExtraValue("null");
            temp.setAlarmTime(new AlarmsHelper().getTimeByHoursOfDay(d.getHourOftheDay()));
            temp.setAlarmDate(new AlarmsHelper().getTodaysDate());
            new AlarmData(getBaseContext()).add(temp);
        }

        Intent alarms = new Intent(ServiceCentre.this, Alarms.class);
        alarms.putExtra(Alarms.INTENT, triggers.RESET_ALL.name());
        sendBroadcast(alarms);

    }


}
