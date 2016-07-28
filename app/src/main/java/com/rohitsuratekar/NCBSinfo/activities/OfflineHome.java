package com.rohitsuratekar.NCBSinfo.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;
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
import com.rohitsuratekar.NCBSinfo.activities.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.locations.LectureHalls;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteBuilder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportDynamics;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportRoute;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.Arrays;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OfflineHome extends BaseActivity implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.OFFLINE_HOME;
    }


    //Set up UI

    @BindView(R.id.off_home_layout)
    RelativeLayout homeLayout;
    @BindView(R.id.off_home_footerHolder)
    LinearLayout footerHolder;
    @BindView(R.id.off_home_footer)
    LinearLayout homeFooter;
    @BindView(R.id.off_home_title_holder)
    LinearLayout titleHolder;
    @BindView(R.id.hour_background)
    LinearLayout hour_back;
    @BindView(R.id.hour_layout)
    LinearLayout hour;
    @BindView(R.id.minute_background)
    LinearLayout min_back;
    @BindView(R.id.minute_layout)
    LinearLayout min;
    @BindView(R.id.seconds_background)
    LinearLayout sec_back;
    @BindView(R.id.seconds_layout)
    LinearLayout sec;
    @BindView(R.id.off_card_layout)
    LinearLayout cardLayout;

    @BindView(R.id.off_home_cardview_title)
    TextView title;
    @BindView(R.id.off_home_cardView_nextText)
    TextView nextText;
    @BindView(R.id.off_home_footer_notice)
    TextView footerNotice;
    @BindView(R.id.off_hour_text)
    TextView hourText;
    @BindView(R.id.off_min_text)
    TextView minText;
    @BindView(R.id.off_sec_text)
    TextView secText;

    @BindView(R.id.off_home_cardView_selector)
    ImageView image;
    @BindView(R.id.off_previousRoute)
    ImageView previous;
    @BindView(R.id.off_nextRoute)
    ImageView next;
    @BindView(R.id.off_home_icon_transport)
    ImageView icon_transport;
    @BindView(R.id.off_home_icon_settings)
    ImageView icon_settings;
    @BindView(R.id.off_home_icon_lecture)
    ImageView icon_lecturehall;
    @BindView(R.id.off_home_icon_contacts)
    ImageView icon_contacts;

    //Variables
    Preferences pref;
    TransportRoute transport;
    LinearLayout.LayoutParams params1, params2, params3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        pref = new Preferences(getBaseContext());
        transport = new RouteBuilder(pref.user().getDefaultRoute(), getBaseContext()).build();

        //Set up toolbar title
        setTitle("");
        TextView specialTitle = (TextView) toolbar.findViewById(R.id.special_tool_bar_title);
        specialTitle.setText(getString(R.string.app_name));
        specialTitle.setVisibility(View.VISIBLE);

        //3 Different instance needed
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
                popupMenu.inflate(R.menu.home_cardview_popup);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.popup_addfav) {
                            pref.user().setDefaultRoute(transport.getRoute());
                            Snackbar.make(homeLayout, getResources().getString(R.string.snackbar_added_route, transport.getOrigin().toUpperCase(), transport.getDestination().toUpperCase()), Snackbar.LENGTH_SHORT).show();
                        } else if (item.getItemId() == R.id.popup_remove) {
                            pref.user().setDefaultRoute(Routes.NCBS_IISC);
                            Snackbar.make(homeLayout, getResources().getString(R.string.snackbar_reset_route), Snackbar.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        cardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeColor();
            }
        });

        changeTransport();
        runnable.run();
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {

        public void run() {
            changeTransport();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onDestroy() {
        Log.i(TAG, "Timer stopped");
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "Timer stopped");
        handler.removeCallbacks(runnable);
        super.onPause();
    }

    @Override
    protected void onRestart() {
        runnable.run();
        super.onRestart();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.off_previousRoute:
                transport = new RouteBuilder(new TransportHelper().changeRoute(transport.getRoute(), false), getBaseContext()).build();
                changeTransport();
                break;
            case R.id.off_nextRoute:
                transport = new RouteBuilder(new TransportHelper().changeRoute(transport.getRoute(), true), getBaseContext()).build();
                changeTransport();
                break;
            case R.id.off_home_icon_settings:
                startActivity(new Intent(this, Settings.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.off_home_icon_lecture:
                startActivity(new Intent(this, LectureHalls.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.off_home_icon_transport:
                Intent i1 = new Intent(this, Transport.class);
                i1.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i1);
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.off_home_icon_contacts:
                startActivity(new Intent(this, Contacts.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.off_home_cardview_title:
                Intent i2 = new Intent(this, Transport.class);
                i2.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i2);
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            default:
                Toast.makeText(getBaseContext(), "No item found", Toast.LENGTH_LONG).show();
                break;
        }

    }

    public void changeTransport() {
        TransportDynamics dynamics = transport.getDynamics();
        title.setText(getString(R.string.home_trasnport_title, transport.getOrigin().toUpperCase(), transport.getDestination().toUpperCase()));
        nextText.setText(getResources().getString(R.string.next_transport,
                transport.getType(), new DateConverters().convertFormat(dynamics.getNextTripString(), DateFormats.TIME_12_HOURS_STANDARD)));


        int[] Difference = new int[]{dynamics.getHoursToNextTrip(), dynamics.getMinsToNextTrip(), dynamics.getSecsToNextTrip()};

        hourText.setText(getString(R.string.transport_hours, Difference[0]));
        minText.setText(getString(R.string.transport_min, Difference[1]));
        secText.setText(getString(R.string.transport_sec, Difference[2]));
        if (Difference[0] > 12) {
            Difference[1] = 12;
        }
        params1.weight = ((float) Difference[0] / 12) * 100;
        hour.setLayoutParams(params1);
        params2.weight = ((float) Difference[1] / 60) * 100;
        min.setLayoutParams(params2);
        params3.weight = ((float) Difference[2] / 60) * 100;
        sec.setLayoutParams(params3);

        if (dynamics.getTotalNumberOfMins() <= pref.user().getHurryUpTime()) {
            changeLayoutColor(false);
        } else {
            changeLayoutColor(true);
        }

    }

    /**
     * Randomize colors on time bars
     */
    public void changeColor() {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        hour.setBackgroundColor(color);
        min.setBackgroundColor(color);
        sec.setBackgroundColor(color);
    }

    private void changeLayoutColor(boolean isRegular) {
        if (isRegular) {
            footerHolder.setBackgroundColor(General.getColor(getBaseContext(), R.color.colorPrimary));
            homeFooter.setBackgroundColor(General.getColor(getBaseContext(), R.color.colorPrimary));
            toolbar.setBackgroundColor(General.getColor(getBaseContext(), R.color.colorPrimary));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(General.getColor(getBaseContext(), R.color.colorPrimaryDark));
            }
        } else {
            footerHolder.setBackgroundColor(General.getColor(getBaseContext(), R.color.hurryup_color));
            homeFooter.setBackgroundColor(General.getColor(getBaseContext(), R.color.hurryup_color));
            toolbar.setBackgroundColor(General.getColor(getBaseContext(), R.color.hurryup_color));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(General.getColor(getBaseContext(), R.color.hurryup_color_dark));
            }
        }
    }

}

