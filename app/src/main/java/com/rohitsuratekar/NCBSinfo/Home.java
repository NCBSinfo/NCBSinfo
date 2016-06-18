package com.rohitsuratekar.NCBSinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.offline.OfflineHome;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

public class Home extends AppCompatActivity {

    //Public Constants
    public static final String MODE = "app_mode";
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String FIRST_TIME = "firstTimeOpen";

    //UI elements
    Button onlineButton, offlineButton;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        switch (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MODE, "none")) {
            case ONLINE:
                startActivity(new Intent(Home.this, OnlineHome.class));
                break;
            case OFFLINE:
                startActivity(new Intent(Home.this, OfflineHome.class));
                break;
        }

        onlineButton = (Button) findViewById(R.id.button_home_online);
        offlineButton = (Button) findViewById(R.id.button_home_offline);

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_online));
                alertDialog.setMessage(getResources().getString(R.string.warning_online_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Home.this, Login.class));
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_offline));
                alertDialog.setMessage(getResources().getString(R.string.warning_offline_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Home.this, OfflineHome.class));
                        pref.edit().putString(Home.MODE, Home.OFFLINE).apply();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        //Set transport timings first time app is open
        if(pref.getBoolean(FIRST_TIME, true)) {
            setTransportValue();
            pref.edit().putBoolean(FIRST_TIME, false).apply();
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

    }
}
