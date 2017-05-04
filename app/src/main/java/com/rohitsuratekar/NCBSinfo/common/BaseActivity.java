package com.rohitsuratekar.NCBSinfo.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.nav_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.base_view)
    ViewStub viewStub;

    private CurrentActivity currentActivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout);
        currentActivity = setCurrentActivity();
        ButterKnife.bind(this);
        setUpDrawer();
        setUpActivity();
    }


    private void setUpDrawer() {
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        navigationView.inflateHeaderView(R.layout.base_header); //Header
        navigationView.inflateMenu(R.menu.drawer);
        View header = navigationView.getHeaderView(0);
        GradientDrawable backgroundGradient = (GradientDrawable) header.getBackground();
        backgroundGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        backgroundGradient.setGradientCenter(10, 0);
        backgroundGradient.setColor(General.getColor(getApplicationContext(), R.color.colorPrimary));
        navigationView.setNavigationItemSelectedListener(this);
        toggle.syncState();
    }

    private void setUpActivity() {
        setTitle(getString(currentActivity.getName()));
        viewStub.setLayoutResource(currentActivity.getLayout());
        viewStub.inflate();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        final Class navClass = Helper.getClass(item.getItemId());
        final Activity activity = this;
        if (navClass != null) {
            if (!navClass.equals(this.getClass())) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(activity, navClass));
                        animateTransition();
                    }
                }, 300);
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (navigationView != null) {
            MenuItem item = navigationView.getMenu().findItem(currentActivity.getNavigationID());
            if (item != null) {
                item.setChecked(true);
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            animateTransition();
        }
    }

    public void animateTransition() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    protected abstract CurrentActivity setCurrentActivity();
}
