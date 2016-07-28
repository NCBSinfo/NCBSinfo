package com.rohitsuratekar.NCBSinfo.online.temp.camp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.background.DataManagement;
import com.rohitsuratekar.NCBSinfo.background.NetworkOperations;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.common.NavigationIDs;
import com.rohitsuratekar.NCBSinfo.common.contacts.ContactList;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.lecturehalls.LectureHalls;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.common.utilities.AutoConfiguration;
import com.rohitsuratekar.NCBSinfo.common.utilities.CustomNavigationView;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.interfaces.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.online.login.Login;

public class CAMP extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UserInformation, View.OnClickListener {


    public static final String MODE_CONSTANT = "camp16";
    private final String TAG = getClass().getSimpleName();

    SharedPreferences pref;
    CurrentMode mode;
    ImageView transport, contact, location, campevents;
    RelativeLayout mainLayout;
    LinearLayout warningLayout;
    Button signIn;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camp);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initialization
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        mode = new CurrentMode(getBaseContext(), MODE_CONSTANT);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "Login state changed");
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent service = new Intent(getBaseContext(), DataManagement.class);
                    service.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATE);
                    getBaseContext().startService(service);
                }
            }
        };

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        new CustomNavigationView(navigationView, this, mode);

        transport = (ImageView) findViewById(R.id.camp_home_icon_transport);
        contact = (ImageView) findViewById(R.id.camp_home_icon_contacts);
        location = (ImageView) findViewById(R.id.camp_home_icon_lecture);
        campevents = (ImageView) findViewById(R.id.camp_home_icon_event);
        signIn = (Button) findViewById(R.id.camp_login);

        transport.setOnClickListener(this);
        contact.setOnClickListener(this);
        location.setOnClickListener(this);
        campevents.setOnClickListener(this);
        signIn.setOnClickListener(this);

        mainLayout = (RelativeLayout) findViewById(R.id.camp_main);
        warningLayout = (LinearLayout) findViewById(R.id.camp_warning);


        //If it is camp user and has activated CAMP mode, then only do all data requests.

        if (pref.getString(MODE, ONLINE).equals(registration.camp16.CAMP_MODE)) {

            mainLayout.setVisibility(View.VISIBLE);
            warningLayout.setVisibility(View.GONE);

            //Get details
            if (!pref.getBoolean(firstTime.CAMP_EVENTS_FETCHED, false)) {
                Intent service = new Intent(getBaseContext(), NetworkOperations.class);
                service.putExtra(NetworkOperations.INTENT, NetworkOperations.CAMP16);
                startService(service);
            }

            //Submit registration details
            if (!pref.getBoolean(netwrok.REGISTRATION_DETAILS_SENT, false)) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                pref.edit().putString(registration.FIREBASE_TOKEN, refreshedToken).apply();
                FirebaseMessaging.getInstance().subscribeToTopic(NetworkConstants.topics.PUBLIC);
                FirebaseMessaging.getInstance().subscribeToTopic(NetworkConstants.topics.EMERGENCY);
                Intent service = new Intent(getBaseContext(), NetworkOperations.class);
                service.putExtra(NetworkOperations.INTENT, NetworkOperations.REGISTER);
                startService(service);
                Log.d(TAG, "Subscribed with topic");
            }

            if (pref.getBoolean(firstTime.FIRST_NOTIFICATION_DASHBOARD, true)) {
                new AutoConfiguration(getBaseContext()).nameNotifications();
            }


        } else {
            mainLayout.setVisibility(View.GONE);
            warningLayout.setVisibility(View.VISIBLE);
        }

        if (isCampUser()) {
            signIn.setText("ACTIVATE CAMP MODE");
        }

        if (pref.getBoolean(Contacts.FIRST_TIME_CONTACT, true)) {
            String[][] clist = new ContactList().allContacts();
            for (int j = 0; j < clist.length; j++) {
                new ContactsData(getBaseContext()).add(new ContactModel(1, clist[j][0], clist[j][1], clist[j][2], clist[j][3], "0"));
            }
            pref.edit().putBoolean(Contacts.FIRST_TIME_CONTACT, false).apply();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            if (pref.getString(MODE, ONLINE).equals(registration.camp16.CAMP_MODE)) {
                mAuth.addAuthStateListener(mAuthListener);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            if (pref.getString(MODE, ONLINE).equals(registration.camp16.CAMP_MODE)) {
                mAuth.removeAuthStateListener(mAuthListener);
            }
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
        getMenuInflater().inflate(R.menu.cam, menu);
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
            startActivity(new Intent(CAMP.this, Settings.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        startActivity(new NavigationIDs(item.getItemId(), CAMP.this).getIntent());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.camp_home_icon_transport:
                Intent intent = new Intent(this, Transport.class);
                intent.putExtra(Transport.INDENT, "0");
                startActivity(intent);
                break;
            case R.id.camp_home_icon_contacts:
                startActivity(new Intent(this, Contacts.class));
                break;
            case R.id.camp_home_icon_lecture:
                startActivity(new Intent(this, LectureHalls.class));
                break;
            case R.id.camp_home_icon_event:
                startActivity(new Intent(this, CAMPevents.class));
                break;
            case R.id.camp_login:
                if (isCampUser()) {
                    pref.edit().putString(MODE, registration.camp16.CAMP_MODE).apply();
                    Intent i = new Intent(CAMP.this, Home.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                } else {
                    pref.edit().remove(MODE).apply();
                    Intent i = new Intent(CAMP.this, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                }

        }

    }

    private boolean isCampUser() {
        return pref.getString(registration.EMAIL, "email@domain.com").split("@")[1].equals(registration.camp16.CAMP_PATTERN)
                || pref.getBoolean(registration.camp16.IS_CAMP_USER, false);
    }


}



