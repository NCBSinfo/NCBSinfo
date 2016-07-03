package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.interfaces.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class ChangeTransport extends IntentService implements NetworkConstants {

    //Public Constants
    public static final String ROUTE_NO = "routeNo";
    public static final String CHANGE_VALUE = "changeValue";
    public static final String VALIDITY = "validity";

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private SharedPreferences pref;


    public ChangeTransport(String name) {
        super(name);
    }

    public ChangeTransport() {
        super(ChangeTransport.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        this.context = getBaseContext();
        this.pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String routeName = intent.getStringExtra(ROUTE_NO);
        String routeValue = intent.getStringExtra(CHANGE_VALUE);
        String validity = intent.getStringExtra(VALIDITY);

        if (routeName != null && routeValue != null && validity != null) {

            DateFormat currentformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
            try {
                Date dt = currentformat.parse(validity);

                if (dt.after(new Date())) {
                    if (pref.contains(routeName)) {
                        pref.edit().putString(routeName, routeValue).apply();
                    }
                } else {
                    Log.i(TAG, "Changes are outdated");
                }

            } catch (ParseException e) {
                e.printStackTrace();
                Log.e(TAG, "Date parsing");

                if (validity.equals(keys.transport.RESET)) {
                    setTransportValue();
                }
            }
        }

    }

    //Sets default transport timings. Later on this will be overwritten by remote config values
    public void setTransportValue() {

        pref.edit().putString(TransportConstants.NCBS_IISC_WEEK, getResources().getString(R.string.def_ncbs_iisc_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_IISC_SUNDAY, getResources().getString(R.string.def_ncbs_iisc_sunday)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_WEEK, getResources().getString(R.string.def_iisc_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_SUNDAY, getResources().getString(R.string.def_iisc_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_WEEK, getResources().getString(R.string.def_ncbs_mandara_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_SUNDAY, getResources().getString(R.string.def_ncbs_mandara_sunday)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_WEEK, getResources().getString(R.string.def_mandara_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_SUNDAY, getResources().getString(R.string.def_mandara_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_WEEK, getResources().getString(R.string.def_ncbs_icts_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_SUNDAY, getResources().getString(R.string.def_ncbs_icts_sunday)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_WEEK, getResources().getString(R.string.def_icts_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_SUNDAY, getResources().getString(R.string.def_icts_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_CBL, getResources().getString(R.string.def_ncbs_cbl)).apply();
        pref.edit().putString(TransportConstants.BUGGY_NCBS, getResources().getString(R.string.def_buggy_from_ncbs)).apply();
        pref.edit().putString(TransportConstants.BUGGY_MANDARA, getResources().getString(R.string.def_buggy_from_mandara)).apply();

        pref.edit().putString(TransportConstants.CAMP_BUGGY_NCBS, getResources().getString(R.string.def_camp_buggy_ncbs)).apply();
        pref.edit().putString(TransportConstants.CAMP_BUGGY_MANDARA, getResources().getString(R.string.def_camp_buggy_mandara)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_MANDARA, getResources().getString(R.string.def_camp_shuttle_mandara)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_NCBS, getResources().getString(R.string.def_camp_shuttle_ncbs)).apply();
        pref.edit().putString(UserInformation.netwrok.LAST_REFRESH_REMOTE_CONFIG, new Utilities().timeStamp()).apply();
    }
}
