package com.rohitsuratekar.NCBSinfo.activities.transport.edit.route;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

class LocationSuggestionAdapter extends RecyclerView.Adapter<LocationSuggestionAdapter.MyViewHolder> {

    LocationSuggestionAdapter(List<LocationSuggestions> items) {
        this.entryList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transport_route_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LocationSuggestions entry = entryList.get(position);
        holder.origin.setText(entry.getOrigin());
        holder.destination.setText(entry.getDestination());
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<LocationSuggestions> entryList;
    private ClickListener myClickListener;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView origin, destination;
        ConstraintLayout mainLayout;

        MyViewHolder(final View view) {
            super(view);
            origin = (TextView) view.findViewById(R.id.tp_bs_list_origin);
            destination = (TextView) view.findViewById(R.id.tp_bs_list_destination);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.tp_bs_list_item);
            context = view.getContext();
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    interface ClickListener {
        void onItemClick(int position);
    }

}

