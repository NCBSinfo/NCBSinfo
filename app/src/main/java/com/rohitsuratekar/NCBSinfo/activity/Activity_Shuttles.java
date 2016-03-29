package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.fragments.fragment_shuttles_all_list;

public class Activity_Shuttles extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        private SectionsPagerAdapter mSectionsPagerAdapter;
        private ViewPager mViewPager;
        TabLayout tabLayout;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shuttles_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        Intent intent = getIntent();
        String currentSwitch = intent.getExtras().getString("switch", "0");



        int currentInt = Integer.parseInt(currentSwitch);
        if (currentInt>4){currentInt=currentInt-1;}  //Remove extra buggy pointer
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
            overridePendingTransition(R.anim.activity_slide_left, R.anim.activity_slide_left_half);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__shuttles, menu);
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
        int newIndex = 0;
        int id = item.getItemId();
        if (id == R.id.nav_home) { Intent i = new Intent(this, Home.class); startActivity(i);
        } else if (id == R.id.nav_transport) { Intent i = new Intent(this, Activity_Shuttles.class); i.putExtra("switch","0");startActivity(i);
        } else if (id == R.id.nav_contact) { Intent i = new Intent(this, Activity_Contact.class); i.putExtra("switch","0");startActivity(i);
        } else if (id == R.id.nav_updates) { Intent i = new Intent(this, Activity_NotificationReceiver.class); startActivity(i);
        } else if (id == R.id.nav_other) { Intent i = new Intent(this, Activity_Extra.class); startActivity(i);
        } else if (id == R.id.nav_settings) { Intent i = new Intent(this, Settings.class); startActivity(i);
        } else if (id == R.id.nav_shuttle_ncbs_iisc) { newIndex = 0;
        } else if (id == R.id.nav_shuttle_iisc_ncbs) { newIndex = 1;
        } else if (id == R.id.nav_shuttle_ncbs_mandara) { newIndex = 2;
        } else if (id == R.id.nav_shuttle_mandara_ncbs) { newIndex = 3;
        } else if (id == R.id.nav_buggy) { newIndex = 4;
        } else if (id == R.id.nav_shuttle_ncbs_icts) { newIndex = 5;
        } else if (id == R.id.nav_shuttle_icts_ncbs) { newIndex = 6;
        } else if (id == R.id.nav_shuttle_ncbs_cbl) { newIndex = 7;
        }

        TabLayout.Tab tab = tabLayout.getTabAt(newIndex);
        assert tab != null;
        tab.select();

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
            switch(position){
                case 0: return fragment_shuttles_all_list.newInstance(0);
                case 1 :return fragment_shuttles_all_list.newInstance(1);
                case 2 :return fragment_shuttles_all_list.newInstance(2);
                case 3: return fragment_shuttles_all_list.newInstance(3);
                case 4 :return fragment_shuttles_all_list.newInstance(4);
                case 5: return fragment_shuttles_all_list.newInstance(5);
                case 6 :return fragment_shuttles_all_list.newInstance(6);
                case 7 :return fragment_shuttles_all_list.newInstance(7);
                default: return fragment_shuttles_all_list.newInstance(0);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:

                    return "NCBS-IISc";
                case 1:
                    return "IISc-NCBS";
                case 2:
                    return "NCBS-Mandara";
                case 3:
                    return "Mandara-NCBS";
                case 4:
                    return "Buggy";
                case 5:
                    return "NCBS-ICTS";
                case 6:
                    return "ICTS-NCBS";
                case 7:
                    return "NCBS-CBL";

            }
            return null;
        }
    }
}
