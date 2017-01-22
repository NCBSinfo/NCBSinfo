package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.background.services.RouteSyncService;
import com.rohitsuratekar.NCBSinfo.background.tasks.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.LoadRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.MigrateApp;
import com.rohitsuratekar.NCBSinfo.background.tasks.OnTaskCompleted;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;

public class Splash extends Activity {

    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        final AppPrefs prefs = new AppPrefs(getBaseContext());

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

        RouteModel model = new RouteData(getBaseContext()).get(2);
        model.setNotes("This one is updated");
        new RouteData(getBaseContext()).update(model);
        Intent intent = new Intent(this, RouteSyncService.class);
        intent.setAction(RouteSyncService.SYNC_ALL);
       // startService(intent);
    }

    LoadRoutes loadRoutes = new LoadRoutes(new OnTaskCompleted() {
        @Override
        public void onTaskCompleted() {
            Intent intent = new Intent(Splash.this, Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    });

    /*@Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }*/


}
