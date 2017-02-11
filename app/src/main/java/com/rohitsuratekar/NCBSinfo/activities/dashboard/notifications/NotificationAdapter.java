package com.rohitsuratekar.NCBSinfo.activities.dashboard.notifications;


import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;

import java.util.List;

class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationModel> modelList;
    private OnNotificationClick notificationClick;

    NotificationAdapter(List<NotificationModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public NotificationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notifications_item, parent, false);
        return new NotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotificationViewHolder holder, int position) {
        NotificationModel model = modelList.get(position);
        holder.title.setText(model.getTitle());
        holder.message.setText(model.getMessage());
        holder.timestamp.setText(model.getTimestamp());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title, message, timestamp;
        ConstraintLayout layout;

        NotificationViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.notification_item_title);
            message = (TextView) itemView.findViewById(R.id.notification_item_message);
            timestamp = (TextView) itemView.findViewById(R.id.notification_item_timestamp);
            layout = (ConstraintLayout) itemView.findViewById(R.id.notification_item_layout);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notificationClick.showFull(v, getLayoutPosition());
                }
            });
        }
    }

    void setOnNotificationClick(OnNotificationClick click) {
        this.notificationClick = click;
    }

    public interface OnNotificationClick {
        void showFull(View v, int position);
    }


}
