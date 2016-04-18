package com.rohitsuratekar.NCBSinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.rohitsuratekar.NCBSinfo.activity.EventUpdates;
import com.rohitsuratekar.NCBSinfo.activity.Registration;
import com.rohitsuratekar.NCBSinfo.activity.Transport;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.SettingsRelated;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.helpers.TransportFunctions;
import com.rohitsuratekar.NCBSinfo.maplist.MapActivity;
import com.rohitsuratekar.NCBSinfo.maplist.MapListActivityImpl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Home extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {
    GoogleMap googleMap;
    TextView title,footer,nextText;
    ImageView image,previous,next, showall;
    ImageView icon_transport, icon_updates, icon_settings;
    LatLng coord;
    String transportFrom, transportTo;
    int isBuggy, currentRoute;
    LinearLayout footerHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_FIRSTTIME,true)){
            new LogEntry(getBaseContext(), StatusCodes.STATUS_OPENED);
            startActivity(new Intent(this, Registration.class));
        }

       //Give warning to users if Android version is lower than 5.0
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.ANDROID_VERSION_WARNING, true)) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle("Compatibility mode");
                alertDialog.setMessage("This app is best suited for Android Lollipop (21) and above. Your current android version is " + android.os.Build.VERSION.SDK_INT + " . Some animations and functions might not work properly. ");
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                       PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.ANDROID_VERSION_WARNING, false).apply();
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();

            }
        }

        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.home_action_bar);

        //set default values
        currentRoute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SettingsRelated.HOME_DEFAULT_ROUTE,0);
        transportFrom = new TransportFunctions().getRouteName(currentRoute)[0];
        transportTo = new TransportFunctions().getRouteName(currentRoute)[1];
        isBuggy = Integer.valueOf(new TransportFunctions().getRouteName(currentRoute)[2]);

       coord = new TransportFunctions().getLocation(getApplicationContext(),transportTo,isBuggy);
       footerHolder = (LinearLayout)findViewById(R.id.home_footerHolder);
       title = (TextView)findViewById(R.id.home_cardview_title);
       footer = (TextView)findViewById(R.id.home_cardView_Footer);
       nextText = (TextView)findViewById(R.id.home_cardView_nextText);
       image= (ImageView)findViewById(R.id.home_cardView_selector);
       previous= (ImageView)findViewById(R.id.previousRoute);
       next= (ImageView)findViewById(R.id.nextRoute);
       showall= (ImageView)findViewById(R.id.home_icon_showall);
       icon_transport= (ImageView)findViewById(R.id.home_icon_transport);
       icon_updates= (ImageView)findViewById(R.id.home_icon_updates);
       icon_settings= (ImageView)findViewById(R.id.home_icon_settings);
       MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        mapFragment.getMap().setOnMapClickListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Home.this,v);
                popupMenu.inflate(R.menu.popup_card);
                popupMenu.show();
            }
        });

        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        showall.setOnClickListener(this);
        icon_updates.setOnClickListener(this);
        icon_transport.setOnClickListener(this);
        icon_settings.setOnClickListener(this);

        //Timer for timeleft
        Timer timeLeft = new Timer();
        timeLeft.schedule(new TimerTask() {
            @Override
            public void run() {
                runTimer();
            }

        }, 0, 1000); //1000 is milliseconds for each time tick
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap=map;
        MapsInitializer.initialize(getBaseContext());
        googleMap.getUiSettings().setMapToolbarEnabled(false);
        if (googleMap!=null){
            googleMap.setOnMapClickListener(this);
            updateMapContents(coord);
        }

    }

    protected void updateMapContents(LatLng coord) {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        googleMap.clear();
        // Update the mapView feature data and camera position.
        googleMap.addMarker(new MarkerOptions()
                .position(coord));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coord, 10f);
        googleMap.moveCamera(cameraUpdate);
        googleMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) ); //Zoom level

        if(currentRoute==8){
            PolygonOptions rectOptions = new PolygonOptions();
            String[] stringarray = getResources().getStringArray(R.array.canara_bank_layout);
            for (String point : stringarray){
                String cLat = point.split(",")[0];
                String cLong = point.split(",")[1];
                rectOptions.add(new LatLng(Double.parseDouble(cLat),Double.parseDouble(cLong)));
            }
            rectOptions.fillColor(R.color.map_polygone_overlay);
            googleMap.addPolygon(rectOptions);
        }

    }
    private void runTimer() {
        this.runOnUiThread(Timer_Tick);
    }
    private Runnable Timer_Tick = new Runnable() {
        public void run() {
            changeTransportText();
        }
    };

    @Override
    public void onMapClick(LatLng latLng) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_LATITUDE, coord.latitude);
        intent.putExtra(MapActivity.EXTRA_LONGITUDE, coord.longitude);
        intent.putExtra(MapActivity.EXTRA_ROUTE, currentRoute);
        startActivity(intent);
    }


    public void changeTransportText(){
        String tempText;
        if (isBuggy==1){
            tempText="Next Buggy ";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                footerHolder.setBackgroundColor(getResources().getColor(R.color.BuggyColor,getTheme()));
            } else {footerHolder.setBackgroundColor(getResources().getColor(R.color.BuggyColor));}
        }
        else {
            tempText="Next Shuttle ";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                footerHolder.setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
            } else {footerHolder.setBackgroundColor(getResources().getColor(R.color.colorPrimary));}
        }
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new TransportFunctions().NextTransport(transportFrom, transportTo, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        nextText.setText(tempText+dformat.format(nextDate.getTime()));

        float[] Difference = new TransportFunctions().TimeLeft(format.format(c2.getTime()), format.format(nextDate.getTime()));

        footer.setText("" + ((int) Difference[2]) + " Hrs " + ((int) Difference[1]) + " Min " + ((int) Difference[0])+" Sec left");

        String tempString = transportFrom.toUpperCase()+"-"+transportTo.toUpperCase();
        title.setText(tempString);
        float minLeft = Difference[2]*60 + Difference[1];
        if(minLeft<PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getFloat(SettingsRelated.SETTING_HURRY_UP_TIME,5)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                footer.setTextColor(getResources().getColor(R.color.hurryupColor,getTheme()));
            } else {footer.setTextColor(getResources().getColor(R.color.hurryupColor));}
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                footer.setTextColor(getResources().getColor(R.color.normalColor,getTheme()));
            } else {footer.setTextColor(getResources().getColor(R.color.normalColor));}

        }


    }

    public int changeRoute(int currentRoute, boolean isNext){
        int route = currentRoute;
        if(isNext){
            route=route+1;
            if (route>8){route=0;}
        }
        else {
            route=route-1;
            if(route<0){route=8;}
        }
        return route;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.previousRoute:
                currentRoute = changeRoute(currentRoute,false);
                transportFrom = new TransportFunctions().getRouteName(currentRoute)[0];
                transportTo = new TransportFunctions().getRouteName(currentRoute)[1];
                isBuggy = Integer.valueOf(new TransportFunctions().getRouteName(currentRoute)[2]);
                coord = new TransportFunctions().getLocation(getBaseContext(),transportTo,isBuggy);
                updateMapContents(coord);
                changeTransportText();
                break;
            case R.id.nextRoute:
                currentRoute=changeRoute(currentRoute,true);
                transportFrom = new TransportFunctions().getRouteName(currentRoute)[0];
                transportTo = new TransportFunctions().getRouteName(currentRoute)[1];
                isBuggy = Integer.valueOf(new TransportFunctions().getRouteName(currentRoute)[2]);
                coord = new TransportFunctions().getLocation(getBaseContext(),transportTo,isBuggy);
                updateMapContents(coord);
                changeTransportText();
                break;
            case R.id.home_icon_settings:
                startActivity(new Intent(this,Settings.class)); break;
            case R.id.home_icon_updates:
                startActivity(new Intent(this,EventUpdates.class)); break;
            case R.id.home_icon_transport:
                Intent i1 = new Intent(this, Transport.class);
                i1.putExtra(General.GEN_TRANSPORT_INTENT,String.valueOf(currentRoute));
                startActivity(i1);break;
            case R.id.home_icon_showall:
                startActivity(new Intent(this,MapListActivityImpl.class));break;
            default:
                Toast.makeText(getBaseContext(),"No item found",Toast.LENGTH_LONG).show();break;

        }
    }
}
