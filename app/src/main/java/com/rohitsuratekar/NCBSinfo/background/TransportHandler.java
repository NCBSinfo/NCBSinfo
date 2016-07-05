package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * This will handle all transport related background wok
 */
public class TransportHandler extends IntentService implements AppConstants {

    public static final String INTENT = TransportHandler.class.getName();
    public static final String RESET = "reset";
    public static final String CLEAR_PAST = "clearPast";

    private final String TAG = getClass().getSimpleName();
    Preferences pref;

    public TransportHandler() {
        super(TransportHandler.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Transport service started ");

        pref = new Preferences(getBaseContext());

        String trigger = intent.getStringExtra(INTENT);
        if (trigger != null) {
            switch (trigger) {
                case RESET:
                    reset();
                    break;
                case CLEAR_PAST:
                    pref.transport().cleanPastPreferences();
                    break;
            }
        }

    }

    private void reset() {

        for (Routes r : Routes.values()){
            pref.transport().resetRoute(r);
        }

    }


}
