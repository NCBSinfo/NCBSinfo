package com.rohitsuratekar.NCBSinfo.maplist;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rohitsuratekar.NCBSinfo.R;

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
            rectOptions.fillColor(R.color.map_polygone_overlay);
            googleMap.addPolygon(rectOptions);
        }

     }

    public void stops(LatLng coords){
        currentmap.clear();
        currentmap.addMarker(new MarkerOptions().position(coords));
        currentmap.addMarker(new MarkerOptions().position(coords));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coords, 10f);
        currentmap.moveCamera(cameraUpdate);
        currentmap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) ); //Zoom level

        MapLocation[] locations =
                {new MapLocation("NCBS", 13.071313, 77.581256),
                        new MapLocation("NCBS", 13.075159, 77.582170),
                        new MapLocation("NCBS", 13.075393, 77.581060),
                        new MapLocation("NCBS", 13.077306, 77.582309),
                        new MapLocation("NCBS",13.072568, 77.592709),
                        new MapLocation("NCBS",13.051613, 77.593249),
                        new MapLocation("NCBS",13.042555, 77.590201),
                        new MapLocation("NCBS",13.014533, 77.584192),
                        new MapLocation("NCBS",13.014017, 77.565932),
                        new MapLocation("NCBS",13.016259, 77.566236),
                        new MapLocation("NCBS",13.016217, 77.567132)
                };

        for (int i = 0; i < locations.length - 1; i++) {
            // mMap is the Map Object
            Polyline line = currentmap.addPolyline(
                    new PolylineOptions().add(
                            new LatLng(locations[i].center.latitude, locations[i].center.longitude),
                            new LatLng(locations[i+1].center.latitude,locations[i+1].center.longitude)
                    ).width(5).color(Color.BLUE).geodesic(true)
            );
        }


    }
}
