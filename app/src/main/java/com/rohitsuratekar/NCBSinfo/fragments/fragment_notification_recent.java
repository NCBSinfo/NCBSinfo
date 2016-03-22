package com.rohitsuratekar.NCBSinfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
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

        }


        return rootView;
    }
}
