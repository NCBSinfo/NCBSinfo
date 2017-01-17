package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.activities.DefaultSettings;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.database.RouteManager;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        RouteManager manager = new RouteManager(getBaseContext());
        manager.clear();
        DefaultSettings.buildDefaultRoutes(getBaseContext());

        startActivity(new Intent(this, Home.class));
    }
}
