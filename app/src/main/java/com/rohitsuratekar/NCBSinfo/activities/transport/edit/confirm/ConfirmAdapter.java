package com.rohitsuratekar.NCBSinfo.activities.transport.edit.confirm;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportRecyclerItem;

import java.util.List;

class ConfirmAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TransportRecyclerItem> list;
    private ConfirmAdapter.onSelected selected;

    ConfirmAdapter(List<TransportRecyclerItem> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_edit_item, parent, false);
            return new TripViewHolder(itemView);
        } else {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transport_confirm_selected_item, parent, false);
            return new SelectedItem(itemView);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        TransportRecyclerItem item = list.get(position);
        if (item.getViewType() == 0) {
            ((TripViewHolder) holder).time.setText(list.get(position).getText());
            ((TripViewHolder) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected.onItemSelected(holder.getLayoutPosition());
                }
            });
        } else {
            ((SelectedItem) holder).time.setText(list.get(position).getText());
            ((SelectedItem) holder).layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected.onItemSelected(holder.getLayoutPosition());
                }
            });
            Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.fade_in_fade_out);
            ((SelectedItem) holder).imageView.setAnimation(animation);
            animation.start();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class TripViewHolder extends RecyclerView.ViewHolder {
        TextView time;
        ConstraintLayout layout;

        TripViewHolder(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tp_edit_item_text);
            layout = (ConstraintLayout) itemView.findViewById(R.id.tp_edit_item_layout);
        }
    }

    private class SelectedItem extends RecyclerView.ViewHolder {
        TextView time;
        ConstraintLayout layout;
        ImageView imageView;

        SelectedItem(View itemView) {
            super(itemView);
            time = (TextView) itemView.findViewById(R.id.tp_confirm_selected_text);
            imageView = (ImageView) itemView.findViewById(R.id.tp_confirm_selected_icon);
            layout = (ConstraintLayout) itemView.findViewById(R.id.tp_confrim_selected_layout);
        }
    }


    @Override
    public int getItemViewType(int position) {
        return list.get(position).getViewType();
    }

    public interface onSelected {
        void onItemSelected(int position);
    }

    void setOnItemClickListener(onSelected selected) {
        this.selected = selected;
    }


}

