package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
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

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_viewpager;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.constants.SQLConstants;
import com.rohitsuratekar.NCBSinfo.fragments.fragment_contact_tab1;
import com.rohitsuratekar.NCBSinfo.fragments.fragment_contact_tab2;
import com.rohitsuratekar.NCBSinfo.helper.helper_contact_list;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_database;

public class Activity_Contact extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Boolean Firstvalue = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(SQLConstants.CONTACT_LOADED, true);

        if (Firstvalue){
            DatabaseHelper db = new DatabaseHelper(getBaseContext());
            String[][] clist = new helper_contact_list().allContacts();
            for (int i=0; i <clist.length; i++){
                db.addContact(new models_contacts_database(1, clist[i][0], clist[i][1],clist[i][2], clist[i][3], "0"));
            }
            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(SQLConstants.CONTACT_LOADED, false).apply();
            db.close();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_contact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Activity_Contact.this, Activity_AddContact.class);
                intent.putExtra("forEdit", 0);
                intent.putExtra("feldID", 0);
                startActivity(intent);
            }
        });

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

        ViewPager viewPager = (ViewPager) findViewById(R.id.contact_viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.contact_tabs);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        String currentSwitch = intent.getExtras().getString("switch", "0");
        int currentInt = Integer.parseInt(currentSwitch);
        TabLayout.Tab tab = tabLayout.getTabAt(currentInt);
        assert tab != null;
        tab.select();

    }

    private void setupViewPager(ViewPager viewPager) {
        adapters_viewpager adapter = new adapters_viewpager(getSupportFragmentManager());
        adapter.addFragment(new fragment_contact_tab1(), "All Contacts");
        adapter.addFragment(new fragment_contact_tab2(), "Favorite");
        viewPager.setAdapter(adapter);


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.activity_slide_right, R.anim.activity_right_half);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__contact, menu);
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
        } else if (id == R.id.nav_contact_add) { Intent i = new Intent(this, Activity_AddContact.class);
            i.putExtra("forEdit", 0);
            i.putExtra("feldID", 0);
            startActivity(i);
        } else if (id == R.id.nav_contact_fav) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
        } else if (id == R.id.nav_contact_all) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            assert tab != null;
            tab.select();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
