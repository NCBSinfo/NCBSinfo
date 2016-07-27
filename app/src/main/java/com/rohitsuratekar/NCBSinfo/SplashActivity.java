package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.activities.OfflineHome;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.background.ServiceCentre;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

public class SplashActivity extends AppCompatActivity {

    Preferences pref;
    BaseParameters baseParameters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        pref = new Preferences(getBaseContext());
        baseParameters = new BaseParameters(getBaseContext());

        //Initialize app if it is opened for first time and not upgraded from past
        if (pref.app().isAppOpenedFirstTime() && pref.app().isPreviouslyUsed()) {
            Intent service = new Intent(SplashActivity.this, ServiceCentre.class);
            service.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_APP_DATA);
            startService(service);
        }
        //If app contains data from past versions
        if (pref.app().isAppOpenedFirstTime() && !pref.app().isPreviouslyUsed()) {
            Intent service = new Intent(SplashActivity.this, ServiceCentre.class);
            service.putExtra(ServiceCentre.INTENT, ServiceCentre.SELECTIVE_UPGRADE);
            startService(service);
        }

        //TODO
        pref.app().setMode(AppConstants.modes.ONLINE);

        //Initialize app with latest app version
        try {
            pref.app().setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionCode);
            pref.app().setAppVersionName(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        pref.app().setLastLogin(General.timeStamp()); //Timestamp
        pref.app().addOpenCount(); //Whenever user opens app

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (pref.app().getMode()) {
                    case ONLINE:
                        Intent intent = new Intent(SplashActivity.this, OnlineHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                        break;
                    case OFFLINE:
                        Intent intent2 = new Intent(SplashActivity.this, OfflineHome.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent2);
                        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                        break;
                    default:
                        Intent intent3 = new Intent(SplashActivity.this, Home.class);
                        intent3.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent3);
                        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                }
            }
        }, 800);


    }
}
