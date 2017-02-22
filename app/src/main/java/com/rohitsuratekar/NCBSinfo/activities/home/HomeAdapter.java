package com.rohitsuratekar.NCBSinfo.activities.home;

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


class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    HomeAdapter(List<SuggestionModel> items) {
        this.entryList = items;
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item_suggestions, parent, false);

        return new HomeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HomeAdapter.MyViewHolder holder, int position) {
        SuggestionModel entry = entryList.get(position);
        holder.details.setText(entry.getDetails());
        holder.icon.setImageResource(entry.getIcon());
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myClickListener.onItemClick(holder.getLayoutPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<SuggestionModel> entryList;
    View currentview;
    private HomeAdapter.ClickListener myClickListener;
    Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView details;
        ImageView icon;
        ConstraintLayout mainLayout;

        MyViewHolder(final View view) {
            super(view);
            currentview = view;
            details = (TextView) view.findViewById(R.id.hm_item_sugg_text);
            icon = (ImageView) view.findViewById(R.id.hm_item_sugg_icon);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.hm_item_layout);
            context = view.getContext();

        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    interface ClickListener {
        void onItemClick(int position);
    }
}
