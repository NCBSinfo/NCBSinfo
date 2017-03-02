package com.rohitsuratekar.NCBSinfo.activities.settings;


import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SettingsIDs {

    SettingsAdapter(List<SettingsModel> items) {
        this.entryList = items;
    }

    private Context context;
    private ClickListener myClickListener;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_SECTION_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_item_header, parent, false);
            context = itemView.getContext();
            return new SectionHolder(itemView);
        } else if (viewType == VIEW_DIVIDER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_divider, parent, false);
            context = itemView.getContext();
            return new SectionDivider(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.settings_item, parent, false);
            context = itemView.getContext();
            return new MyViewHolder(itemView);
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        SettingsModel entry = entryList.get(position);

        if (entry.getViewType() == VIEW_SECTION_HEADER) {
            final SectionHolder section = (SectionHolder) viewHolder;
            section.title.setText(entry.getTitle());
        } else if (entry.getViewType() == VIEW_ITEM) {

            final MyViewHolder holder = (MyViewHolder) viewHolder;
            holder.title.setText(entry.getTitle());
            holder.subtitle.setText(entry.getSubtitle());
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(holder.getLayoutPosition());
                }
            });

            if (entry.getIcon() != 0) {
                holder.icon.setVisibility(View.VISIBLE);
                holder.icon.setImageResource(entry.getIcon());
            } else {
                holder.icon.setVisibility(View.INVISIBLE);
            }
        }
    }


    @Override
    public int getItemCount() {
        return entryList.size();

    }

    @Override
    public int getItemViewType(int position) {
        return entryList.get(position).getViewType();
    }


    private List<SettingsModel> entryList;

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView icon;
        ConstraintLayout layout;

        MyViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.st_txt_title);
            subtitle = (TextView) view.findViewById(R.id.st_txt_subtitle);
            layout = (ConstraintLayout) view.findViewById(R.id.st_item_layout);
            icon = (ImageView) view.findViewById(R.id.st_icon);
        }
    }

    private class SectionHolder extends RecyclerView.ViewHolder {
        TextView title;

        SectionHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.st_item_header);
        }
    }

    private class SectionDivider extends RecyclerView.ViewHolder {
        SectionDivider(final View view) {
            super(view);
        }
    }


    void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    interface ClickListener {
        void onItemClick(int position);
    }


}