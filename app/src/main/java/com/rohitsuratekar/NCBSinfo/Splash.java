package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.intro.Intro;
import com.rohitsuratekar.NCBSinfo.background.tasks.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoadRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.MigrateApp;
import com.rohitsuratekar.NCBSinfo.background.tasks.OnTaskCompleted;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.preferences.LearningPrefs;

import java.util.Calendar;

import static android.R.string.ok;

/**
 * This is slash activity to start background process and load all databases.
 * This activity also shifts old database and preferences to new one
 *
 * @see com.rohitsuratekar.NCBSinfo.database.Database for all Database methods
 * @see AppPrefs for all prefernecs methods
 */
public class Splash extends Activity {

    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        prefs = new AppPrefs(getBaseContext());
        new LearningPrefs(getBaseContext()).appOpened();

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

    /**
     * Loads all routes in background and goes to {@link Home} or {@link Intro}
     *
     * @see LoadRoutes
     */
    LoadRoutes loadRoutes = new LoadRoutes(new OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {
            if (prefs.isLTSShown()) {
                selectAction();
            } else {
                new AlertDialog.Builder(Splash.this)
                        .setTitle(getSalutation())
                        .setMessage(getString(R.string.lts_dialog))
                        .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                prefs.LTSshown();
                                dialog.dismiss();
                                selectAction();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
        }
    });

    private void selectAction() {
        if (prefs.isIntroSeen()) {
            Intent intent = new Intent(Splash.this, Home.class);
            //Not sure about addFlags() or setFlags()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        } else {
            Intent intent = new Intent(Splash.this, Intro.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @NonNull
    private String getSalutation() {
        int hourOfDay = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hourOfDay > 4 && hourOfDay <= 12) {
            return getString(R.string.salutation_morning);
        } else if (hourOfDay > 12 && hourOfDay <= 18) {
            return getString(R.string.salutation_afternoon);
        } else if (hourOfDay > 18 && hourOfDay <= 23) {
            return getString(R.string.salutation_night);
        } else {
            return getString(R.string.salutation_late_night);
        }
    }


}
