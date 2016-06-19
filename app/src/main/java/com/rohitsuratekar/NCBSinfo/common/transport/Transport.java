package com.rohitsuratekar.NCBSinfo.common.transport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.common.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.common.utilities.CustomNavigationView;

public class Transport extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Public
    public static final String INDENT = "transportIndent";
    public static final String MODE_CONSTANT = "transport";

    //Local
    private final String TAG = this.getClass().getSimpleName();
    TabLayout tabLayout;

    SharedPreferences pref;
    CurrentMode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.transport_toolbar);
        setSupportActionBar(toolbar);
        //Initialization
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mode = new CurrentMode(getBaseContext(),MODE_CONSTANT);
        //UI setup
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Personalization
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new CustomNavigationView(navigationView, this, mode);

        // Set up the ViewPager with the sections adapter.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.transport_viewpager);
        tabLayout = (TabLayout) findViewById(R.id.transport_tabs);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        //Focus selected path
        Intent intent = getIntent();
        String currentSwitch = intent.getExtras().getString(INDENT, "0");
        int currentInt = Integer.parseInt(currentSwitch);
        if (currentInt>TransportConstants.ROUTE_BUGGY_NCBS){currentInt=currentInt-1;}  //Remove extra buggy pointer
        TabLayout.Tab tab = tabLayout.getTabAt(currentInt);
        assert tab != null;
        tab.select();
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
        getMenuInflater().inflate(R.menu.transport, menu);
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
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return TransportFragment.newInstance(1, false);
                case 1:
                    return TransportFragment.newInstance(2, false);
                case 2:
                    return TransportFragment.newInstance(3, false);
                case 3:
                    return TransportFragment.newInstance(4, false);
                case 4:
                    return TransportFragment.newInstance(5, true); //This is buggy and also 6
                case 5:
                    return TransportFragment.newInstance(7, false);
                case 6:
                    return TransportFragment.newInstance(8, false);
                case 7:
                    return TransportFragment.newInstance(9, false);
                default:
                    return TransportFragment.newInstance(0, false);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.route_ncbs_iisc);
                case 1:
                    return getResources().getString(R.string.route_iisc_ncbs);
                case 2:
                    return getResources().getString(R.string.route_ncbs_mandara);
                case 3:
                    return getResources().getString(R.string.route_mandara_ncbs);
                case 4:
                    return getResources().getString(R.string.buggy);
                case 5:
                    return getResources().getString(R.string.route_ncbs_icts);
                case 6:
                    return getResources().getString(R.string.route_icts_ncbs);
                case 7:
                    return getResources().getString(R.string.route_ncbs_cbl);

            }
            return null;
        }
    }
}
