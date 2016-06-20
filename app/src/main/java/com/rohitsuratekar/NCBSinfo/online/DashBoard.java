package com.rohitsuratekar.NCBSinfo.online;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.CustomNavigationView;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.rohitsuratekar.NCBSinfo.online.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.online.login.Registration;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MODE_CONSTANT = "dashBoard";
    SharedPreferences pref;
    CurrentMode mode;

    TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dash_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Initialization
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mode = new CurrentMode(getBaseContext(), MODE_CONSTANT);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new CustomNavigationView(navigationView, this, mode);

        name = (TextView) findViewById(R.id.dashboard_name);
        email = (TextView) findViewById(R.id.dashboard_email);

        name.setText(pref.getString(Registration.USERNAME, "Username"));
        email.setText(pref.getString(Registration.EMAIL, "email@domain.com"));

        ImageView myImageView = (ImageView) findViewById(R.id.dashboard_logo);
        Animation myFadeInAnimation = AnimationUtils.loadAnimation(DashBoard.this, R.anim.alpha_repeate);
        myImageView.startAnimation(myFadeInAnimation);


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
        getMenuInflater().inflate(R.menu.dash_board, menu);
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
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, Home.class));
        } else if (id == R.id.nav_transport) {
            Intent i = new Intent(this, Transport.class);
            i.putExtra(Transport.INDENT, TransportConstants.ROUTE_NCBS_IISC);
            startActivity(i);
        } else if (id == R.id.nav_updates) {
            startActivity(new Intent(this, Events.class));
        } else if (id == R.id.nav_experimental) {
            startActivity(new Intent(this, Experimental.class));
        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(this, Settings.class));
        } else if (id == R.id.nav_contacts) {
            startActivity(new Intent(this, Contacts.class));
        } else if (id == R.id.nav_dashboard) {
            startActivity(new Intent(this, DashBoard.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
