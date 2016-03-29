package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_extra_grid;

public class Activity_Extra extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        GridView grid = (GridView)findViewById(R.id.Otherinfo_Grid);
        final String[] names = getResources().getStringArray(R.array.extra_info_icons);
        final int[] icons = {
                R.drawable.icon_lecturehall,
                R.drawable.icon_canteen,
                R.drawable.icon_experimental
        };
        adapters_extra_grid adapter = new adapters_extra_grid(Activity_Extra.this, names, icons);
        grid.setAdapter(adapter);
        grid.setFocusable(false);
        grid.setFocusableInTouchMode(false);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        Intent intent = new Intent(Activity_Extra.this, Activity_LectureHalls.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(Activity_Extra.this, Activity_Canteen.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(Activity_Extra.this, Activity_Experimental.class);
                        startActivity(intent3);
                        break;
                }
            }
        });

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
        getMenuInflater().inflate(R.menu.activity__extra, menu);
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
        } else if (id == R.id.nav_other_canteen) { Intent i = new Intent(this, Activity_Canteen.class); startActivity(i);
        } else if (id == R.id.nav_other_lecture) { Intent i = new Intent(this, Activity_LectureHalls.class); startActivity(i);
        } else if (id == R.id.nav_other_experimental) { Intent i = new Intent(this, Activity_Experimental.class); startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
