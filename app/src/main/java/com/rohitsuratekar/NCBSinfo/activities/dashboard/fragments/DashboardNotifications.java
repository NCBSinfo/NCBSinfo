package com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.NotificationAdapter;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;

import java.util.Collections;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 13-07-16.
 */
public class DashboardNotifications extends Fragment {

    RecyclerView recyclerView;
    List<NotificationModel> fullList;
    NotificationAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.base_fragment, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.base_recyclerView);
        fullList = new NotificationData(getContext()).getAll();
        Collections.reverse(fullList);
        adapter = new NotificationAdapter(fullList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new NotificationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.notification_viewer);
                dialog.setCanceledOnTouchOutside(true);
                TextView NoteTitle = (TextView) dialog.findViewById(R.id.notificationViewer_title);
                TextView NoteMessage = (TextView) dialog.findViewById(R.id.notificationViewer_message);
                TextView NoteTimestamp = (TextView) dialog.findViewById(R.id.notificationViewer_timestamp);
                String title = fullList.get(position).getTitle();
                if (title.length() < 100) {
                    //This is to keep width of dialog long enough
                    title = String.format("%1$-" + (100 - title.length()) + "s", title);
                }
                NoteMessage.setText(fullList.get(position).getMessage());
                NoteTitle.setText(title);
                NoteTimestamp.setText(fullList.get(position).getTimestamp());
                dialog.show();

            }
        });


        return rootView;
    }
}
