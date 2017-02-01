package com.rohitsuratekar.NCBSinfo.activities.dashboard;

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


class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.MyViewHolder> {

    DashboardAdapter(List<DashboardModel> items) {
        this.entryList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        DashboardModel entry = entryList.get(position);
        holder.title.setText(entry.getTitle());
        holder.subtitle.setText(entry.getSubtitle());
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

    private List<DashboardModel> entryList;
    private ClickListener myClickListener;
    private Context context;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView icon;
        ConstraintLayout mainLayout;

        MyViewHolder(final View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.dash_item_title);
            subtitle = (TextView) view.findViewById(R.id.dash_item_subtitle);
            icon = (ImageView) view.findViewById(R.id.dash_item_icon);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.dash_item_layout);
            context = view.getContext();
        }
    }

    void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    interface ClickListener {
        void onItemClick(int position);
    }

}
