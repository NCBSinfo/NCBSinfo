package com.rohitsuratekar.NCBSinfo.maplist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;

public class MapLocationViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
    public TextView title;
    public TextView description;

    protected GoogleMap mGoogleMap;
    protected MapLocation mMapLocation;

    public MapView mapView;
    private Context mContext;

    public MapLocationViewHolder(Context context, View view) {
        super(view);

        mContext = context;

        title = (TextView) view.findViewById(R.id.title);
        description = (TextView) view.findViewById(R.id.description);
        mapView = (MapView) view.findViewById(R.id.mapview);

        mapView.onCreate(null);
        mapView.getMapAsync(this);
    }

    public void setMapLocation(MapLocation mapLocation) {
        mMapLocation = mapLocation;

        // If the map_activity is ready, update its content.
        if (mGoogleMap != null) {
            updateMapContents();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        MapsInitializer.initialize(mContext);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        // If we have map_activity data, update the map content.
        if (mMapLocation != null) {
            updateMapContents();
        }
    }

    protected void updateMapContents() {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        mGoogleMap.clear();

        // Update the mapView feature data and camera position.
        mGoogleMap.addMarker(new MarkerOptions().position(mMapLocation.center));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mMapLocation.center, 10f);
        mGoogleMap.moveCamera(cameraUpdate);
        mGoogleMap.animateCamera( CameraUpdateFactory.zoomTo( 14.0f ) ); //Zoom level
        //PolylineOptions polOption = new PolylineOptions().add(new LatLng(13.071313,77.581256),
         //               new LatLng(13.016163,77.566992))
          //      .width(5).color(Color.RED);


    }
}
