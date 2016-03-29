package com.rohitsuratekar.NCBSinfo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.helper.helper_AES;

public class Activity_Experimental extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experimental);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        final AlertDialog alertDialog = new AlertDialog.Builder(
                Activity_Experimental.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Oops!");

        // Setting Dialog Message
        alertDialog.setMessage("These functions are available only to CCCP or CAMP users");

        // Setting OK Button
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
            }
        });

        // Showing Alert Message
        alertDialog.show();

        final ImageButton cccp = (ImageButton)findViewById(R.id.exp_cccp_go);
        final ImageButton camp = (ImageButton)findViewById(R.id.exp_camp_go);
        final Button web_cccp = (Button)findViewById(R.id.exp_cccpButton);
        final Button web_camp = (Button)findViewById(R.id.exp_campButton);
        web_camp.setVisibility(View.INVISIBLE);
        web_cccp.setVisibility(View.INVISIBLE);
        cccp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Experimental.this);
                final EditText edittext = new EditText(Activity_Experimental.this);
                alert.setTitle("Log in");
                alert.setMessage("Enter CCCP ID given by organisers");
                alert.setView(edittext);

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getBaseContext(), "Invalid ID!", Toast.LENGTH_LONG).show();
                        web_cccp.setVisibility(View.VISIBLE);
                        cccp.setEnabled(false);
                        cccp.setColorFilter(R.color.experimental_diabledButtons);
                    }
                });

                alert.show();

            }
        });

        camp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder alert = new AlertDialog.Builder(Activity_Experimental.this);
                final EditText edittext = new EditText(Activity_Experimental.this);
                alert.setTitle("Log in");
                alert.setMessage("Enter CAMP email ID");
                alert.setView(edittext);

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getBaseContext(), "You are not allowed!", Toast.LENGTH_LONG).show();
                        web_camp.setVisibility(View.VISIBLE);
                        camp.setEnabled(false);
                        camp.setColorFilter(R.color.experimental_diabledButtons);
                    }
                });

                alert.show();

            }
        });

        web_camp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://camp.ncbs.res.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        web_cccp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://events.ncbs.res.in/event/discussion-meeting-conflict-and-cooperation-cellular-populations-cccp-2016";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity__experimental, menu);
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
