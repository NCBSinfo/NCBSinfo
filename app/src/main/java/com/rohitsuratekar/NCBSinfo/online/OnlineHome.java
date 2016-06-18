package com.rohitsuratekar.NCBSinfo.online;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;

public class OnlineHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_home);
        Intent intent = new Intent(this, Transport.class);
        intent.putExtra(Transport.INDENT,"0");
        startActivity(intent);
    }
}
