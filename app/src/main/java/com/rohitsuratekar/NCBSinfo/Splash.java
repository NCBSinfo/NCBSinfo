package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rohitsuratekar.NCBSinfo.activities.EditTransport;

public class Splash extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        startActivity(new Intent(this, EditTransport.class));

    }


}
