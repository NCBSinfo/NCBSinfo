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
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 16-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.TripHolder> {

    private int nextTripIndex;
    private List<String> trips;

    TransportAdapter(int nextTripIndex, List<String> trips) {
        this.nextTripIndex = nextTripIndex;
        this.trips = trips;
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_trip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.icon.setImageResource(R.drawable.icon_circle);
        holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        holder.text.setTypeface(Typeface.DEFAULT);
        holder.icon.clearAnimation();
        holder.half_circle.setVisibility(View.GONE);

        try {
            holder.text.setText(DateConverter.changeFormat(ConverterMode.DATE_FIRST, trips.get(position), "hh:mm a"));
        } catch (ParseException e) {
            Log.error(e.getMessage());
            holder.text.setText("--:--");
        }
        if (position < nextTripIndex) {
            if (position == 0) {
                holder.icon.setImageResource(R.drawable.icon_circle_end);
                holder.icon.setScaleY(-1f);
            } else {
                if (position == trips.size() - 1) {
                    holder.icon.setImageResource(R.drawable.icon_circle_end);
                } else {
                    holder.icon.setImageResource(R.color.colorPrimary);
                }
            }
        } else if (position == nextTripIndex) {
            if (position != 0) {
                holder.half_circle.setVisibility(View.VISIBLE);
            }
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent));
            Animation pulse = AnimationUtils.loadAnimation(context, R.anim.pulse);
            holder.icon.startAnimation(pulse);
            holder.text.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.icon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        }


    }

    void setNextTripIndex(int nextTripIndex) {
        this.nextTripIndex = nextTripIndex;
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    class TripHolder extends RecyclerView.ViewHolder {
        ImageView icon, half_circle;
        TextView text;

        TripHolder(View itemView) {
            super(itemView);
            icon = ButterKnife.findById(itemView, R.id.tp_trip_icon);
            text = ButterKnife.findById(itemView, R.id.tp_trip_text);
            half_circle = ButterKnife.findById(itemView, R.id.tp_trip_half_circle);
        }
    }
}