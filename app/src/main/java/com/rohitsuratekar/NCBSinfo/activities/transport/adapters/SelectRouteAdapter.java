package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

public class SelectRouteAdapter extends RecyclerView.Adapter<SelectRouteAdapter.MyViewHolder> {

    public SelectRouteAdapter(List<String[]> items) {
        this.entryList = items;
    }

    @Override
    public SelectRouteAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transport_bot_sheet_item, parent, false);

        return new SelectRouteAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SelectRouteAdapter.MyViewHolder holder, int position) {
        String[] entry = entryList.get(position);
        holder.origin.setText(entry[0]);
        holder.destination.setText(entry[1]);
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<String[]> entryList;
    View currentview;
    private SelectRouteAdapter.ClickListener myClickListener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView origin, destination;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
            origin = (TextView) view.findViewById(R.id.bs_list_origin);
            destination = (TextView) view.findViewById(R.id.bs_list_destination);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.bs_list_item);
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
