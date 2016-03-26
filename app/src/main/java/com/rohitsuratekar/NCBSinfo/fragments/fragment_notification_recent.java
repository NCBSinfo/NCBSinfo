package com.rohitsuratekar.NCBSinfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;

import java.util.ArrayList;
import java.util.List;

public class fragment_notification_recent extends Fragment {

    TextView title, message, timestamp;
    List<models_userNotifications> entrylist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_notificationreceiver_recent, container, false);

        title = (TextView)rootView.findViewById(R.id.recent_Title);
        message = (TextView)rootView.findViewById(R.id.recent_Message);
        timestamp = (TextView)rootView.findViewById(R.id.recent_Timestamp);

        DatabaseHelper db = new DatabaseHelper(rootView.getContext());
        entrylist = db.getAllEntries();
        int itemnumber = entrylist.size();
        if (itemnumber>0){
        title.setText(entrylist.get(itemnumber-1).getNotificationTitle());
        message.setText(entrylist.get(itemnumber-1).getNotificationMessage());
        timestamp.setText(entrylist.get(itemnumber-1).getTimestamp());}
        else{

            title.setText("Nothing here yet!");
            message.setText("It looks like you have not received any notification yet!");
            timestamp.setText("N/A");

        }

        LinearLayout l1 = (LinearLayout)rootView.findViewById(R.id.notification_receiver_layout_recent);
        ImageView icon = (ImageView)rootView.findViewById(R.id.notification_receiver_recent_icon);

        if (entrylist.get(itemnumber-1).getNotificationTopic().equals(GCMConstants.GCM_TOPIC_jc)){
            l1.setBackgroundColor(rootView.getResources().getColor(R.color.user_log_topic_jc));
            icon.setBackgroundResource(R.drawable.icon_jc);
        }
        else if (entrylist.get(itemnumber-1).getNotificationTopic().equals(GCMConstants.GCM_TOPIC_student)){
            l1.setBackgroundColor(rootView.getResources().getColor(R.color.user_log_topic_student));
            icon.setBackgroundResource(R.drawable.icon_students);
        }
        else if (entrylist.get(itemnumber-1).getNotificationTopic().equals(GCMConstants.GCM_TOPIC_TALK)){
            l1.setBackgroundColor(rootView.getResources().getColor(R.color.user_log_topic_talk));
            icon.setBackgroundResource(R.drawable.icon_research_talk);
        }


        return rootView;
    }
}
