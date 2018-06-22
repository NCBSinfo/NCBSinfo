package com.rohitsuratekar.NCBSinfo.activities.developer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.rohitsuratekar.NCBSinfo.BuildConfig;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.fragments.home.RemoteConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DevelopersOptions extends AppCompatActivity implements RemoteConstants {

    @BindView(R.id.dev_recycler)
    RecyclerView recyclerView;

    private FirebaseRemoteConfig mf;
    private List<DevRecyclerModel> modelList;
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developers_options);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.dev_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setTitle(R.string.settings_developers);

        mf = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mf.setConfigSettings(configSettings);
        mf.setDefaults(R.xml.remote_config_defaults);
        setItems();
        recyclerView.setAdapter(new DevAdapter(modelList));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    private void setItems() {
        modelList = new ArrayList<>();
        String[] ss = new String[]{PROMO_START_TIME, PROMO_END_TIME, IS_HOME_PROMO_ACTIVE, HOME_GRAPHICS_URL};
        for (String s : ss) {
            if (!s.equals(HOME_GRAPHICS_URL)) {
                DevRecyclerModel m1 = new DevRecyclerModel(s, mf.getString(s));
                modelList.add(m1);
            } else {
                String ns = mf.getString(s).replace("https://firebasestorage.googleapis.com/v0/b/ncbs-info.appspot.com", "");
                String n = ns.split("token")[0];
                DevRecyclerModel m1 = new DevRecyclerModel(s, n);
                modelList.add(m1);
            }
        }
        if (prefs.getAdminCode().trim().length() > 0) {
            DevRecyclerModel m2 = new DevRecyclerModel("admin_code", prefs.getAdminCode());
            modelList.add(m2);
        }
        if (prefs.getAdminCode().equals(mf.getString(ADMIN_CODE))) {
            DevRecyclerModel m3 = new DevRecyclerModel("admin_mode", "true");
            modelList.add(m3);
        } else {
            DevRecyclerModel m3 = new DevRecyclerModel("admin_mode", "false");
            modelList.add(m3);
        }
    }

    @OnClick(R.id.dev_text)
    public void showPass() {


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DevelopersOptions.this);
        alertDialog.setTitle("Added Key");
        alertDialog.setMessage("Enter key to override all basic options.");

        final EditText input = new EditText(DevelopersOptions.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("ADD",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText().toString().trim().length() > 0) {
                            prefs.setAdminCode(input.getText().toString());
                            Intent i = new Intent(DevelopersOptions.this, DevelopersOptions.class);
                            finish();
                            startActivity(i);
                        }
                    }
                });
        alertDialog.setIcon(R.drawable.icon_developer);
        alertDialog.show();
    }

    @OnClick(R.id.dev_remove)
    public void removeDev() {
        prefs.removeDeveloper();
        finish();
    }
}
