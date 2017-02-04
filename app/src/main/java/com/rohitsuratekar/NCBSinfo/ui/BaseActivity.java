/*
 * Copyright (c) 2016. Rohit Suratekar
 */

package com.rohitsuratekar.NCBSinfo.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportEdit;
import com.secretbiology.helpers.general.General;

import java.util.HashMap;

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public Toolbar toolbar;
    private NavigationView navigationView;
    private CurrentActivity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //Base drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view); //Base navigation view
        navigationView.setNavigationItemSelectedListener(this);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Transition fade = new Fade();
            fade.excludeTarget(android.R.id.statusBarBackground, true);
            fade.excludeTarget(android.R.id.navigationBarBackground, true);
            getWindow().setExitTransition(fade);
            getWindow().setEnterTransition(fade);
        }

        this.currentActivity = setUpActivity(); //Call this before using Current Activity
        new SetUpActivity(this, currentActivity);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navigationView != null) {
            if (navigationView.getMenu().findItem(currentActivity.getNavigationMenu()) != null) {
                navigationView.getMenu().findItem(currentActivity.getNavigationMenu()).setChecked(true);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(currentActivity.getMenu(), menu);
        int[] idList = new int[]{R.id.action_settings, R.id.action_add_route, R.id.action_edit_route};
        for (int i : idList) {
            if (menu.findItem(i) != null) {
                menu.findItem(i).getIcon().setColorFilter(General.getColor(getBaseContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_edit_route) {
            startActivity(new Intent(this, TransportEdit.class));
            animateTransition();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        final Activity ongoingActivity = this;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        HashMap<Integer, CurrentActivity> activityMap = new HashMap<>();
        for (CurrentActivity c : CurrentActivity.values()) {
            activityMap.put(c.getNavigationMenu(), c);
        }
        if (item.getItemId() != currentActivity.getNavigationMenu()) {
            final CurrentActivity activity = activityMap.get(item.getItemId());
            if (activity != null) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(ongoingActivity, activity.getCurrentClass()));
                        animateTransition();
                    }
                }, 300);

            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    public void animateTransition() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    protected abstract CurrentActivity setUpActivity();


}