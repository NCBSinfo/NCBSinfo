package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;

/**
 * This will handle all transport related background wok
 */
public class TransportHandler extends IntentService {

    public static final String INTENT = TransportHandler.class.getName();
    public static final String RESET = "reset";
    public static final String CLEAR_PAST = "clearPast";

    private final String TAG = getClass().getSimpleName();
    SharedPreferences pref;

    public TransportHandler() {
        super(TransportHandler.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Transport service started ");

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String trigger = intent.getStringExtra(INTENT);
        if (trigger != null) {
            switch (trigger) {
                case RESET:
                    reset();
                    break;
                case CLEAR_PAST:
                    cleanPastPreferences();
                    break;
            }
        }

    }

    private void reset() {
        pref.edit().putString(Routes.NCBS_IISC.getWeekKey(), getString(R.string.def_ncbs_iisc_week)).apply();
        pref.edit().putString(Routes.NCBS_IISC.getSundayKey(), getString(R.string.def_ncbs_iisc_sunday)).apply();
        pref.edit().putString(Routes.IISC_NCBS.getWeekKey(), getString(R.string.def_iisc_ncbs_week)).apply();
        pref.edit().putString(Routes.IISC_NCBS.getSundayKey(), getString(R.string.def_iisc_ncbs_sunday)).apply();
        pref.edit().putString(Routes.NCBS_MANDARA.getWeekKey(), getString(R.string.def_ncbs_mandara_week)).apply();
        pref.edit().putString(Routes.NCBS_MANDARA.getSundayKey(), getString(R.string.def_ncbs_mandara_sunday)).apply();
        pref.edit().putString(Routes.MANDARA_NCBS.getWeekKey(), getString(R.string.def_mandara_ncbs_week)).apply();
        pref.edit().putString(Routes.MANDARA_NCBS.getSundayKey(), getString(R.string.def_mandara_ncbs_sunday)).apply();
        pref.edit().putString(Routes.BUGGY_FROM_NCBS.getWeekKey(), getString(R.string.def_buggy_from_ncbs)).apply();
        pref.edit().putString(Routes.BUGGY_FROM_MANDARA.getWeekKey(), getString(R.string.def_buggy_from_mandara)).apply();
        pref.edit().putString(Routes.NCBS_ICTS.getWeekKey(), getString(R.string.def_ncbs_icts_week)).apply();
        pref.edit().putString(Routes.NCBS_ICTS.getSundayKey(), getString(R.string.def_ncbs_icts_sunday)).apply();
        pref.edit().putString(Routes.ICTS_NCBS.getWeekKey(), getString(R.string.def_icts_ncbs_week)).apply();
        pref.edit().putString(Routes.ICTS_NCBS.getSundayKey(), getString(R.string.def_icts_ncbs_sunday)).apply();
        pref.edit().putString(Routes.NCBS_CBL.getWeekKey(), getString(R.string.def_ncbs_cbl)).apply();
    }


    /**
     * Clear past preferences and clean up space for others
     */
    private void cleanPastPreferences() {

        pref.edit().remove("route1_ncbs_iisc_week").apply();
        pref.edit().remove("route2_iisc_ncbs_week").apply();
        pref.edit().remove("route3_ncbs_mandara_week").apply();
        pref.edit().remove("route4_mandara_ncbs_week").apply();
        pref.edit().remove("route5_ncbs_icts_week").apply();
        pref.edit().remove("route6_icts_ncbs_week").apply();
        pref.edit().remove("route1_ncbs_iisc_sunday").apply();
        pref.edit().remove("route2_iisc_ncbs_sunday").apply();
        pref.edit().remove("route3_ncbs_mandara_sunday").apply();
        pref.edit().remove("route4_mandara_ncbs_sunday").apply();
        pref.edit().remove("route5_ncbs_icts_sunday").apply();
        pref.edit().remove("route6_icts_ncbs_sunday").apply();
        pref.edit().remove("route7_ncbs_cbl").apply();
        pref.edit().remove("buggy_from_ncbs").apply();
        pref.edit().remove("buggy_from_mandara").apply();
        pref.edit().remove("camp_buggy_ncbs").apply();
        pref.edit().remove("camp_buggy_mandara").apply();
        pref.edit().remove("camp_shuttle_ncbs").apply();
        pref.edit().remove("camp_shuttle_mandara").apply();

    }
}
