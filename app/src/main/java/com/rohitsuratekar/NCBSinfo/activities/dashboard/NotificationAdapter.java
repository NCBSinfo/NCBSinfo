package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;
import com.rohitsuratekar.NCBSinfo.utilities.General;

import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 13-07-16.
 */
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private List<NotificationModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, message, type, timestamp;
        public LinearLayout icon_holder, fullLayout;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            currentview = view;
            title = (TextView) view.findViewById(R.id.notification_title);
            message = (TextView) view.findViewById(R.id.notification_details);
            icon_holder = (LinearLayout) view.findViewById(R.id.notification_holder);
            fullLayout = (LinearLayout) view.findViewById(R.id.notification_layout);
            type = (TextView) view.findViewById(R.id.notificatiob_type);
            timestamp = (TextView) view.findViewById(R.id.notification_timestamp);
            icon = (ImageView) view.findViewById(R.id.notification_icon);
            fullLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public NotificationAdapter(List<NotificationModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NotificationModel entry = entryList.get(position);
        String message = entry.getMessage();
        if (message.length() > 50) {
            message = message.trim();
            message = message.substring(0, 47);
            message = message + "...";
        }

        holder.title.setText(entry.getTitle());
        holder.message.setText(message);
        holder.timestamp.setText(new General().makeReadableTime(
                new DateConverters().convertToDate(entry.getTimestamp())));
        holder.type.setText("INFO");
        holder.icon.setImageResource(R.drawable.icon_information);
        holder.icon_holder.setBackgroundColor(currentview.getResources().getColor(R.color.general_message));
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }


}
