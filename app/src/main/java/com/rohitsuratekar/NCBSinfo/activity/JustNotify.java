package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;

public class JustNotify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.just_notify);

        Intent intent = getIntent();
        String datacode = intent.getStringExtra(General.GEN_EVENTDETAILS_DATACODE);
    }
}
