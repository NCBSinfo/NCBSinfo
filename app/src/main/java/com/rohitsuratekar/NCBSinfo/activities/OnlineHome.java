package com.rohitsuratekar.NCBSinfo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.activities.events.Events;
import com.rohitsuratekar.NCBSinfo.activities.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.activities.maps.MapActivity;
import com.rohitsuratekar.NCBSinfo.activities.maps.MapHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteBuilder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportDynamics;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportRoute;
import com.rohitsuratekar.NCBSinfo.background.DataManagement;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineHome extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.ONLINE_HOME;
    }


    //Set up UI
    @BindView(R.id.home_layout)
    RelativeLayout homeLayout;
    @BindView(R.id.home_footerHolder)
    LinearLayout footerHolder;
    @BindView(R.id.home_footer)
    LinearLayout homeFooter;
    @BindView(R.id.home_title_holder)
    LinearLayout titleHolder;

    @BindView(R.id.home_cardview_title)
    TextView title;
    @BindView(R.id.home_timeleft_text)
    TextView timeLeft;
    @BindView(R.id.home_cardView_nextText)
    TextView nextText;
    @BindView(R.id.home_footerText)
    TextView footerText;

    @BindView(R.id.home_cardView_selector)
    ImageView image;
    @BindView(R.id.previousRoute)
    ImageView previous;
    @BindView(R.id.nextRoute)
    ImageView next;
    @BindView(R.id.home_icon_transport)
    ImageView icon_transport;
    @BindView(R.id.home_icon_updates)
    ImageView icon_updates;
    @BindView(R.id.home_icon_experimental)
    ImageView icon_experimental;
    @BindView(R.id.home_icon_contacts)
    ImageView icon_contacts;

    //Variables
    Preferences pref;
    TransportRoute transport;
    GoogleMap googleMap;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //Initialization
        pref = new Preferences(getBaseContext());
        mAuth = FirebaseAuth.getInstance();
        transport = new RouteBuilder(pref.user().getDefaultRoute(), getBaseContext()).build();

        //Initialize Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.home_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        //Set up toolbar title
        setTitle("");
        TextView specialTitle = (TextView) toolbar.findViewById(R.id.special_tool_bar_title);
        specialTitle.setText(getString(R.string.app_name));
        specialTitle.setVisibility(View.VISIBLE);

        //Initialize authentication
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Log.i(TAG, "Logged in user " + firebaseAuth.getCurrentUser().getEmail());
                    //Second if condition is required for database migration
                    if (!pref.user().getUserType().equals(AppConstants.userType.REGULAR_USER) || !pref.network().isOldDeleted()) {
                        Intent intent = new Intent(OnlineHome.this, DataManagement.class);
                        intent.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATA);
                        startService(intent);
                    }
                } else {
                    Log.i(TAG, "No credentials found");
                }
            }
        };

        //Add listener
        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        icon_updates.setOnClickListener(this);
        icon_transport.setOnClickListener(this);
        icon_experimental.setOnClickListener(this);
        icon_contacts.setOnClickListener(this);
        title.setOnClickListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(OnlineHome.this, v);
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


        changeTransport();
        changeTransport();
        runnable.run();
        footerText.setText(setFooterText());
        footerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPlaySTore();
            }
        });

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
    public void onStart() {
        super.onStart();
        if (mAuthListener != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_LATITUDE, transport.getDestinationLocation().latitude);
        intent.putExtra(MapActivity.EXTRA_LONGITUDE, transport.getDestinationLocation().longitude);
        intent.putExtra(MapActivity.EXTRA_ROUTE, transport.getRouteNo());
        startActivity(intent);
        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        MapsInitializer.initialize(getBaseContext());
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (googleMap != null) {
            googleMap.setOnMapClickListener(this);
            new MapHelper(getBaseContext()).updateMapContents(googleMap, transport);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.previousRoute:
                transport = new RouteBuilder(new TransportHelper().changeRoute(transport.getRoute(), false), getBaseContext()).build();
                changeTransport();
                new MapHelper(getBaseContext()).updateMapContents(googleMap, transport);
                break;
            case R.id.nextRoute:
                transport = new RouteBuilder(new TransportHelper().changeRoute(transport.getRoute(), true), getBaseContext()).build();
                changeTransport();
                new MapHelper(getBaseContext()).updateMapContents(googleMap, transport);
                break;
            case R.id.home_icon_experimental:
                startActivity(new Intent(this, Experimental.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.home_icon_updates:
                startActivity(new Intent(this, Events.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.home_icon_transport:
                Intent i1 = new Intent(this, Transport.class);
                i1.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i1);
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.home_icon_contacts:
                startActivity(new Intent(this, Contacts.class));
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                break;
            case R.id.home_cardview_title:
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
        timeLeft.setText(getResources().getString(R.string.time_left, dynamics.getHoursToNextTrip(), dynamics.getMinsToNextTrip(), dynamics.getSecsToNextTrip()));
        if (dynamics.getTotalNumberOfMins() <= pref.user().getHurryUpTime()) {
            changeColor(false);
        } else {
            changeColor(true);
        }
    }

    private String setFooterText() {
        if (pref.network().isLatestApp()) {
            return "";
        } else {
            return getString(R.string.update_notice);
        }

    }

    private void gotoPlaySTore() {

        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        String url = "";
        try {
            //Check whether Google Play store is installed or not:
            getPackageManager().getPackageInfo("com.android.vending", 0);
            url = "market://details?id=" + appPackageName;
        } catch (final Exception e) {
            url = "https://play.google.com/store/apps/details?id=" + appPackageName;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void changeColor(boolean isRegular) {
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



