package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.General;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class OnNetworkChanged extends BroadcastReceiver {

    private final String TAG = getClass().getSimpleName();
    Preferences pref;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Services started after network state changed");

        pref = new Preferences(context);
        if (new General().isNetworkAvailable(context)) {
            if (new General().isOnProxy()) {
                Log.i(TAG, "Skipping Firebase network calls because user is on proxy");
            } else {
                Log.i(TAG, "Network call started");
                if (!pref.app().getMode().equals(AppConstants.modes.OFFLINE)
                        && !pref.app().getMode().equals(AppConstants.modes.UNKNOWN)) {
                    switch (pref.user().getUserType()) {
                        case NEW_USER:
                            Intent i1 = new Intent(context, DataManagement.class);
                            i1.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATE);
                            context.startService(i1);
                            break;
                        case OLD_USER:
                            Intent i2 = new Intent(context, DataManagement.class);
                            i2.putExtra(DataManagement.INTENT, DataManagement.FETCH_FIREBASE_DATA);
                            context.startService(i2);
                            break;
                        case REGULAR_USER:
                            if (new TransportHelper().TimeLeftFromNow(pref.network().getLastFireBaseSync())[0] > 2) {
                                Intent i3 = new Intent(context, DataManagement.class);
                                i3.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATE);
                                context.startService(i3);
                            }
                            break;
                    }
                }
            }
        } else {
            Log.i(TAG, "Skipping network calls because of no network available");
        }
    }
}
