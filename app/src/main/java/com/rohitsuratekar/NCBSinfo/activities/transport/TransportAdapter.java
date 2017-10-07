package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.graphics.Typeface;
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
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Rohit Suratekar on 05-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.TripHolder> {

    private List<String> tripList;
    private int nextTrip;
    private String message;

    TransportAdapter(List<String> tripList, int nextTrip) {
        this.tripList = tripList;
        this.nextTrip = nextTrip;
        this.message = "";
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_trip_item, parent, false));
    }

    void updateNext(int index) {
        this.nextTrip = index;
        notifyDataSetChanged();
    }

    void updateMessage(String message) {
        this.message = message;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {
        Context context = holder.itemView.getContext();

        holder.dotExtra.setVisibility(View.GONE);
        holder.helpText.setVisibility(View.GONE);
        holder.arrow.setVisibility(View.GONE);
        holder.trip.setTypeface(Typeface.DEFAULT);
        holder.trip.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryText));
        holder.top.setVisibility(View.VISIBLE);
        holder.bottom.setVisibility(View.VISIBLE);


        if (position < nextTrip) {
            holder.dot.setImageResource(R.drawable.circle);
            holder.top.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else if (position > nextTrip) {
            holder.top.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            holder.bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            holder.dot.setImageResource(R.drawable.empty_circle);
        } else if (position == nextTrip) {
            Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
            holder.dot.startAnimation(pulse);
            holder.top.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.bottom.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryLight));
            holder.dot.setImageResource(R.drawable.empty_circle_activated);
            holder.dotExtra.setVisibility(View.VISIBLE);
            holder.helpText.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.trip.setTypeface(Typeface.DEFAULT_BOLD);
            holder.trip.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            holder.helpText.setText(message);
        }
        if (position == tripList.size() - 1) {
            holder.bottom.setVisibility(View.INVISIBLE);
        }

        try {
            holder.trip.setText(DateConverter.changeFormat(ConverterMode.DATE_FIRST, tripList.get(position), "hh:mm a"));
        } catch (ParseException e) {
            holder.trip.setText("--:--");
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
