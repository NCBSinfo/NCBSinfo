package com.rohitsuratekar.NCBSinfo.fragments.informtion;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder> {

    private List<InfoModel> modelList;
    private OnInfoClick onInfoClick;

    public InfoAdapter(List<InfoModel> modelList, OnInfoClick onInfoClick) {
        this.modelList = modelList;
        this.onInfoClick = onInfoClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.info_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final InfoModel model = modelList.get(position);
        if (model.getImage() != 0) {
            holder.image.setImageResource(model.getImage());
        }
        holder.title.setText(model.getTitle());
        if (model.getDetails() != 0) {
            holder.details.setText(model.getDetails());
        } else {
            holder.details.setVisibility(View.GONE);
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInfoClick.clicked(model.getAction());
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ConstraintLayout layout;
        ImageView image;
        TextView title, details;

        ViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.info_card);
            layout = itemView.findViewById(R.id.info_layout);
            image = itemView.findViewById(R.id.info_card_image);
            title = itemView.findViewById(R.id.info_card_title);
            details = itemView.findViewById(R.id.info_card_details);
        }
    }

    public interface OnInfoClick {
        void clicked(int actionID);
    }
}
