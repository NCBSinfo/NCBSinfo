package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;

import java.util.List;

public class TransportRouteListAdapter extends RecyclerView.Adapter<TransportRouteListAdapter.MyViewHolder> {

    public TransportRouteListAdapter(List<Route> items) {
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
        Route entry = entryList.get(position);
        holder.origin.setText(entry.getOrigin().toUpperCase());
        if (!entry.getType().toString().toLowerCase().equals("shuttle")) {
            holder.destination.setText(entry.getDestination().toUpperCase() + " ( " + entry.getType().toString().toLowerCase() + " )");
        } else {
            holder.destination.setText(entry.getDestination().toUpperCase());
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<Route> entryList;
    View currentview;
    private TransportRouteListAdapter.ClickListener myClickListener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView origin, destination;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
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

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }

}
