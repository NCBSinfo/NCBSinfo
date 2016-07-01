package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rohitsuratekar.NCBSinfo.activities.OfflineHome;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.background.TransportHandler;
import com.rohitsuratekar.NCBSinfo.interfaces.User;

public class Home extends AppCompatActivity implements User {

    public final String TAG = getClass().getSimpleName();

    Button online, offline;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //Initialization
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar);

        online = (Button) findViewById(R.id.button_home_online);
        offline = (Button) findViewById(R.id.button_home_offline);

        Intent intent = new Intent(this, TransportHandler.class);
        intent.putExtra(TransportHandler.INTENT, TransportHandler.RESET);
        startService(intent);


        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putString(MODE, ONLINE).apply();
                startActivity(new Intent(Home.this, OnlineHome.class));
            }
        });

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pref.edit().putString(MODE, OFFLINE).apply();
                startActivity(new Intent(Home.this, OfflineHome.class));
            }
        });

    }


}
