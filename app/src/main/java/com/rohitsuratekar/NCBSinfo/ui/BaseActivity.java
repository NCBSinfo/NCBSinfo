package com.rohitsuratekar.NCBSinfo.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.interfaces.User;
import com.rohitsuratekar.NCBSinfo.utilities.CurrentMode;

/**
 * This is base UI for all activities. All activities should should implement this except special need (e.g. Settings Activity)
 * Each activity has identifier set in  'CurrentActivity' enum. This identifier will set UI and activity related variables.
 * This will ensure homogeneous UI in highly customizable fashion. This will also reduce unnecessary layouts and xml.
 */

public abstract class BaseActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, User {

    CurrentActivity currentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_layout); //Base layout

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); //Base toolbar
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout); //Base drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view); //Base navigation view
        navigationView.setNavigationItemSelectedListener(this);

        //Variables should come from child activity
        this.currentActivity = setCurrentActivity();

        //Set
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(currentActivity.getLayout());
        viewStub.inflate();
        setTitle(getString(currentActivity.getTitle()));

        //Set navigation drawer
        new CurrentNavigationDrawer(currentActivity, navigationView, getBaseContext()).set();

        //Hide unwanted items from layout
        setLayout();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(new CurrentMenu(currentActivity).set(), menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (item.getItemId() == R.id.nav_change_mode) {
            changeMode();
        } else {
            Intent intent = new NavigationIDs(item.getItemId(), this).getIntent();
            //Start activity only when current activity is not the same activity
            if (!intent.filterEquals(new Intent(this, this.getClass()))) {
                startActivity(intent);
            }
        }
        return true;
    }

    private void setLayout() {
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        if (!currentActivity.needTabLayout()) {
            tabs.setVisibility(View.GONE);
        }
    }

    private void changeMode() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.warning_mode_change))
                .setMessage(new CurrentMode(getBaseContext()).getWarningMessage())
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().clear().apply();
                        Intent intent = new Intent(getBaseContext(), Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(new CurrentMode(getBaseContext()).getIcon())
                .show();
    }

    /**
     * This will set current activity and UI will set accordingly
     *
     * @return CurrentActivity (enum)
     */
    protected abstract CurrentActivity setCurrentActivity();

}