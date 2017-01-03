package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {

    public StepAdapter(List<StepModel> items) {
        this.entryList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_step_indicator, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        StepModel entry = entryList.get(position);
        holder.icon.setImageResource(entry.getIcon());
        if (position == 0) {
            holder.left.setVisibility(View.INVISIBLE);
        }
        if (position == entryList.size() - 1) {
            holder.right.setVisibility(View.INVISIBLE);
        }

        if (position == currentLocation) {
            holder.icon.setImageResource(R.drawable.icon_checked);
        }

        if (entryList.get(position).isSkipped()) {
            holder.icon.setColorFilter(General.getColor(context, R.color.colorFavorite));
        }

        if (entryList.get(position).isDone()) {
            holder.icon.setImageResource(R.drawable.icon_checked_done);
            holder.icon.setColorFilter(General.getColor(context, R.color.green));
        }

    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<StepModel> entryList;
    private ClickListener myClickListener;
    private Context context;
    private int currentLocation = 0;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView icon, left, right;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            icon = (ImageView) view.findViewById(R.id.step_image);
            right = (ImageView) view.findViewById(R.id.step_right);
            left = (ImageView) view.findViewById(R.id.step_left);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getLayoutPosition());
                }
            });
            context = view.getContext();

        }
    }

    public void updateLocation(int currentLocation) {
        this.currentLocation = currentLocation;
        notifyDataSetChanged();
    }

    public void isSkipped(int loc, boolean isIt) {
        entryList.get(loc).setSkipped(isIt);
        notifyDataSetChanged();
    }

    public void isDone(int location, boolean isIt) {
        entryList.get(location).setDone(isIt);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }


}
