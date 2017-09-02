package com.rohitsuratekar.NCBSinfo.activities.settings.log;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.List;

class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {

    LogAdapter(List<String> items) {
        this.entryList = items;
    }

    private Context context;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.log_item, parent, false);
        context = itemView.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String full = entryList.get(position);
        String[] entry = entryList.get(position).split(":");
        if (entry.length > 3) {
            if (full.contains("SecretBiology")) {
                holder.text.setTextColor(General.getColor(context, R.color.green));
            } else {
                holder.text.setTextColor(General.getColor(context, R.color.colorSecondaryText));
            }
            holder.text.setText(entry[3]);
        }

    }


    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<String> entryList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        MyViewHolder(final View view) {
            super(view);
            text = (TextView) view.findViewById(R.id.log_item_text);
        }
    }


}
