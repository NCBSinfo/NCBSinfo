package com.rohitsuratekar.NCBSinfo.activities.maps;

import android.content.Context;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Routes;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportRoute;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 17-07-16.
 */
public class MapHelper {

    Context context;

    public MapHelper(Context context) {
        this.context = context;
    }

    public void updateMapContents(GoogleMap googleMap, TransportRoute transport) {
        // Since the mapView is re-used, need to remove pre-existing mapView features.
        if (googleMap != null) {
            googleMap.clear();
        }
        // Update the mapView feature data and camera position.
        googleMap.addMarker(new MarkerOptions()
                .position(transport.getDestinationLocation()));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(transport.getDestinationLocation(), 10f);
        googleMap.moveCamera(cameraUpdate);
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f)); //Zoom level

        if (transport.getRouteNo() == Routes.NCBS_CBL.getRouteNo()) {
            PolygonOptions rectOptions = new PolygonOptions();
            String[] stringarray = context.getResources().getStringArray(R.array.canara_bank_layout);
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
}
