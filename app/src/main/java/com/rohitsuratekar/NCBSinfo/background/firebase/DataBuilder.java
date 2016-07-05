package com.rohitsuratekar.NCBSinfo.background.firebase;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.activities.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * Databuilder to send request to firebase database
 */
public class DataBuilder {

    Preferences pref;
    Context context;

    public DataBuilder(Context context) {
        this.context = context;
        this.pref = new Preferences(context);
    }

    public DataStructure make() {
        DataStructure data = new DataStructure();
        data.setName(pref.user().getName());
        data.setEmail(pref.user().getEmail());
        data.setToken(pref.user().getToken());
        data.setDefaultRoute(pref.user().getDefaultRoute());
        data.setCurrentApp(pref.app().getAppVesion());
        data.setFirebaseID(pref.user().getFirebaseID());
        data.setLastLogin(pref.app().getLastLogin());
        data.setRequestCalls(makeRequestCalls());

        //Converting notifications allowed is essential because if there is no notification field, database will return false
        //And we can not differentiate whether it is preference or default value

        if (pref.user().isNotificationAllowed()) {
            data.setNotificationPreference(1);
        } else {
            data.setNotificationPreference(2);
        }

        return data;
    }

    private String makeRequestCalls() {
        String call = String.valueOf(pref.app().getOpenCount());
        return call;
    }

    public void save(DataStructure data) {
        if (data.getName() != null) {
            pref.user().setName(data.getName());
        }
        if (data.getEmail() != null) {
            pref.user().setEmail(data.getEmail());
        }
        switch (data.isNotificationPreference()) {
            case 0:
                pref.user().notificationAllowed(true);
                break; //Default
            case 1:
                pref.user().notificationAllowed(true);
                break; //Preference
            case 2:
                pref.user().notificationAllowed(false);
                break; //Preference
        }
        pref.user().setDefaultRoute(new TransportHelper(context).getRoute(data.getDefaultRoute()));
    }
}
