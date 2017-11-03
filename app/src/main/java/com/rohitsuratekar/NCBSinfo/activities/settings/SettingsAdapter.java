package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

/**
 * Created by SecretAdmin on 10/30/2017 for NCBSinfo.
 * All code is released under MIT License.
 */

class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements SettingsActions {


    private List<SettingsModel> modelList;
    private OnSelect select;

    SettingsAdapter(List<SettingsModel> modelList, OnSelect select) {
        this.modelList = modelList;
        this.select = select;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_ITEM:
                return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false));
            case VIEW_HEADER:
                return new Header(LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_header, parent, false));
            default:
                return new Line(LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_line, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        SettingsModel model = modelList.get(position);


        if (model.getViewType() == VIEW_ITEM) {
            ItemHolder item = (ItemHolder) holder;


            //Reset
            item.icon.setImageResource(android.R.color.transparent);
            item.icon.setBackgroundResource(android.R.color.transparent);
            item.title.setAlpha(1f);
            item.details.setAlpha(0.54f);
            item.icon.setAlpha(1f);


            if (model.getIcon() != 0) {
                item.icon.setImageResource(model.getIcon());
            } else {
                item.icon.setImageResource(android.R.color.transparent);
            }

            item.title.setText(model.getTitle());
            if (model.getDescription() != null) {
                item.details.setText(model.getDescription());
            } else {
                item.details.setText("");
            }

            if (model.isDisabled()) {
                item.title.setAlpha(0.38f);
                item.details.setAlpha(0.38f);
                item.icon.setAlpha(0.38f);
            }

            if (model.getAction() == ACTION_APP_DETAILS) {
                item.icon.setImageResource(android.R.color.transparent);
                item.icon.setBackgroundResource(R.mipmap.ic_launcher_round);
            }

            item.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select.clicked(holder.getLayoutPosition());
                }
            });

        } else if (model.getViewType() == VIEW_HEADER) {
            Header header = (Header) holder;
            header.header.setText(model.getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return modelList.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title, details;

        ItemHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.st_item_icon);
            title = itemView.findViewById(R.id.st_item_title);
            details = itemView.findViewById(R.id.st_item_details);
        }
    }

    class Header extends RecyclerView.ViewHolder {

        TextView header;

        Header(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.st_icon_header);
        }
    }

    class Line extends RecyclerView.ViewHolder {

        Line(View itemView) {
            super(itemView);
        }
    }

    interface OnSelect {
        void clicked(int position);
    }
}
