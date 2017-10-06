package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

/**
 * Created by Rohit Suratekar on 05-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.TripHolder> {

    private List<String> tripList;
    private int nextTrip;

    TransportAdapter(List<String> tripList, int nextTrip) {
        this.tripList = tripList;
        this.nextTrip = nextTrip;
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_trip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {
        Context context = holder.itemView.getContext();

        holder.dotExtra.setVisibility(View.GONE);
        holder.helpText.setVisibility(View.GONE);
        holder.arrow.setVisibility(View.GONE);
        holder.trip.setTypeface(Typeface.DEFAULT);

        LayerDrawable layers = (LayerDrawable) holder.dot.getDrawable();
        GradientDrawable shape = (GradientDrawable) layers.findDrawableByLayerId(R.id.circle_outer_layer);
        shape.mutate(); //Need this so that android won't catch
        shape.setColor(ContextCompat.getColor(context, R.color.transportIndicator));

        if (position < nextTrip) {
            holder.dot.setImageResource(R.drawable.circle);
        }

        if (position > nextTrip) {
            holder.top.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            holder.bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            shape.setColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));

        }
        if (position == nextTrip) {
            Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
            holder.dot.startAnimation(pulse);
            holder.bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            shape.setColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.dotExtra.setVisibility(View.VISIBLE);
            holder.helpText.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.trip.setTypeface(Typeface.DEFAULT_BOLD);
            holder.trip.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        }
        if (position == tripList.size() - 1) {
            holder.bottom.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    class TripHolder extends RecyclerView.ViewHolder {
        TextView trip, helpText;
        ImageView dot, top, bottom, dotExtra, arrow;

        TripHolder(View itemView) {
            super(itemView);
            trip = itemView.findViewById(R.id.tp_item_trip);
            top = itemView.findViewById(R.id.tp_item_bar_top);
            bottom = itemView.findViewById(R.id.tp_item_bar_bottom);
            dot = itemView.findViewById(R.id.tp_item_dot);
            dotExtra = itemView.findViewById(R.id.tp_item_dot_extra);
            arrow = itemView.findViewById(R.id.tp_item_arrow);
            helpText = itemView.findViewById(R.id.tp_item_help_text);
        }
    }
}
