package com.rohitsuratekar.NCBSinfo.maplist;

import com.google.android.gms.maps.model.LatLng;

public class MapLocation {
    public String name;
    public LatLng center;

    @SuppressWarnings("unused")
    public MapLocation() {}

    public MapLocation(String name, double lat, double lng) {
        this.name = name;
        this.center = new LatLng(lat, lng);
    }
}
