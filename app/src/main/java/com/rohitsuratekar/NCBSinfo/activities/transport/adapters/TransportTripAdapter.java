package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.List;

public class TransportTripAdapter extends RecyclerView.Adapter<TransportTripAdapter.MyViewHolder> {

    private List<String> entryList;
    private int currentItem;
    private ClickListener myClickListener;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView details;
        ImageView dot, top, bottom;
        ConstraintLayout mainLayout;

        MyViewHolder(final View view) {
            super(view);
            details = (TextView) view.findViewById(R.id.tp_edit_item_text);
            dot = (ImageView) view.findViewById(R.id.tp_list_dot);
            top = (ImageView) view.findViewById(R.id.tp_list_top_line);
            bottom = (ImageView) view.findViewById(R.id.tp_list_bottom_line);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.tp_list_layout);
            context = view.getContext();


        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TransportTripAdapter(List<String> entryList, int currentItem) {
        this.entryList = entryList;
        this.currentItem = currentItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transport_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String entry = entryList.get(position);
        holder.details.setText(entry);

        //Universal
        if (position == 0) {
            holder.top.setVisibility(View.INVISIBLE);
        }
        if (position == entryList.size() - 1) {
            holder.bottom.setVisibility(View.INVISIBLE);
        }

        //Local

        if (position < currentItem) {
            holder.dot.setColorFilter(General.getColor(context, R.color.colorSecondaryText));
            holder.details.setTextColor(General.getColor(context, R.color.colorSecondaryText));
            holder.dot.setImageResource(R.drawable.icon_dot);
            holder.dot.setAlpha((float) 0.6);
            holder.details.setAlpha((float) 0.6);
        } else if (position == currentItem) {
            holder.dot.setColorFilter(General.getColor(context, R.color.colorAccent));
            holder.details.setTextColor(General.getColor(context, R.color.colorPrimary));
            holder.dot.setImageResource(R.drawable.icon_gps_on);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_fade_out);
            holder.dot.startAnimation(animation);
            holder.dot.setAlpha((float) 1);
            holder.details.setAlpha((float) 1);
        } else {
            holder.dot.setColorFilter(General.getColor(context, R.color.colorSecondaryText));
            holder.details.setTextColor(General.getColor(context, R.color.colorSecondaryText));
            holder.dot.setImageResource(R.drawable.icon_gps_off);
            holder.dot.setAlpha((float) 1);
            holder.details.setAlpha((float) 1);
        }


    }

    public void setCurrentItem(int item) {
        this.currentItem = item;
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    interface ClickListener {
        void onItemClick(int position);
    }


}
