package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

/**
 * Created by Rohit Suratekar on 15-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private List<DashboardItem> itemList;
    private OnItemClick onItemClick;

    DashboardAdapter(List<DashboardItem> itemList, OnItemClick onItemClick) {
        this.itemList = itemList;
        this.onItemClick = onItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        DashboardItem item = itemList.get(position);

        holder.title.setText(item.getTitle());
        holder.subtitle.setText(item.getSubtitle());
        if (item.isEditable()) {
            holder.editableIcon.setVisibility(View.VISIBLE);
        } else {
            holder.editableIcon.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.itemClicked(holder.getAdapterPosition());
            }
        });

        if (item.getAction() == Dashboard.ACTION_EMAIL || item.getAction() == Dashboard.ACTION_SYNC) {
            holder.title.setAlpha(0.6f);
        } else {
            holder.title.setAlpha(1f);
        }

        if (item.getIcon() != 0) {
            holder.icon.setVisibility(View.VISIBLE);
            holder.icon.setImageResource(item.getIcon());
        } else {
            holder.icon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, subtitle;
        ImageView icon, editableIcon;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.db_item_title);
            subtitle = itemView.findViewById(R.id.db_item_subtitle);
            editableIcon = itemView.findViewById(R.id.db_item_edit);
            icon = itemView.findViewById(R.id.db_item_icon);
        }
    }

    interface OnItemClick {
        void itemClicked(int position);
    }
}
