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
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 15-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TripAdapter extends RecyclerView.Adapter<TripAdapter.TripHolder> {

    private List<String> trips;
    private int nextTrip;

    TripAdapter(List<String> trips, int nextTrip) {
        this.trips = trips;
        this.nextTrip = nextTrip;
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TripHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transport_trip_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(TripHolder tripHolder, int i) {
        tripHolder.text.setText(trips.get(i));
        Context context = tripHolder.itemView.getContext();
        if (i < nextTrip) {
            tripHolder.text.setTextColor(ContextCompat.getColor(context, R.color.divider));
        } else if (i == nextTrip) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_fade_out);
            tripHolder.text.setAlpha(1);
            tripHolder.text.setTypeface(tripHolder.text.getTypeface(), Typeface.BOLD);
            tripHolder.text.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
            tripHolder.text.startAnimation(animation);
        }

    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    class TripHolder extends RecyclerView.ViewHolder {

        TextView text;

        TripHolder(View itemView) {
            super(itemView);
            text = ButterKnife.findById(itemView, R.id.transport_trip_text);
        }
    }
}
