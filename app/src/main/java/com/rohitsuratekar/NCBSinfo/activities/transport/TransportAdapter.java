package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.TripHolder> {

    private List<String> list;

    TransportAdapter(List<String> list) {
        this.list = list;
    }

    @Override
    public TripHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TripHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(TripHolder holder, int position) {

        try {
            holder.tripName.setText(DateConverter.changeFormat(ConverterMode.DATE_FIRST, list.get(position), "hh:mm a"));
        } catch (ParseException e) {
            e.printStackTrace();
            holder.tripName.setText("--:--");
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class TripHolder extends RecyclerView.ViewHolder {
        TextView tripName;

        TripHolder(View itemView) {
            super(itemView);
            tripName = ButterKnife.findById(itemView, R.id.tp_list_trip);
        }
    }
}
