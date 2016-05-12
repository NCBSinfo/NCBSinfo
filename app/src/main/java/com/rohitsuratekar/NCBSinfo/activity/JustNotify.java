package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;

public class JustNotify extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.just_notify);

        Intent intent = getIntent();
        String title = intent.getStringExtra(General.GEN_NOTIFY_TITLE);
        String message = intent.getStringExtra(General.GEN_NOTIFY_MESSAGE);

        TextView titleView = (TextView)findViewById(R.id.just_title);
        TextView messageView = (TextView)findViewById(R.id.just_message);

        if (titleView != null) {
            titleView.setText(title);
        }
        if (messageView != null) {
            messageView.setText(message);
        }

    }
}
