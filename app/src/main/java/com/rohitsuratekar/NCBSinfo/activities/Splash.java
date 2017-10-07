package com.rohitsuratekar.NCBSinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.background.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.GetAllRoutes;
import com.rohitsuratekar.NCBSinfo.background.OnFinish;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

public class Splash extends AppCompatActivity implements OnFinish {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        new GetAllRoutes(getBaseContext(), this).execute();
    }

    private void gotoHome() {
        Intent intent = new Intent(this, Transport.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void finished() {
        gotoHome();
    }

    @Override
    public void allRoutes(List<RouteData> routeDataList) {

        //Keep blank
        if (routeDataList.size() != 0) {
            gotoHome();
        } else {
            Log.i(getClass().getSimpleName(), "No routes found. Creating default ones.");
            new CreateDefaultRoutes(getBaseContext(), this).execute();
        }

    }
}