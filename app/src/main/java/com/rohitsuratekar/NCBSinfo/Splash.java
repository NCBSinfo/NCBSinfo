package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.common.AppState;
import com.rohitsuratekar.NCBSinfo.debug.TestRoutes;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        AppState.initialize(new TestRoutes().getAllRoutes());
        startActivity(new Intent(this, Home.class));
    }
}
