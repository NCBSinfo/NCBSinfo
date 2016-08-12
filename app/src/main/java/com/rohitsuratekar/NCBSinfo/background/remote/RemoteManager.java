package com.rohitsuratekar.NCBSinfo.background.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.secretbiology.helpers.general.General;

import java.util.List;

/**
 * Manages all remote config values and their actions.
 * This class can be called from NetworkOperations.java. No need to save this information.
 * Utilize triggers and destroy
 */
public class RemoteManager implements RemoteData {

    private final String TAG = getClass().getSimpleName();
    List<RemoteModel> allList;
    Context context;
    Preferences pref;

    public RemoteManager(List<RemoteModel> allList, Context context) {
        this.allList = allList;
        this.context = context;
        this.pref = new Preferences(context);
    }

    public void takeAction() {
        Log.i(TAG, "Remote Data Updated");
        for (RemoteModel r : allList) {

            //Set App Level
            if (r.getLevel().equals(levels.APP)) {
                switch (r.getKey()) {
                    case keys.CURRENT_APP:
                        pref.network().setLatestApp(Integer.valueOf(r.getValue())); //Saves latest app version
                        break;
                    case keys.TIMESTAMP:
                        pref.network().setLastUpdated(r.getValue());
                        break;
                    case keys.MESSAGE:
                        pref.network().setMessage(r.getKey());
                        break;
                    default:
                        Log.e(TAG, "Unknown App Level Key : " + r.getKey());
                }
            }
            if (r.getLevel().equals(levels.TRANSPORT)) {

                //Save Transport Value
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                if (sharedPreferences.contains(r.getKey().trim())) {
                    sharedPreferences.edit().putString(r.getKey().trim(), r.getValue()).apply();
                    Log.i(TAG, "Transport updated from network : " + r.getKey().trim());
                }
                pref.transport().setLastUpdate(General.timeStamp());

            }

        }

    }

}
