package com.rohitsuratekar.NCBSinfo.online;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.Alarms;
import com.rohitsuratekar.NCBSinfo.background.DataManagement;
import com.rohitsuratekar.NCBSinfo.background.NetworkOperations;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.common.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.common.utilities.AutoConfiguration;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.interfaces.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.interfaces.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.online.constants.RemoteConstants;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.rohitsuratekar.NCBSinfo.online.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.online.maps.MapActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OnlineHome extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        View.OnClickListener, UserInformation, NetworkConstants, AlarmConstants {

    private final String TAG = this.getClass().getSimpleName();

    public static final String MODE_CONSTANT = "onlineHome";

    GoogleMap googleMap;
    TextView title, timeLeft, nextText;
    ImageView image, previous, next;
    ImageView icon_transport, icon_updates, icon_experimental, icon_contacts;
    LinearLayout footerHolder, homeFooter, titleHolder;
    RelativeLayout homeLayout;
    TransportModel transport;
    SharedPreferences pref;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_home);

        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //Set up transport
        transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(), pref.getInt(Transport.DEFAULT_ROUTE, TransportConstants.ROUTE_NCBS_IISC));

        Intent i = new Intent(getBaseContext(), Alarms.class);
        i.putExtra(Alarms.INTENT, RESET_ALL);
        getBaseContext().sendBroadcast(i);

        //Set up remote configuration and firebase
        mAuth = FirebaseAuth.getInstance();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.i(TAG, "Login state changed to ");
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent service = new Intent(getBaseContext(), DataManagement.class);
                    service.putExtra(DataManagement.INTENT, DataManagement.SEND_FIREBASEDATE);
                    getBaseContext().startService(service);
                }
            }
        };

        //TODO: when database is up running, remove authentication with just email address
        //Notify CAMP users
        if ((pref.getBoolean(registration.camp16.IS_CAMP_USER, false) || isCampUser()) && pref.getBoolean(firstTime.CAMP_NOTICE, true)) {
            final AlertDialog alertDialog = new AlertDialog.Builder(OnlineHome.this).create();
            alertDialog.setTitle("CAMP 2016");
            alertDialog.setMessage("We have detected that you are CAMP 2016 user, you want to change mode to CAMP?");
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Sure", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    pref.edit().putBoolean(firstTime.CAMP_NOTICE, false).apply();
                    pref.edit().putString(MODE, registration.camp16.CAMP_MODE).apply();
                    Intent i = new Intent(OnlineHome.this, Home.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Not now", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    pref.edit().putBoolean(firstTime.CAMP_NOTICE, false).apply();
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        //Initialize Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.home_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
            mapFragment.getMap().setOnMapClickListener(this);
        }

        //Find UI Elements
        homeLayout = (RelativeLayout) findViewById(R.id.home_layout);
        footerHolder = (LinearLayout) findViewById(R.id.home_footerHolder);
        homeFooter = (LinearLayout) findViewById(R.id.off_home_footer);
        titleHolder = (LinearLayout) findViewById(R.id.home_title_holder);
        title = (TextView) findViewById(R.id.home_cardview_title);
        timeLeft = (TextView) findViewById(R.id.home_timeleft_text);
        nextText = (TextView) findViewById(R.id.home_cardView_nextText);
        image = (ImageView) findViewById(R.id.home_cardView_selector);
        previous = (ImageView) findViewById(R.id.previousRoute);
        next = (ImageView) findViewById(R.id.nextRoute);
        icon_transport = (ImageView) findViewById(R.id.home_icon_transport);
        icon_updates = (ImageView) findViewById(R.id.home_icon_updates);
        icon_experimental = (ImageView) findViewById(R.id.home_icon_experimental);
        icon_contacts = (ImageView) findViewById(R.id.home_icon_contacts);

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

        //Get configuration
        getConfiguration();

        //Submit registration details for new user

        if (!pref.getBoolean(netwrok.REGISTRATION_DETAILS_SENT, false)) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            FirebaseMessaging.getInstance().subscribeToTopic(topics.PUBLIC);
            FirebaseMessaging.getInstance().subscribeToTopic(topics.EMERGENCY);
            pref.edit().putString(registration.FIREBASE_TOKEN, refreshedToken).apply();
            Intent service = new Intent(getBaseContext(), NetworkOperations.class);
            service.putExtra(NetworkOperations.INTENT, NetworkOperations.REGISTER);
            startService(service);
            Log.d(TAG, "Subscribed with topic");
        }


        if (pref.getBoolean(firstTime.FIRST_NOTIFICATION_DASHBOARD, true)) {
            new AutoConfiguration(getBaseContext()).nameNotifications();
        }
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

    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;
        MapsInitializer.initialize(getBaseContext());
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (googleMap != null) {
            googleMap.setOnMapClickListener(this);
            updateMapContents(transport.getDestinationLocation());
        }

    }

    protected void updateMapContents(LatLng coord) {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        if (googleMap != null) {
            googleMap.clear();
        }
        // Update the mapView feature data and camera position.
        googleMap.addMarker(new MarkerOptions()
                .position(coord));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coord, 10f);
        googleMap.moveCamera(cameraUpdate);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f)); //Zoom level

        if (transport.getRouteNo() == TransportConstants.ROUTE_NCBS_CBL) {
            PolygonOptions rectOptions = new PolygonOptions();
            String[] stringarray = getResources().getStringArray(R.array.canara_bank_layout);
            for (String point : stringarray) {
                String cLat = point.split(",")[0];
                String cLong = point.split(",")[1];
                rectOptions.add(new LatLng(Double.parseDouble(cLat), Double.parseDouble(cLong)));
            }
            rectOptions.strokeWidth((float) 1);
            rectOptions.fillColor(R.color.map_polygone_overlay);
            googleMap.addPolygon(rectOptions);
        }

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
            case R.id.previousRoute:
                transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(),
                        changeRoute(transport.getRouteNo(), false));
                changeTransport();
                updateMapContents(transport.getDestinationLocation());
                break;
            case R.id.nextRoute:
                transport = new TransportHelper(getBaseContext()).getTransport(getBaseContext(),
                        changeRoute(transport.getRouteNo(), true));
                changeTransport();
                updateMapContents(transport.getDestinationLocation());
                break;
            case R.id.home_icon_experimental:
                startActivity(new Intent(this, Experimental.class));
                break;
            case R.id.home_icon_updates:
                startActivity(new Intent(this, Events.class));
                break;
            case R.id.home_icon_transport:
                Intent i1 = new Intent(this, Transport.class);
                i1.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i1);
                break;
            case R.id.home_icon_contacts:
                startActivity(new Intent(this, Contacts.class));
                break;
            case R.id.home_cardview_title:
                Intent i2 = new Intent(this, Transport.class);
                i2.putExtra(Transport.INDENT, String.valueOf(transport.getRouteNo()));
                startActivity(i2);
                break;
            default:
                Toast.makeText(getBaseContext(), "No item found", Toast.LENGTH_LONG).show();
                break;
        }

    }

    public int changeRoute(int currentRoute, boolean isNext) {
        int route = currentRoute;
        if (isNext) {
            route = route + 1;
            if (route > TransportConstants.ROUTE_NCBS_CBL) {
                route = TransportConstants.ROUTE_NCBS_IISC;
            }
        } else {
            route = route - 1;
            if (route < TransportConstants.ROUTE_NCBS_IISC) {
                route = TransportConstants.ROUTE_NCBS_CBL;
            }
        }
        return route;
    }

    public void changeTransport() {
        title.setText(getString(R.string.home_trasnport_title, transport.getFrom().toUpperCase(), transport.getTo().toUpperCase()));
        nextText.setText(getResources().getString(R.string.next_transport,
                transport.getType(), new TransportHelper(getBaseContext()).convertToSimpleDate(transport.getNextTrip())));
        float[] Difference = transport.getTimeLeft();
        timeLeft.setText(getResources().getString(R.string.time_left, (int) Difference[2], ((int) Difference[1]), ((int) Difference[0])));

    }

    private void getConfiguration() {

        long cacheExpiration = RemoteConstants.CACHE_EXPIRATION;

        // If in developer mode cacheExpiration is set to 0 so each fetch will retrieve values from
        // the server.
        if (mFirebaseRemoteConfig != null) {
            if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
                cacheExpiration = 0;
            }
            if (new Utilities().isOnline(getBaseContext())) {
                mFirebaseRemoteConfig.fetch(cacheExpiration)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "Fetch Succeeded");
                                    // Once the config is successfully fetched it must be activated before newly fetched
                                    // values are returned.
                                    mFirebaseRemoteConfig.activateFetched();
                                    setTransportValue(mFirebaseRemoteConfig, pref);
                                    pref.edit().putBoolean(netwrok.IS_OLD_VERSION, mFirebaseRemoteConfig.getBoolean(netwrok.IS_OLD_VERSION)).apply();
                                    pref.edit().putBoolean(registration.camp16.CAMP_ACCESS, false).apply();
                                    pref.edit().putString(netwrok.LAST_REFRESH_REMOTE_CONFIG, new Utilities().timeStamp()).apply();

                                } else {
                                    Log.d(TAG, "Fetch failed");
                                }
                            }
                        });
            } else {
                Log.e(TAG, "No connection detected!");
            }
        }

    }

    public void setTransportValue(FirebaseRemoteConfig mFirebaseRemoteConfig, SharedPreferences pref) {
        pref.edit().putString(TransportConstants.NCBS_IISC_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_IISC_WEEK)).apply();
        pref.edit().putString(TransportConstants.NCBS_IISC_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_IISC_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.IISC_NCBS_WEEK)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.IISC_NCBS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_MANDARA_WEEK)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_MANDARA_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.MANDARA_NCBS_WEEK)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.MANDARA_NCBS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_ICTS_WEEK)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_ICTS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_WEEK, mFirebaseRemoteConfig.getString(TransportConstants.ICTS_NCBS_WEEK)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_SUNDAY, mFirebaseRemoteConfig.getString(TransportConstants.ICTS_NCBS_SUNDAY)).apply();
        pref.edit().putString(TransportConstants.NCBS_CBL, mFirebaseRemoteConfig.getString(TransportConstants.NCBS_CBL)).apply();
        pref.edit().putString(TransportConstants.BUGGY_NCBS, mFirebaseRemoteConfig.getString(TransportConstants.BUGGY_NCBS)).apply();
        pref.edit().putString(TransportConstants.BUGGY_MANDARA, mFirebaseRemoteConfig.getString(TransportConstants.BUGGY_MANDARA)).apply();

        pref.edit().putString(TransportConstants.CAMP_BUGGY_NCBS, mFirebaseRemoteConfig.getString(TransportConstants.CAMP_BUGGY_NCBS)).apply();
        pref.edit().putString(TransportConstants.CAMP_BUGGY_MANDARA, mFirebaseRemoteConfig.getString(TransportConstants.CAMP_BUGGY_MANDARA)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_MANDARA, mFirebaseRemoteConfig.getString(TransportConstants.CAMP_SHUTTLE_MANDARA)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_NCBS, mFirebaseRemoteConfig.getString(TransportConstants.CAMP_SHUTTLE_NCBS)).apply();
        pref.edit().putString(netwrok.LAST_REFRESH_REMOTE_CONFIG, new Utilities().timeStamp()).apply();
    }

    private boolean isCampUser() {
        return pref.getString(registration.EMAIL, "email@domain.com").split("@")[1].equals(registration.camp16.CAMP_PATTERN);
    }

}
