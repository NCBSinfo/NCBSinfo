package com.rohitsuratekar.NCBSinfo.online.dashboard;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.common.NavigationIDs;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.common.utilities.CustomNavigationView;
import com.rohitsuratekar.NCBSinfo.common.utilities.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.online.temp.camp.CAMP;

import java.util.Collections;
import java.util.List;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserInformation {

    public static final String MODE_CONSTANT = "dashBoard";
    SharedPreferences pref;
    CurrentMode mode;

    TextView name, email;
    RecyclerView recyclerView;
    List<NotificationModel> fullList;
    NotificationAdapter adapter;

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

        name.setText(pref.getString(registration.USERNAME, "Username"));
        email.setText(pref.getString(registration.EMAIL, "email@domain.com"));

        recyclerView = (RecyclerView) findViewById(R.id.dashboard_recycleview);
        fullList = new NotificationData(getBaseContext()).getAll();
        Collections.reverse(fullList);
        adapter = new NotificationAdapter(fullList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(DashBoard.this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new NotificationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                final Dialog dialog = new Dialog(DashBoard.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.notification_viewer);
                dialog.setCanceledOnTouchOutside(true);
                TextView NoteTitle = (TextView) dialog.findViewById(R.id.notificationViewer_title);
                TextView NoteMessage = (TextView) dialog.findViewById(R.id.notificationViewer_message);
                TextView NoteTimestamp = (TextView) dialog.findViewById(R.id.notificationViewer_timestamp);
                String title = fullList.get(position).getTitle();
                if (title.length() < 100) {
                    //This is to keep width of dialog long enough
                    title = String.format("%1$-" + (100 - title.length()) + "s", title);
                }
                NoteMessage.setText(fullList.get(position).getMessage());
                NoteTitle.setText(title);
                NoteTimestamp.setText(fullList.get(position).getTimestamp());
                dialog.show();

            }
        });

        if (pref.getString(MODE, ONLINE).equals(registration.camp16.CAMP_MODE)) {
            Intent i = new Intent(DashBoard.this, CAMP.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }


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
        startActivity(new NavigationIDs(item.getItemId(), DashBoard.this).getIntent());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
