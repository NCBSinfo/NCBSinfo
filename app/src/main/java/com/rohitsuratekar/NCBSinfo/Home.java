package com.rohitsuratekar.NCBSinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.rohitsuratekar.NCBSinfo.offline.OfflineHome;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

public class Home extends AppCompatActivity {

    //Public Constants
    public static final String MODE = "app_mode";
    public static final String ONLINE = "online";
    public static final String OFFLINE = "offline";
    public static final String LOGIN = "login";

    //UI elements
    Button onlineButton, offlineButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        switch (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MODE,"none")){
            case ONLINE:
                startActivity(new Intent(Home.this, OnlineHome.class)); break;
            case OFFLINE:
                startActivity(new Intent(Home.this, OfflineHome.class)); break;
            case LOGIN:
                startActivity(new Intent(Home.this,Login.class)); break;
        }

        onlineButton = (Button)findViewById(R.id.button_home_online);
        offlineButton = (Button)findViewById(R.id.button_home_offline);

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_online));
                alertDialog.setMessage(getResources().getString(R.string.warning_online_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Home.this,Login.class));
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_offline));
                alertDialog.setMessage(getResources().getString(R.string.warning_offline_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Home.this,OfflineHome.class));
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

    }
}
