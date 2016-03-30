package com.rohitsuratekar.NCBSinfo.adapters;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;

import java.util.List;

public class adapters_userNotificationLog extends RecyclerView.Adapter<adapters_userNotificationLog.MyViewHolder> {

    private List<models_userNotifications> entryList;
    View currentview;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, message, timestamp;
        public LinearLayout layout1;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            title = (TextView) view.findViewById(R.id.notLog_title);
            message = (TextView) view.findViewById(R.id.notLog_message);
            timestamp = (TextView) view.findViewById(R.id.notLog_timestamp);
            layout1 = (LinearLayout)view.findViewById(R.id.not_userLogLayout);
            image = (ImageView)view.findViewById(R.id.notification_log_icon);
        }
    }


    public adapters_userNotificationLog(List<models_userNotifications> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_notificationreceiver_log_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        models_userNotifications entry = entryList.get(position);
        holder.title.setText(entry.getNotificationTitle());
        holder.message.setText(entry.getNotificationMessage());
        holder.timestamp.setText(entry.getTimestamp());

        if (entry.getNotificationTopic().equals(GCMConstants.GCM_TOPIC_jc)){
            holder.layout1.setBackgroundColor(currentview.getResources().getColor(R.color.user_log_topic_jc));
            holder.image.setBackgroundResource(R.drawable.icon_jc);
        }
        else if (entry.getNotificationTopic().equals(GCMConstants.GCM_TOPIC_student)){
            holder.layout1.setBackgroundColor(currentview.getResources().getColor(R.color.user_log_topic_student));
            holder.image.setBackgroundResource(R.drawable.icon_students);
        }
        else if (entry.getNotificationTopic().equals(GCMConstants.GCM_TOPIC_TALK)){
            holder.layout1.setBackgroundColor(currentview.getResources().getColor(R.color.user_log_topic_talk));
            holder.image.setBackgroundResource(R.drawable.icon_research_talk);
        }
        else if (entry.getNotificationTopic().equals(GCMConstants.GCM_TOPIC_EMERGENCY)){
            holder.layout1.setBackgroundColor(currentview.getResources().getColor(R.color.user_log_topic_emergency));
            holder.image.setBackgroundResource(R.drawable.icon_emergency);
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }
}