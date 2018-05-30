package com.rohitsuratekar.NCBSinfo.fragments.transport;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.Helper;

import java.text.ParseException;
import java.util.List;

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.ViewHolder> {

    private int highlightIndex;
    private List<String> tripList;
    private String type;

    TransportAdapter(List<String> tripList, int highlightIndex) {
        this.tripList = tripList;
        this.highlightIndex = highlightIndex;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_trip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        holder.ending.setVisibility(View.GONE);
        holder.text.setTypeface(Typeface.DEFAULT);
        holder.text.setTextColor(ContextCompat.getColor(context, android.R.color.primary_text_light));

        try {
            holder.text.setText(Helper.displayTime(tripList.get(position)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.extraText.setText(context.getString(R.string.hm_next, type));

        if (position < highlightIndex) {
            holder.text.setTextColor(ContextCompat.getColor(context, android.R.color.secondary_text_dark));
        }

        if (position == highlightIndex) {
            holder.frontImage.setImageResource(R.color.colorAccent);
            holder.extraText.setVisibility(View.VISIBLE);
            holder.backImage.setVisibility(View.INVISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.text.setTypeface(Typeface.DEFAULT_BOLD);
            holder.text.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            holder.frontImage.setImageResource(R.color.colorPrimary);
            holder.extraText.setVisibility(View.INVISIBLE);
            holder.arrow.setVisibility(View.INVISIBLE);
            holder.backImage.setVisibility(View.VISIBLE);
        }
        if (position > highlightIndex) {
            holder.text.setTextColor(ContextCompat.getColor(context, android.R.color.primary_text_light));
            holder.backImage.setImageResource(R.color.colorLight);
            holder.frontImage.setImageResource(R.color.colorLight);
            holder.text.setTypeface(Typeface.DEFAULT);
            holder.ending.setColorFilter(ContextCompat.getColor(context, R.color.colorLight));
        }

        if (position == tripList.size() - 1) {
            holder.ending.setVisibility(View.VISIBLE);
            holder.backImage.setVisibility(View.INVISIBLE);
        }

    }

    void updateNext(int index) {
        this.highlightIndex = index;
        notifyDataSetChanged();
    }

    void updateMessage(String message) {
        this.type = message;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tripList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView backImage, frontImage, arrow, ending;
        TextView text, extraText;

        ViewHolder(View itemView) {
            super(itemView);
            backImage = itemView.findViewById(R.id.tp_item_back_image);
            frontImage = itemView.findViewById(R.id.tp_item_front_image);
            arrow = itemView.findViewById(R.id.tp_item_arrow);
            text = itemView.findViewById(R.id.tp_item_text);
            extraText = itemView.findViewById(R.id.tp_item_extra_text);
            ending = itemView.findViewById(R.id.tp_item_ending);
        }
    }
}
