package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.intro.Intro;
import com.rohitsuratekar.NCBSinfo.background.tasks.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoadRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.MigrateApp;
import com.rohitsuratekar.NCBSinfo.background.tasks.OnTaskCompleted;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;

public class Splash extends Activity {

    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        prefs = new AppPrefs(getBaseContext());

        prefs.updateVersion();
        // Create database if it is opened first time
        if (prefs.isFirstOpen()) {
            //Migrate App
            new MigrateApp(new OnTaskCompleted() {
                @Override
                public void onTaskCompleted() {
                    // Add default Routes
                    new CreateDefaultRoutes(new OnTaskCompleted() {
                        @Override
                        public void onTaskCompleted() {
                            prefs.appOpened();

                            //Now get session ready
                            loadRoutes.execute(getBaseContext());
                        }
                    }).execute(getBaseContext());
                }
            }).execute(getBaseContext());

        } else {
            loadRoutes.execute(getBaseContext());
        }

    }

    LoadRoutes loadRoutes = new LoadRoutes(new OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {
            if (prefs.isIntroSeen()) {
                Intent intent = new Intent(Splash.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            } else {
                Intent intent = new Intent(Splash.this, Intro.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }

        }
    });


}
