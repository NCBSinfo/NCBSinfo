package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.adapters.ViewpagerAdapter;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.fragments.DevelopersLogFragment;
import com.rohitsuratekar.NCBSinfo.fragments.EventsLogFragment;
import com.rohitsuratekar.NCBSinfo.fragments.OldEventLogFragment;
import com.rohitsuratekar.NCBSinfo.helpers.NetworkRelated;

public class EventUpdates extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_updates);
        Toolbar toolbar = (Toolbar) findViewById(R.id.update_toolbar);
        setSupportActionBar(toolbar);

        //Send user to register first
        if(!PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_REGISTERED,false)){
            startActivity(new Intent(this, Registration.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ViewPager viewPager = (ViewPager) findViewById(R.id.update_viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.update_tabs);
        tabLayout.setupWithViewPager(viewPager);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.Navigation_Name);
        TextView email = (TextView)header.findViewById(R.id.Navigation_Email);
        if(name!=null) {
            name.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Preferences.PREF_USERNAME, "username"));
            email.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(Preferences.PREF_EMAIL, "email@domain.com"));
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new EventsLogFragment(), "Upcoming events");
        adapter.addFragment(new OldEventLogFragment(), "Past events");
        viewPager.setAdapter(adapter);
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
        getMenuInflater().inflate(R.menu.event_updates, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id==R.id.action_settings){
            startActivity(new Intent(this,Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {startActivity(new Intent(this, Home.class));
        } else if (id == R.id.nav_transport) {
            Intent i = new Intent(this,Transport.class);
            i.putExtra(General.GEN_TRANSPORT_INTENT,"0");
            startActivity(i);
        } else if (id == R.id.nav_updates) { startActivity(new Intent(this,EventUpdates.class));
        } else if (id == R.id.nav_experimental) {startActivity(new Intent(this,Experimental.class));
        } else if (id == R.id.nav_settings) {startActivity(new Intent(this, Settings.class));
        } else if (id == R.id.nav_registration){startActivity(new Intent(this,Registration.class));}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
