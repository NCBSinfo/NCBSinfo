package com.rohitsuratekar.NCBSinfo.online;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.common.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.online.events.Events;
import com.rohitsuratekar.NCBSinfo.online.experimental.Experimental;
import com.rohitsuratekar.NCBSinfo.online.maps.MapActivity;

import java.util.Timer;
import java.util.TimerTask;

public class OnlineHome extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {

    GoogleMap googleMap;
    TextView title, timeLeft, nextText;
    ImageView image, previous, next;
    ImageView icon_transport, icon_updates, icon_experimental, icon_contacts;
    LinearLayout footerHolder, homeFooter, titleHolder;
    RelativeLayout homeLayout;
    TransportModel transport;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_home);

        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        //Set up transport
        transport = new TransportHelper().getTransport(getBaseContext(), pref.getInt(Transport.DEFAULT_ROUTE, TransportConstants.ROUTE_NCBS_IISC));

        //Initialize Map
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        mapFragment.getMap().setOnMapClickListener(this);

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
                transport = new TransportHelper().getTransport(getBaseContext(),
                        changeRoute(transport.getRouteNo(), false));
                changeTransport();
                updateMapContents(transport.getDestinationLocation());
                break;
            case R.id.nextRoute:
                transport = new TransportHelper().getTransport(getBaseContext(),
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
                transport.getType(), new TransportHelper().convertToSimpleDate(transport.getNextTrip())));
        float[] Difference = transport.getTimeLeft();
        timeLeft.setText(getResources().getString(R.string.time_left, (int) Difference[2], ((int) Difference[1]), ((int) Difference[0])));

    }
}
