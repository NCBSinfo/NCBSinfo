package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportSheetAdapter extends RecyclerView.Adapter<TransportSheetAdapter.ItemHolder> {

    private List<RouteData> routeDataList;
    private int index;
    private int reverseIndex;
    private onRouteClick click;

    TransportSheetAdapter(List<RouteData> routeDataList, int index, int reverseIndex) {
        this.routeDataList = routeDataList;
        this.index = index;
        this.reverseIndex = reverseIndex;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_sheet_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Context context = holder.itemView.getContext();
        final RouteData data = routeDataList.get(position);
        holder.text.setText(context.getString(R.string.home_card_title, data.getOrigin().toUpperCase(), data.getDestination().toUpperCase()));
        holder.subText.setText(data.getType());

        if (index == data.getRouteID()) {
            holder.icon.setImageResource(R.color.green);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.clicked(data.getRouteID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return routeDataList.size();
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

    void setOnRouteClick(onRouteClick c) {
        click = c;
    }

    interface onRouteClick {
        void clicked(int routeID);
    }

}
