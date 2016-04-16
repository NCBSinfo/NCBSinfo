package com.rohitsuratekar.NCBSinfo.maplist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

public class MapListActivityImpl extends MapListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected MapLocationAdapter createMapListAdapter() {
        ArrayList<MapLocation> mapLocations = new ArrayList<>(LIST_LOCATIONS.length);
        mapLocations.addAll(Arrays.asList(LIST_LOCATIONS));
        MapLocationAdapter adapter = new MapLocationAdapter();
        adapter.setMapLocations(mapLocations);

        return adapter;
    }

    @Override
    public void showMapDetails(View view) {
        MapLocation mapLocation = (MapLocation) view.getTag();

        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.EXTRA_LATITUDE, mapLocation.center.latitude);
        intent.putExtra(MapActivity.EXTRA_LONGITUDE, mapLocation.center.longitude);

        startActivity(intent);
    }

    private static final MapLocation[] LIST_LOCATIONS = new MapLocation[]{
            new MapLocation("0", 13.071313, 77.581256),
            new MapLocation("1", 13.016163, 77.566992),
            new MapLocation("2", 13.093389, 77.578188),
            new MapLocation("3", 13.146300, 77.514059)
    };
}
