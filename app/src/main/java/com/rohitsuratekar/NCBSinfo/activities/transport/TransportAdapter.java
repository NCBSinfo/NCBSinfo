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

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.TripHolder> {

    private int nextTripIndex;
    private int size;

    TransportAdapter(int nextTripIndex, int size) {
        this.nextTripIndex = nextTripIndex;
        this.size = size;
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_trip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if (position < nextTripIndex) {
            if (position == 0) {
                holder.icon.setImageResource(R.drawable.icon_circle_end);
                holder.icon.setScaleY(-1f);
            } else {
                if (position == size - 1) {
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

    @Override
    public int getItemCount() {
        return size;
    }

    class TripHolder extends RecyclerView.ViewHolder {
        ImageView icon, half_circle;
        TextView text;

        public TripHolder(View itemView) {
            super(itemView);
            icon = ButterKnife.findById(itemView, R.id.tp_trip_icon);
            text = ButterKnife.findById(itemView, R.id.tp_trip_text);
            half_circle = ButterKnife.findById(itemView, R.id.tp_trip_half_circle);
        }
    }
}
