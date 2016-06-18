package com.rohitsuratekar.NCBSinfo.common.transport;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.Login;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.online.fragments.RegisterFragment;

public class Transport extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    //Public
    public static final String INDENT = "transportIndent";
    //Local
    private final String TAG = this.getClass().getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
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
        mode = new CurrentMode(getBaseContext(), pref.getString(Home.MODE, Home.ONLINE));
        //UI setup
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //Personalization
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.inflateHeaderView(mode.getDrawerHeader());
        navigationView.inflateMenu(mode.getDrawerMenu());
        MenuItem currentMenu = navigationView.getMenu().findItem(R.id.nav_dashboard);
        if (currentMenu != null) {
            currentMenu.setTitle(pref.getString(RegisterFragment.USERNAME, "User").trim().split(" ")[0] + "\'s " + getString(R.string.dashboard));
        }
        View header = navigationView.getHeaderView(0);
        TextView name = (TextView) header.findViewById(R.id.Navigation_Name);
        TextView email = (TextView) header.findViewById(R.id.Navigation_Email);
        ImageView switchButton = (ImageView) header.findViewById(R.id.switch_mode);
        if (name != null) {
            name.setText(pref.getString(RegisterFragment.USERNAME, "User Name"));
            email.setText(pref.getString(RegisterFragment.EMAIL, "email@domain.com"));
        }
        if (switchButton != null) {
            switchButton.setImageResource(mode.getIcon());
            switchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawer.closeDrawer(GravityCompat.START);
                    final AlertDialog alertDialog = new AlertDialog.Builder(Transport.this).create();
                    alertDialog.setTitle(getString(R.string.warning_mode_change));
                    alertDialog.setMessage(mode.getSwitchModeMessage());
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Transport.this, Login.class));
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Not sure", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            alertDialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }

        // Set up the ViewPager with the sections adapter.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.transport_viewpager);
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
                    return TransportFragment.newInstance(1);
                case 1:
                    return TransportFragment.newInstance(2);
                case 2:
                    return TransportFragment.newInstance(3);
                case 3:
                    return TransportFragment.newInstance(4);
                case 4:
                    return TransportFragment.newInstance(5);
                case 5:
                    return TransportFragment.newInstance(6);
                case 6:
                    return TransportFragment.newInstance(7);
                default:
                    return TransportFragment.newInstance(0);
            }
        }

        @Override
        public int getCount() {
            return 7;
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
                    return getResources().getString(R.string.route_ncbs_icts);
                case 5:
                    return getResources().getString(R.string.route_icts_ncbs);
                case 6:
                    return getResources().getString(R.string.route_ncbs_cbl);

            }
            return null;
        }
    }
}
