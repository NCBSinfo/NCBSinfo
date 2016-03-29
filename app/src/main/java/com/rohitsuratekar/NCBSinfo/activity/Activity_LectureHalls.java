package com.rohitsuratekar.NCBSinfo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_lecturehall;
import com.rohitsuratekar.NCBSinfo.helper.helper_lecturehall;

import java.util.ArrayList;

public class Activity_LectureHalls extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListView expListView;
    private int lastExpandedPosition = -1;
    adapters_lecturehall mLectureAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_halls);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        expListView = (ExpandableListView) findViewById(R.id.LectureHallList);

        setGroupData();
        //setChildGroupData();
        mLectureAdapter = new adapters_lecturehall(groupItem, childItem);
        mLectureAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);

        // setting list adapter
        expListView.setAdapter(mLectureAdapter);
        registerForContextMenu(expListView);
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {




            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
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
        getMenuInflater().inflate(R.menu.activity__lecture_halls, menu);
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


    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    public void setGroupData() {

        groupItem.clear();
        ArrayList<String> child ;
        ArrayList<String[]> temparray = new helper_lecturehall().listHalls();
        for(int i=0;i<temparray.size();i++) {
            child = new ArrayList<String>();
            groupItem.add("<b>" + temparray.get(i)[0] + "</b><small> (" + temparray.get(i)[1]+")</small>");
            child.add(temparray.get(i)[3]+" , "+temparray.get(i)[2]);
            child.add("<small>"+temparray.get(i)[4]+"</small>");
            childItem.add(child);
        }

    }
}
