package com.rohitsuratekar.NCBSinfo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);



        Button btn = (Button)findViewById(R.id.debug_button2);
        if (btn != null) {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    long cacheExpiration = 3600*24; // 1 hour in seconds.
                    // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
                    // the server.
                    if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                        cacheExpiration = 0;
                    }

                    mFirebaseRemoteConfig.fetch(0)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("HERE", "Fetch Succeeded");
                                        // Once the config is successfully fetched it must be activated before newly fetched
                                        // values are returned.
                                        mFirebaseRemoteConfig.activateFetched();
                                        Log.i("Current Value",mFirebaseRemoteConfig.getString("transport_route2"));

                                    } else {
                                        Log.d("TAG", "Fetch failed");
                                    }
                                }
                            });
                }
            });
        }




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login.this, Home.class));
    }



}
