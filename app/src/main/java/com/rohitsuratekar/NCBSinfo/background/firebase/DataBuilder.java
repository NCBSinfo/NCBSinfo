package com.rohitsuratekar.NCBSinfo.background.firebase;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.secretbiology.helpers.general.General;

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
        data.setDefaultRoute(pref.user().getDefaultRouteValue());
        data.setCurrentApp(pref.app().getAppVesion());
        data.setFirebaseID(pref.user().getFirebaseID());
        data.setLastLogin(pref.app().getLastLogin());
        data.setRequestCalls(makeRequestCalls());
        data.setLastSync(General.timeStamp());

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
        String count1 = String.valueOf(pref.app().getOpenCount());
        String count2 = String.valueOf(pref.app().getNotificationOpened());
       //todo : String count3 = String.valueOf(pref.app().getNetworkRequests());
        return count1 + "_" + count2 + "_" + "paused";
    }
}
