package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.activities.Home;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        startActivity(new Intent(this, Home.class));
    }
}
