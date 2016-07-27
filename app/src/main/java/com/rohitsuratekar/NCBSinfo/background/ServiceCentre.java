package com.rohitsuratekar.NCBSinfo.background;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.activities.contacts.ContactList;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.background.alarms.AlarmIDs;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.background.alarms.AlarmsHelper;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.constants.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
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
    public static final String RESET_PREFERENCES = "resetPreferences";
    public static final String LOGIN_RESET = "resetUser";
    public static final String APP_UPGRADED = "upgradedApp";
    public static final String APP_REBOOTED = "rebootedApp";
    public static final String SELECTIVE_UPGRADE = "selectiveUpgrade";

    private Preferences pref;

    public ServiceCentre() {
        super(ServiceCentre.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.i(TAG, "Service Centre trigger requested at " + General.timeStamp());
        pref = new Preferences(getBaseContext());
        //Do not add default field here
        String trigger = intent.getStringExtra(INTENT);
        if (trigger != null) {
            switch (trigger) {
                case RESET_APP_DATA:
                    resetAppData();  //Warning : This will reset all data base, including personalized data
                    break;
                case RESET_PREFERENCES:
                    clearCustomization();
                    break;
                case LOGIN_RESET:
                    resetUser();
                    break;
                case APP_UPGRADED:
                    afterUpgrade();
                    break;
                case APP_REBOOTED:
                    afterBoot();
                    break;
                case SELECTIVE_UPGRADE:
                    selectiveUpgrade();
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

        //Clear all preferences
        pref.clearAll();

        //Initialize app with latest app version
        try {
            pref.app().setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            pref.app().setAppVersionName(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pref.app().setLastLogin(General.timeStamp()); //Timestamp
        pref.app().addOpenCount(); //Whenever user opens app

        //Reset Transport values
        Intent transport = new Intent(ServiceCentre.this, TransportHandler.class);
        transport.putExtra(TransportHandler.INTENT, TransportHandler.RESET);
        startService(transport);

        //Sign out from Firebase
        unsubsribeTopics();

        //Reset Contact Data
        resetContacts();

        //Reset Alarms
        resetAlarms();

        //App is already opened
        pref.app().setAppOpenedFirstTime();

    }

    /**
     * Use this when user login
     * This will just reset Transport , Contacts and Alarms and previous customizations
     */
    private void resetUser() {
        //Initialize app with latest app version
        try {
            pref.app().setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            pref.app().setAppVersionName(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pref.app().setLastLogin(General.timeStamp()); //Timestamp
        pref.app().addOpenCount(); //Whenever user opens app

        //Reset Transport values
        Intent transport = new Intent(ServiceCentre.this, TransportHandler.class);
        transport.putExtra(TransportHandler.INTENT, TransportHandler.RESET);
        startService(transport);
        //Reset Contact Data and Customization, reset alarms
        clearCustomization();
        //App is already opened
        pref.app().setAppOpenedFirstTime();

    }

    /**
     * After app is upgraded
     */
    private void afterUpgrade() {
        Intent alarms = new Intent(ServiceCentre.this, Alarms.class);
        alarms.putExtra(Alarms.INTENT, alarmTriggers.RESET_ALL.name());
        sendBroadcast(alarms);

        //Initialize app with latest app version
        try {
            pref.app().setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            pref.app().setAppVersionName(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Intent service = new Intent(ServiceCentre.this, DataManagement.class);
        service.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATA);
        startService(service);
    }

    /**
     * After app is rebooted
     */
    private void afterBoot() {
        Intent alarms = new Intent(ServiceCentre.this, Alarms.class);
        alarms.putExtra(Alarms.INTENT, alarmTriggers.RESET_ALL.name());
        sendBroadcast(alarms);
    }

    /**
     * This flag is triggered when app is just updated and there is still personal information about users
     * Here, save past information and then reset data
     */

    private void selectiveUpgrade() {

        //Clear old transport
        Intent clearOld = new Intent(ServiceCentre.this, TransportHandler.class);
        clearOld.putExtra(TransportHandler.INTENT, TransportHandler.CLEAR_PAST);
        startService(clearOld);

        //Reset Transport values
        Intent transport = new Intent(ServiceCentre.this, TransportHandler.class);
        transport.putExtra(TransportHandler.INTENT, TransportHandler.RESET);
        startService(transport);

        //Reset Contact Data
        resetContacts();

        //Reset Alarms
        resetAlarms();

        //App is already opened
        pref.app().setAppOpenedFirstTime();

    }

    private void clearCustomization() {

        //Set all default preferences
        pref.user().setDefaultRoute(Routes.NCBS_IISC);
        pref.user().setHurryUpTime(5);
        pref.user().setNotificationOnset(10);
        pref.user().notificationAllowed(true);
        pref.user().setNumberOfEventsToKeep(25);
        pref.settings().setDefaultRouteUsed(false);
        pref.settings().setHurryUpUsed(false);
        pref.settings().setDevelopersOptions(false);
        pref.settings().setImportantNotifications(true);
        pref.settings().setEventNotifications(true);
        pref.settings().setEmergencyNotifications(true);

        resetContacts();
        //Reset Alarms
        resetAlarms();


    }

    /**
     * Removes all alarms including personalized alarms set buy users.
     * Use this method only for newly installed app.
     * If you want to cancel or modify single or daily alarm, use background.Alarms.class
     */
    private void resetAlarms() {

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
            temp.setTrigger(alarmTriggers.DAILY_FETCH.name());
            temp.setLevel(alarmLevel.NETWORK.name());
            temp.setExtraParameter("null");
            temp.setExtraValue("null");
            temp.setAlarmTime(new AlarmsHelper().getTimeByHoursOfDay(d.getHourOftheDay())); //Important to convert to HH:mm
            temp.setAlarmDate(new AlarmsHelper().getTodaysDate());
            new AlarmData(getBaseContext()).add(temp);
        }

        //Create low priority alarms to remote fetch
        for (lowPriorityAlarms d : lowPriorityAlarms.values()) {
            AlarmModel temp = new AlarmModel();
            temp.setAlarmID(d.getAlarmID());
            temp.setType(alarmType.REPEAT.name());
            temp.setTrigger(alarmTriggers.LOW_PRIORITY_ALARMS.name());
            temp.setLevel(alarmLevel.NETWORK.name());
            temp.setExtraParameter("endTime");
            temp.setExtraValue(new AlarmsHelper().getTimeByHoursOfDay(d.getEndHour())); //Important to convert to HH:mm
            temp.setAlarmTime(new AlarmsHelper().getTimeByHoursOfDay(d.getStartHour())); //Important to convert to HH:mm
            temp.setAlarmDate(new AlarmsHelper().getTodaysDate());
            new AlarmData(getBaseContext()).add(temp);
        }

        Intent alarms = new Intent(ServiceCentre.this, Alarms.class);
        alarms.putExtra(Alarms.INTENT, alarmTriggers.RESET_ALL.name());
        sendBroadcast(alarms);
    }


    /**
     * Removes all contacts including contacts marked as favorite and creates new contacts with default data
     */
    private void resetContacts() {
        //Reset Contact Data
        //TODO: Save favorite contacts on remote data and retrieve them on login
        new ContactsData(getBaseContext()).clearAll();

        String[][] clist2 = new ContactList().allContacts();
        for (String[] aClist2 : clist2) {
            new ContactsData(getBaseContext()).add(new ContactModel(1, aClist2[0], aClist2[1], aClist2[2], aClist2[3], "0"));
        }
    }

    /**
     * Sign outs from Firebase Instances
     */
    private void unsubsribeTopics() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //Subscribe to general topics
            FirebaseMessaging.getInstance().unsubscribeFromTopic(NetworkConstants.fcmTopics.PUBLIC);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(NetworkConstants.fcmTopics.EMERGENCY);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(NetworkConstants.fcmTopics.DEBUG);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(NetworkConstants.fcmTopics.STUDENT);
            FirebaseMessaging.getInstance().unsubscribeFromTopic(NetworkConstants.fcmTopics.CAMP16);
            Log.i(TAG, "Unsubscribed with all topics");
            mAuth.signOut();
            Log.i(TAG, "User Signed Out");
        } else {
            Log.e(TAG, "No user found to sign out");
        }
    }


}
