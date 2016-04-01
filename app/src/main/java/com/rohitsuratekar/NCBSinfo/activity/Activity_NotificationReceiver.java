package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_viewpager;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.fragments.fragment_notification_log;
import com.rohitsuratekar.NCBSinfo.fragments.fragment_notification_recent;

public class Activity_NotificationReceiver extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_receiver_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.Navigation_Name);
        TextView email = (TextView)header.findViewById(R.id.Navigation_Email);
        if(name!=null) {
            name.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DATA_USERNAME, "username"));
            email.setText(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(GCMConstants.DATA_EMAIL, "email@domain.com"));
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.notification_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.notification_tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapters_viewpager adapter = new adapters_viewpager(getSupportFragmentManager());
        adapter.addFragment(new fragment_notification_recent(), "Recent");
        adapter.addFragment(new fragment_notification_log(), "Log");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_slide_left, R.anim.activity_slide_left_half);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__notification_receiver, menu);
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
            startActivity(new Intent(this,Settings.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) { Intent i = new Intent(this, Home.class); startActivity(i);
        } else if (id == R.id.nav_transport) { Intent i = new Intent(this, Activity_Shuttles.class); i.putExtra("switch","0");startActivity(i);
        } else if (id == R.id.nav_contact) { Intent i = new Intent(this, Activity_Contact.class); i.putExtra("switch","0");startActivity(i);
        } else if (id == R.id.nav_updates) { Intent i = new Intent(this, Activity_NotificationReceiver.class); startActivity(i);
        } else if (id == R.id.nav_other) { Intent i = new Intent(this, Activity_Extra.class); startActivity(i);
        } else if (id == R.id.nav_settings) { Intent i = new Intent(this, Settings.class); startActivity(i);
        } else if (id == R.id.nav_updates_registration) { Intent i = new Intent(this, Activity_GCMRegistration.class); startActivity(i);
        } else if (id == R.id.nav_updates_moderator) { Intent i = new Intent(this, Activity_GCMModeration.class); startActivity(i);
        } else if (id == R.id.nav_updates_log) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
