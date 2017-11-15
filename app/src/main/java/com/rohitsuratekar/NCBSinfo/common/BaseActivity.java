package com.rohitsuratekar.NCBSinfo.common;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.BuildConfig;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.Dashboard;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;

/**
 * Created by Rohit Suratekar on 05-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public abstract class BaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private FrameLayout baseLayout;
    private NavigationView navigationView;
    public ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    public Toolbar toolbar; //Keep it public so that child activities can access

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.base_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        baseLayout = findViewById(R.id.base_view);
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(this);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        changeHeader();

    }

    /**
     * Override all setContentView constructors so that we can just extend and use it like regular
     * appcombat activity.
     */
    @Override
    public void setContentView(int layoutResID) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        if (baseLayout != null && inflater != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            View stubView = inflater.inflate(layoutResID, baseLayout, false);
            baseLayout.addView(stubView, params);
        }
    }

    @Override
    public void setContentView(View view) {
        if (baseLayout != null) {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            baseLayout.addView(view, params);
        }
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        if (baseLayout != null) {
            baseLayout.addView(view, params);
        }
    }

    /**
     * Change header color and gradient.
     * All header titles and clicks can be handled from here.
     */
    private void changeHeader() {
        View header = navigationView.getHeaderView(0);
        GradientDrawable backgroundGradient = (GradientDrawable) header.getBackground();
        backgroundGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        backgroundGradient.setGradientCenter(10, 0);
        backgroundGradient.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        drawerToggle.syncState();

        AppPrefs prefs = new AppPrefs(getApplicationContext());
        TextView headerTitle = header.findViewById(R.id.nav_header_title);
        TextView headerSubTitle = header.findViewById(R.id.nav_header_subtitle);
        if (headerTitle != null && headerSubTitle != null) {
            if (prefs.isUsedLoggedIn()) {
                headerTitle.setText(prefs.getUserName());
                headerSubTitle.setText(prefs.getUserEmail());
            } else {
                int versionCode = BuildConfig.VERSION_CODE;
                String versionName = BuildConfig.VERSION_NAME;
                headerSubTitle.setText(getString(R.string.header_subtitle, versionCode, versionName));
            }
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        final Activity activity = this;
        if (activity.getClass() != getNavClass(item.getItemId())) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(activity, getNavClass(item.getItemId())));
                    animateTransition();
                }
            }, 300);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MenuItem item = navigationView.getMenu().findItem(setNavigationMenu());
        if (item != null) {
            item.setChecked(true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            animateTransition();
        }
    }

    public void animateTransition() {
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * Useful for navigating between activities.
     *
     * @param navID : Navigation ID
     * @return : Class to change the activity
     */
    private Class getNavClass(int navID) {
        switch (navID) {
            case R.id.nav_home:
                return Home.class;
            case R.id.nav_transport:
                return Transport.class;
            case R.id.nav_contacts:
                return Contacts.class;
            case R.id.nav_settings:
                return Settings.class;
            case R.id.nav_dash:
                return Dashboard.class;
        }
        return Home.class;
    }

    /**
     * Force child activity to set navigation ID.
     * Default is 0, which will lead to no effect on navigation item click.
     *
     * @return : ID of navigation menu item
     */
    protected abstract int setNavigationMenu();
}
