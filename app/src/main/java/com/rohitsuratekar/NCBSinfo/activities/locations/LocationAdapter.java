package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ramotion.foldingcell.FoldingCell;
import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    public List<String[]> allLocations;
    unfoldCell cell;

    public LocationAdapter(List<String[]> allLocations) {
        this.allLocations = allLocations;
    }

    @Override
    public LocationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_location, parent, false));
    }

    @Override
    public void onBindViewHolder(LocationAdapter.ViewHolder holder, int position) {
        String[] entry = allLocations.get(position);
        holder.header.setText(entry[0]);
        holder.name.setText(entry[0]);
        holder.oldname.setText(entry[1]);
        holder.landmark.setText(entry[2]);
    }

    @Override
    public int getItemCount() {
        return allLocations.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        FoldingCell foldingCell;
        TextView header, name, oldname, landmark;


        ViewHolder(View itemView) {
            super(itemView);
            foldingCell = (FoldingCell) itemView.findViewById(R.id.recycler_folding);
            header = (TextView) itemView.findViewById(R.id.loc_list_header);
            name = (TextView) itemView.findViewById(R.id.loc_list_name);
            oldname = (TextView) itemView.findViewById(R.id.loc_list_old_name);
            landmark = (TextView) itemView.findViewById(R.id.loc_list_landmark);
            foldingCell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cell.onItemClick(getLayoutPosition(), v);
                }
            });
        }
    }

    public void unfoldCell(unfoldCell cell) {
        this.cell = cell;
    }


    public interface unfoldCell {
        void onItemClick(int position, View v);
    }

}
