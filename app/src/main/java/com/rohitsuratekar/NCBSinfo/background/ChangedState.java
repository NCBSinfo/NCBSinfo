package com.rohitsuratekar.NCBSinfo.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;

/**
 * This will do network call ONLY if user has not registered with our database before.
 * No call of device is in "Offline mode".
 */
public class ChangedState extends BroadcastReceiver implements UserInformation {
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        //If it is not offline mode
        if (!pref.getString(MODE, OFFLINE).equals(OFFLINE)) {
            //If device is online
            if (new Utilities().isOnline(context)) {
                //If device has not registered with server
                if (!pref.getString(USER_TYPE, currentUser.REGULAR_USER).equals(currentUser.REGULAR_USER)) {
                    Intent i = new Intent(context, DataManagement.class);
                    i.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATE);
                    context.startService(i);
                }

            }
        }

    }
}
