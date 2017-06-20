package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportSheetAdapter extends RecyclerView.Adapter<TransportSheetAdapter.ItemHolder> {

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView text, subText;

        ItemHolder(View itemView) {
            super(itemView);
            icon = ButterKnife.findById(itemView, R.id.tp_sheet_icon);
            text = ButterKnife.findById(itemView, R.id.tp_sheet_text);
            subText = ButterKnife.findById(itemView, R.id.tp_sheet_subtext);
        }
    }

}
