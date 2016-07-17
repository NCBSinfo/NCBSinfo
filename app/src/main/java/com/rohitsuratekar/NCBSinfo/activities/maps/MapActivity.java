package com.rohitsuratekar.NCBSinfo.activities.maps;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Public
    public static final String ANDROID_MAP_WARNING = "pref_maps_firstTime";

    public static final String EXTRA_LATITUDE = "lat";
    public static final String EXTRA_LONGITUDE = "lng";
    public static final String EXTRA_ROUTE = "route";

    GoogleMap currentmap;
    int currentRoute;
    LatLng coords;
    BaseParameters baseParameters;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maps);
        currentRoute = getIntent().getIntExtra(EXTRA_ROUTE, 0);
        baseParameters = new BaseParameters(getBaseContext());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_call);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + "080-2366-6001"));
                startActivity(intent);
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(ANDROID_MAP_WARNING, true)) {

            final AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.warning_map_title));
            alertDialog.setMessage(getResources().getString(R.string.warning_map_details));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "I agree", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceManager.getDefaultSharedPreferences(getBaseContext()).edit().putBoolean(ANDROID_MAP_WARNING, false).apply();
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
            alertDialog.setCanceledOnTouchOutside(false);
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
                    overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                    overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
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
        googleMap.clear();

        coords = new LatLng(lat, lng);
        // googleMap.addMarker(new MarkerOptions().position(coords));
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 10f);
        googleMap.moveCamera(cameraUpdate);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f)); //Zoom level

        if (currentRoute == Routes.NCBS_CBL.getRouteNo()) {
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
        putStops(currentRoute);
    }

    private void putStops(int route) {

        if (route == Routes.NCBS_IISC.getRouteNo()) { //NCBS-IISc
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
        } else if (route == Routes.IISC_NCBS.getRouteNo()) { //IISc-NCBS
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.014730, 77.581150))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("RRI")).showInfoWindow(); //RRI
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.016642, 77.583859))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Indian Air Force")).showInfoWindow(); //Indian Air Force
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.038025, 77.589211))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Hebbal")).showInfoWindow(); //Hebbal
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.049927, 77.592736))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Esteem Mall")).showInfoWindow(); //Esteem Mall
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.062567, 77.593080))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Big Market")).showInfoWindow(); //Big Market
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.065568, 77.593080))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Byatarayanapura")).showInfoWindow(); //Byatarayanapura
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.072615, 77.592443))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("GKVK Gate")).showInfoWindow(); //GKVK Gate

        } else if (route == Routes.NCBS_MANDARA.getRouteNo()) { //NCBS-Mandara
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.070403, 77.578944))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("NCBS Back Gate")).showInfoWindow(); //NCBS Back Gate
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.070484, 77.578144))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Double Road 1")).showInfoWindow(); //Double Road 1
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.092300, 77.559512))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Betahalli")).showInfoWindow(); //Betahalli
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.098118, 77.569677))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Purva Venezia")).showInfoWindow(); //Purva Venezia
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.099277, 77.577166))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Dairy Circle")).showInfoWindow(); //Dairy Circle
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.095423, 77.579421))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Yelahanka New Town Bus Station")).showInfoWindow(); //Yelahanka New Town Bus Station

        } else if (route == Routes.MANDARA_NCBS.getRouteNo()) { //Mandara-NCBS

            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.095423, 77.579421))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Yelahanka New Town Bus Station")).showInfoWindow(); //Yelahanka New Town Bus Station
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.099282, 77.577098))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Dairy Circle")).showInfoWindow(); //Dairy Circle
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.098323, 77.572076))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("NCC Urban Aster")).showInfoWindow(); //NCC Urban Aster
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.097986, 77.570094))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Purva Venezia")).showInfoWindow(); //Purva Venezia
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.092074, 77.559663))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Betahalli")).showInfoWindow(); //Betahalli
            currentmap.addMarker(new MarkerOptions().position(new LatLng(13.070403, 77.578944))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("NCBS Back Gate")).showInfoWindow(); //NCBS Back Gate

        } else if (route == Routes.BUGGY_FROM_NCBS.getRouteNo()) { //Buggy from NCBS
            //Nothing is here
        } else if (route == Routes.BUGGY_FROM_MANDARA.getRouteNo()) { //Buggy from Mandara
            //Nothing is here
        } else if (route == Routes.NCBS_CBL.getRouteNo()) { //CBL
            final AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.warning_cbl_title));
            alertDialog.setMessage(getResources().getString(R.string.warning_cbl_details));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        } else {
            final AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
            alertDialog.setTitle(getResources().getString(R.string.warning_noroute_title));
            alertDialog.setMessage(getResources().getString(R.string.warning_noroute_details));
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }

        if (route != Routes.NCBS_CBL.getRouteNo()) {
            currentmap.addMarker(new MarkerOptions().position(coords)
                    .title("Destination")).showInfoWindow();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
    }
}
