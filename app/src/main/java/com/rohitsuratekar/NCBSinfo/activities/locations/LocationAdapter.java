package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

/**
 * Created by Rohit Suratekar on 15-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.MyViewHolder> {

    LocationAdapter(List<LocationModel> items) {
        this.entryList = items;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.locations_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LocationModel entry = entryList.get(position);
        holder.name.setText(entry.getName());
        holder.old_name.setText(entry.getOldName());
        holder.details.setText(entry.getDetails());
        holder.building.setText(entry.getBuilding());
        holder.floor.setText(getFloor(entry.getFloor()));
    }

    private String getFloor(int i) {
        return String.valueOf(i);
       /* switch (i) {
            case 0:
                return "GF";
            case 1:
                return "FF";
            case 2:
                return "SF";
            case 3:
                return "TF";
            case -1:
                return "BM";
            default:
                return "--";
        }*/
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }

    private List<LocationModel> entryList;


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, old_name, details, floor, building;
        ImageView icon;
        ConstraintLayout mainLayout;

        MyViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.loc_item_name);
            old_name = view.findViewById(R.id.loc_item_old_name);
            details = view.findViewById(R.id.loc_item_details);
            floor = view.findViewById(R.id.loc_item_floor);
            building = view.findViewById(R.id.loc_item_building);
            mainLayout = view.findViewById(R.id.loc_item_layout);
            icon = view.findViewById(R.id.loc_item_icon);

        }
    }


}