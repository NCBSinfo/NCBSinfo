package com.rohitsuratekar.NCBSinfo.offline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.Settings;
import com.rohitsuratekar.NCBSinfo.common.contacts.ContactColors;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.lecturehalls.LectureHalls;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.common.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class OfflineHome extends AppCompatActivity implements View.OnClickListener {

    TextView title, timeLeft, nextText, footerNotice;
    ImageView image, previous, next;
    ImageView icon_transport, icon_settings, icon_lecturehall, icon_contacts;
    LinearLayout footerHolder, homeFooter, titleHolder, cardLayout;
    RelativeLayout homeLayout;
    TransportModel transport;
    SharedPreferences pref;
    LinearLayout hour_back, min_back, sec_back;
    LinearLayout hour, min, sec;
    TextView hourText, minText, secText;
    LinearLayout.LayoutParams params1,params2,params3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline_home);

        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //Set up transport
        transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(), pref.getInt(Transport.DEFAULT_ROUTE, TransportConstants.ROUTE_NCBS_IISC));

        //Find UI Elements
        homeLayout = (RelativeLayout) findViewById(R.id.off_home_layout);
        footerHolder = (LinearLayout) findViewById(R.id.off_home_footerHolder);
        homeFooter = (LinearLayout) findViewById(R.id.off_home_footer);
        titleHolder = (LinearLayout) findViewById(R.id.off_home_title_holder);
        title = (TextView) findViewById(R.id.off_home_cardview_title);
        nextText = (TextView) findViewById(R.id.off_home_cardView_nextText);
        footerNotice = (TextView) findViewById(R.id.off_home_footer_notice);
        image = (ImageView) findViewById(R.id.off_home_cardView_selector);
        previous = (ImageView) findViewById(R.id.off_previousRoute);
        next = (ImageView) findViewById(R.id.off_nextRoute);
        icon_transport = (ImageView) findViewById(R.id.off_home_icon_transport);
        icon_settings = (ImageView) findViewById(R.id.off_home_icon_settings);
        icon_lecturehall = (ImageView) findViewById(R.id.off_home_icon_lecture);
        icon_contacts = (ImageView) findViewById(R.id.off_home_icon_contacts);
        hour_back = (LinearLayout)findViewById(R.id.hour_background);
        hour = (LinearLayout)findViewById(R.id.hour_layout);
        min_back = (LinearLayout)findViewById(R.id.minute_background);
        min = (LinearLayout)findViewById(R.id.minute_layout);
        sec_back = (LinearLayout)findViewById(R.id.seconds_background);
        sec = (LinearLayout)findViewById(R.id.seconds_layout);
        cardLayout = (LinearLayout)findViewById(R.id.off_card_layout);
        hourText = (TextView)findViewById(R.id.off_hour_text);
        minText = (TextView)findViewById(R.id.off_min_text);
        secText = (TextView)findViewById(R.id.off_sec_text);

        params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);



        footerNotice.setText(getResources().getString(R.string.home_offline).toUpperCase());
        //Add listener
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        icon_lecturehall.setOnClickListener(this);
        icon_transport.setOnClickListener(this);
        icon_settings.setOnClickListener(this);
        icon_contacts.setOnClickListener(this);
        title.setOnClickListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(OfflineHome.this, v);
                popupMenu.inflate(R.menu.popup_card);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.popup_addfav) {
                            pref.edit().putInt(Transport.DEFAULT_ROUTE, transport.getRouteNo()).apply();
                            Snackbar.make(homeLayout, getResources().getString(R.string.snackbar_added_route, transport.getFrom().toUpperCase(), transport.getTo().toUpperCase()), Snackbar.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.popup_remove) {
                            pref.edit().putInt(Transport.DEFAULT_ROUTE, TransportConstants.ROUTE_NCBS_IISC).apply();
                            Snackbar.make(homeLayout, getResources().getString(R.string.snackbar_reset_route), Snackbar.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        changeTransport();

        Timer timeLeft = new Timer();
        timeLeft.schedule(new TimerTask() {
            @Override
            public void run() {
                runTimer();
            }

        }, 0, 1000); //1000 is milliseconds for each time tick

        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });

    }

    private void runTimer() {
        this.runOnUiThread(Timer_Tick);
    }

    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            changeTransport();
        }
    };

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.off_previousRoute:
                transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(),
                        new OnlineHome().changeRoute(transport.getRouteNo(), false));
                changeTransport();
                break;
            case R.id.off_nextRoute:
                transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(),
                        new OnlineHome().changeRoute(transport.getRouteNo(), true));
                changeTransport();
                break;
            case R.id.off_home_icon_settings:
                startActivity(new Intent(this, Settings.class));
                break;
            case R.id.off_home_icon_lecture:
                startActivity(new Intent(this, LectureHalls.class));
                break;
            case R.id.off_home_icon_transport:
                Intent i1 = new Intent(this, Transport.class);
                i1.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i1);
                break;
            case R.id.off_home_icon_contacts:
                startActivity(new Intent(this, Contacts.class));
                break;
            case R.id.off_home_cardview_title:
                Intent i2 = new Intent(this, Transport.class);
                i2.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i2);
                break;
            default:
                Toast.makeText(getBaseContext(), "No item found", Toast.LENGTH_LONG).show();
                break;
        }

    }

    public void changeTransport() {
        title.setText(getString(R.string.home_trasnport_title, transport.getFrom().toUpperCase(), transport.getTo().toUpperCase()));
        nextText.setText(getResources().getString(R.string.next_transport,
                transport.getType(), new TransportHelper(getBaseContext()).convertToSimpleDate(transport.getNextTrip())));

        float[] Difference = transport.getTimeLeft();

        //Update transport of timeleft goes in negative
        float sum = 0;
        for(float f : Difference){ sum = sum+f;}
        if(sum<0){transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(),transport.getRouteNo());}

        hourText.setText((int) Difference[2]+" Hrs");
        minText.setText((int) Difference[1]+" Min");
        secText.setText((int) Difference[0]+" Sec");
        if(Difference[2]>12){
            Difference[2]=12;
        }
        params1.weight = (Difference[2]/12)*100;  hour.setLayoutParams(params1);
        params2.weight = (Difference[1]/60)*100;  min.setLayoutParams(params2);
        params3.weight = (Difference[0]/60)*100;  sec.setLayoutParams(params3);

    }

    public void changeColor(){
        Random r = new Random();
        char c = (char)(r.nextInt(26)+'a');
        hour.setBackgroundColor(new ContactColors(getBaseContext(), String.valueOf(c).toLowerCase()).getColor());
        min.setBackgroundColor(new ContactColors(getBaseContext(), String.valueOf(c).toLowerCase()).getColor());
        sec.setBackgroundColor(new ContactColors(getBaseContext(), String.valueOf(c).toLowerCase()).getColor());
    }

}
