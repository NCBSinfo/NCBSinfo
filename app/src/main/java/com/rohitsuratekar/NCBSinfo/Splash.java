package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.rohitsuratekar.NCBSinfo.activities.Home;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportEdit;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        startActivity(new Intent(this, Home.class));

    }
}
