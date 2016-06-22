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

import com.rohitsuratekar.NCBSinfo.common.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.offline.OfflineHome;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
import com.rohitsuratekar.NCBSinfo.online.login.Registration;
import com.rohitsuratekar.NCBSinfo.online.temp.camp.CAMP;

public class Home extends AppCompatActivity implements UserInformation {

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
                overridePendingTransition(0, 0);
                break;

            case OFFLINE:
                startActivity(new Intent(Home.this, OfflineHome.class));
                overridePendingTransition(0, 0);
                break;

            case registration.camp16.CAMP_MODE:
                startActivity(new Intent(Home.this, CAMP.class));
                overridePendingTransition(0, 0);
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
                        Intent intent = new Intent(Home.this, Registration.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
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
                        pref.edit().putString(Home.MODE, Home.OFFLINE).apply();
                        alertDialog.dismiss();
                        startActivity(new Intent(Home.this, OfflineHome.class));
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
        if (pref.getBoolean(firstTime.APP_OPEN, true)) {
            setTransportValue();
            pref.edit().putBoolean(firstTime.APP_OPEN, false).apply();
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
        pref.edit().putString(netwrok.LAST_REFRESH_REMOTE_CONFIG, new Utilities().timeStamp()).apply();
    }
}
