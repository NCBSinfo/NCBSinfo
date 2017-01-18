package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.activities.background.DefaultSettings;
import com.rohitsuratekar.NCBSinfo.activities.background.events.SplashLoadingEvent;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.SetUpActivity;
import com.secretbiology.helpers.general.Log;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class Splash extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SetUpActivity(this, R.layout.splash, "Splash", false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        if (preferences.getBoolean("testasdasd1", true)) {
            startService(new Intent(this, DefaultSettings.class).setAction(DefaultSettings.RESET_TRANSPORT));
            preferences.edit().putBoolean("testasdasd1", false).apply();
        } else {
            startActivity(new Intent(Splash.this, Home.class));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loadingDone(SplashLoadingEvent splashLoadingEvent) {
        Log.inform(splashLoadingEvent.checkEvent());
        startActivity(new Intent(Splash.this, Home.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}
