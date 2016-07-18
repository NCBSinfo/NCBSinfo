package com.rohitsuratekar.NCBSinfo.background.remote;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

import java.util.List;

/**
 * Manages all remote config values and their actions.
 * This class can be called from NetworkOperations.java. No need to save this information.
 * Utilize triggers and destroy
 */
public class RemoteManager implements RemoteData {

    List<RemoteModel> allList;
    Context context;
    Preferences pref;

    public RemoteManager(List<RemoteModel> allList, Context context) {
        this.allList = allList;
        this.context = context;
        this.pref = new Preferences(context);
    }

    public void takeAction() {

        for (RemoteModel r : allList) {
            switch (r.getKey()) {
                case CURRENT_APP:
                    pref.app().setLatestApp(Integer.valueOf(r.getValue())); //Saves latest app version
                    break;

            }
        }

    }

}
