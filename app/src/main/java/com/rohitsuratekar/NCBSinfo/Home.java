package com.rohitsuratekar.NCBSinfo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
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
import com.rohitsuratekar.NCBSinfo.activity.Contacts;
import com.rohitsuratekar.NCBSinfo.activity.EventUpdates;
import com.rohitsuratekar.NCBSinfo.activity.Experimental;
import com.rohitsuratekar.NCBSinfo.activity.LectureHalls;
import com.rohitsuratekar.NCBSinfo.activity.Registration;
import com.rohitsuratekar.NCBSinfo.activity.Transport;
import com.rohitsuratekar.NCBSinfo.background.DataFetch;
import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.constants.SettingsRelated;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.ConferenceData;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.ExternalData;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.helpers.TransportFunctions;
import com.rohitsuratekar.NCBSinfo.maplist.MapActivity;
import com.rohitsuratekar.NCBSinfo.models.ConferenceModel;
import com.rohitsuratekar.NCBSinfo.models.ExternalModel;
import com.rohitsuratekar.NCBSinfo.tempActivitites.CAMP;
import com.rohitsuratekar.NCBSinfo.tempActivitites.ExternalRegistrations;
import com.rohitsuratekar.retro.google.fusiontable.Commands;
import com.rohitsuratekar.retro.google.fusiontable.Service;
import com.rohitsuratekar.retro.google.fusiontable.reponse.FusionTableRow;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {
    GoogleMap googleMap;
    TextView title,footer,nextText;
    ImageView image,previous,next;
    ImageView icon_transport, icon_updates, icon_experimental, icon_contacts;
    LatLng coord;
    String transportFrom, transportTo;
    int isBuggy, currentRoute;
    LinearLayout footerHolder, homeFooter, titleHolder;
    RelativeLayout homeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

       //Give warning to users if Android version is lower than 5.0
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.ANDROID_VERSION_WARNING, true)) {

            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_version_title));
                alertDialog.setMessage(getResources().getString(R.string.warning_version_details,android.os.Build.VERSION.SDK_INT ));
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
       homeLayout = (RelativeLayout)findViewById(R.id.home_layout);
       coord = new TransportFunctions().getLocation(getApplicationContext(),transportTo,isBuggy);
       footerHolder = (LinearLayout)findViewById(R.id.home_footerHolder);
       homeFooter = (LinearLayout)findViewById(R.id.home_footer);
       titleHolder = (LinearLayout)findViewById(R.id.home_title_holder);
       title = (TextView)findViewById(R.id.home_cardview_title);
       footer = (TextView)findViewById(R.id.home_cardView_Footer);
       nextText = (TextView)findViewById(R.id.home_cardView_nextText);
       image= (ImageView)findViewById(R.id.home_cardView_selector);
       previous= (ImageView)findViewById(R.id.previousRoute);
       next= (ImageView)findViewById(R.id.nextRoute);
       icon_transport= (ImageView)findViewById(R.id.home_icon_transport);
       icon_updates= (ImageView)findViewById(R.id.home_icon_updates);
       icon_experimental= (ImageView)findViewById(R.id.home_icon_experimental);
        icon_contacts= (ImageView)findViewById(R.id.home_icon_contacts);
       MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.home_map);
        mapFragment.getMapAsync(this);
        mapFragment.getMap().setOnMapClickListener(this);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(Home.this,v);
                popupMenu.inflate(R.menu.popup_card);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getItemId() == R.id.popup_addfav){
                            String[] route = new TransportFunctions().getRouteName(currentRoute);
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(SettingsRelated.HOME_DEFAULT_ROUTE,currentRoute).apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(SettingsRelated.SETTINGS_TRANSPORT_ROUTES, String.valueOf(currentRoute)).apply();
                            Snackbar.make(homeLayout, getResources().getString(R.string.snackbar_added_route,route[0].toUpperCase(),route[1].toUpperCase()),Snackbar.LENGTH_SHORT).show();
                        }
                        else if (item.getItemId() == R.id.popup_remove){
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putInt(SettingsRelated.HOME_DEFAULT_ROUTE,0).apply();
                            PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putString(SettingsRelated.SETTINGS_TRANSPORT_ROUTES,"0").apply();
                            Snackbar.make(homeLayout, getResources().getString(R.string.snackbar_reset_route),Snackbar.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        previous.setOnClickListener(this);
        next.setOnClickListener(this);
        icon_updates.setOnClickListener(this);
        icon_transport.setOnClickListener(this);
        icon_experimental.setOnClickListener(this);
        icon_contacts.setOnClickListener(this);
        title.setOnClickListener(this);

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
    protected void onResume() {
        super.onResume();
        currentRoute = PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getInt(SettingsRelated.HOME_DEFAULT_ROUTE,0);
        transportFrom = new TransportFunctions().getRouteName(currentRoute)[0];
        transportTo = new TransportFunctions().getRouteName(currentRoute)[1];
        isBuggy = Integer.valueOf(new TransportFunctions().getRouteName(currentRoute)[2]);
        changeTransportText();
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
            tempText=getResources().getString(R.string.next_transport,getResources().getString(R.string.buggy));
        }
        else {
            tempText=getResources().getString(R.string.next_transport,getResources().getString(R.string.shuttle));
        }
        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Calendar nextDate = new TransportFunctions().NextTransport(transportFrom, transportTo, format.format(c2.getTime()), isBuggy);
        SimpleDateFormat dformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        nextText.setText(getResources().getString(R.string.next_transport_details,tempText, dformat.format(nextDate.getTime())));

        float[] Difference = new TransportFunctions().TimeLeft(format.format(c2.getTime()), format.format(nextDate.getTime()));

        footer.setText(getResources().getString(R.string.time_left, (int) Difference[2] ,((int) Difference[1]) , ((int) Difference[0])));

        title.setText(getResources().getStringArray(R.array.home_spinner_items)[currentRoute]);
        float minLeft = Difference[2]*60 + Difference[1];

        Window window = getWindow();

        if(minLeft < PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getFloat(SettingsRelated.SETTING_HURRY_UP_TIME,5)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                footer.setTextColor(getResources().getColor(R.color.hurryupColor,getTheme()));
                footerHolder.setBackgroundColor(getResources().getColor(R.color.hurryupColor,getTheme()));
                homeFooter.setBackgroundColor(getResources().getColor(R.color.hurryupColor,getTheme()));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.hurryupColor,getTheme())));
                window.setStatusBarColor(getResources().getColor(R.color.hurryup_dark,getTheme()));


            } else {
                footer.setTextColor(getResources().getColor(R.color.hurryupColor));
                footerHolder.setBackgroundColor(getResources().getColor(R.color.hurryupColor));
                homeFooter.setBackgroundColor(getResources().getColor(R.color.hurryupColor));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.hurryupColor)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.hurryup_dark));
                }
            }
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                footer.setTextColor(getResources().getColor(R.color.normalColor,getTheme()));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary,getTheme())));
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark,getTheme()));

            } else {
                footer.setTextColor(getResources().getColor(R.color.normalColor));
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
                }

            }

            //Check Buggy color
            if (isBuggy==1){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    footerHolder.setBackgroundColor(getResources().getColor(R.color.BuggyColor,getTheme()));
                    homeFooter.setBackgroundColor(getResources().getColor(R.color.BuggyColor,getTheme()));
                } else {
                    footerHolder.setBackgroundColor(getResources().getColor(R.color.BuggyColor));
                    homeFooter.setBackgroundColor(getResources().getColor(R.color.BuggyColor));
                }
            }
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    footerHolder.setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
                    homeFooter.setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
                } else {
                    footerHolder.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    homeFooter.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                }
            }

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
            case R.id.home_icon_experimental:
                startActivity(new Intent(this, Experimental.class)); break;
            case R.id.home_icon_updates:
                startActivity(new Intent(this,EventUpdates.class)); break;
            case R.id.home_icon_transport:
                Intent i1 = new Intent(this, Transport.class);
                i1.putExtra(General.GEN_TRANSPORT_INTENT,String.valueOf(currentRoute));
                startActivity(i1);break;
            case R.id.home_icon_contacts:
                startActivity(new Intent(this, Contacts.class));break;
            case R.id.home_cardview_title:
                Intent i2 = new Intent(this, Transport.class);
                i2.putExtra(General.GEN_TRANSPORT_INTENT,String.valueOf(currentRoute));
                startActivity(i2);break;
            default:
                Toast.makeText(getBaseContext(),"No item found",Toast.LENGTH_LONG).show();break;
        }
    }
}
