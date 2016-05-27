package com.rohitsuratekar.NCBSinfo.maplist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.Registration;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_LATITUDE = "lat";
    public static final String EXTRA_LONGITUDE = "lng";
    public static final String EXTRA_ROUTE = "route";

    GoogleMap currentmap;
    int currentRoute;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);
        currentRoute = getIntent().getIntExtra(EXTRA_ROUTE, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.ANDROID_MAP_WARNING,true)){

            final AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.warning_map_title));
            alertDialog.setMessage(getResources().getString(R.string.warning_map_details));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "I agree", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(Preferences.ANDROID_MAP_WARNING, false).apply();
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Go back", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                    startActivity(new Intent(MapActivity.this, Home.class));
                }
            });
            alertDialog.show();

        }


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);

                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }

                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        double lat = getIntent().getDoubleExtra(EXTRA_LATITUDE, 0);
        double lng = getIntent().getDoubleExtra(EXTRA_LONGITUDE, 0);

        currentmap = googleMap;
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));

        LatLng coords = new LatLng(lat, lng);
        googleMap.addMarker(new MarkerOptions().position(coords));
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 10f);
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
        putStops(currentRoute);
     }

    private void putStops(int route){

        if (route==0){ //NCBS-IISc
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.072639, 77.592433))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("GKVK Gate")).showInfoWindow(); //GKVK Gate
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.057432, 77.593462))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Kodigehalli Gate")).showInfoWindow(); //Kodigehalli Gate
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.049441, 77.592805))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Esteem Mall")).showInfoWindow(); //Esteem Mall
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.041138, 77.589856))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Hebbal")).showInfoWindow(); //Hebbal
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.025598, 77.585348))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("CBI")).showInfoWindow(); //CBI
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.020056, 77.584026))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("HMT Bhavan")).showInfoWindow(); //HMT Bhavan
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.016179, 77.584182))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Mekhri Circle")).showInfoWindow(); //Mekhri Circle
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.014575, 77.581134))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("RRI")).showInfoWindow(); //RRI
        }

        if (route==1){ //NCBS-IISc
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.072639, 77.592433))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("GKVK Gate")).showInfoWindow(); //GKVK Gate
        }

    }




}
