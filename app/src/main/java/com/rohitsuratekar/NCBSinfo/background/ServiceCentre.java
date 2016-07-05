package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.activities.contacts.ContactList;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.utilities.General;

/**
 * This service will handle all background tasks and will act as relay hub
 * All local broadcasts will be sent from this service
 */
public class ServiceCentre extends IntentService {

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
        //TODO: uncomment
//        Intent alarms = new Intent(ServiceCentre.this, Alarms.class);
//        alarms.putExtra(Alarms.INTENT, Alarms.RESET_ALL);
//        sendBroadcast(alarms);

        //Reset Transport values
        Intent transport = new Intent(ServiceCentre.this, TransportHandler.class);
        transport.putExtra(TransportHandler.INTENT, TransportHandler.RESET);
        startService(transport);

    }
}
