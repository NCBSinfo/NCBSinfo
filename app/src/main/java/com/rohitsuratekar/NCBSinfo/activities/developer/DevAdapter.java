package com.rohitsuratekar.NCBSinfo.activities.developer;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

public class DevAdapter extends RecyclerView.Adapter<DevAdapter.ViewHolder> {

    private List<DevRecyclerModel> modelList;

    public DevAdapter(List<DevRecyclerModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.developers_options_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.header.setText(modelList.get(position).getTitle());
        holder.text.setText(modelList.get(position).getText());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView header, text;

        ViewHolder(View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.dev_item_heading);
            text = itemView.findViewById(R.id.dev_item_text);
        }
    }
}
