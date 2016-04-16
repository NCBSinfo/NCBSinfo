package com.rohitsuratekar.NCBSinfo.maplist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapView;
import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.HashSet;

public class MapLocationAdapter extends RecyclerView.Adapter<MapLocationViewHolder> {
    protected HashSet<MapView> mMapViews = new HashSet<>();
    protected ArrayList<MapLocation> mMapLocations;

    public void setMapLocations(ArrayList<MapLocation> mapLocations) {
        mMapLocations = mapLocations;
    }

    @Override
    public MapLocationViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.map_list_item, viewGroup, false);
        MapLocationViewHolder viewHolder = new MapLocationViewHolder(viewGroup.getContext(), view);

        mMapViews.add(viewHolder.mapView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MapLocationViewHolder viewHolder, int position) {
        MapLocation mapLocation = mMapLocations.get(position);

        viewHolder.itemView.setTag(mapLocation);

        viewHolder.title.setText(mapLocation.name);
        viewHolder.description.setText(mapLocation.center.latitude + " " + mapLocation.center.longitude);

        viewHolder.setMapLocation(mapLocation);
    }

    @Override
    public int getItemCount() {
        return mMapLocations == null ? 0 : mMapLocations.size();
    }

    public HashSet<MapView> getMapViews() {
        return mMapViews;
    }
}
