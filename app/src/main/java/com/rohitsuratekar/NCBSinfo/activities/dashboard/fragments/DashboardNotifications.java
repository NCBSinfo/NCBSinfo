package com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                showDialog(position);
            }
        });


        return rootView;
    }

    private void showDialog(final int position) {
        //Inflate layout
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.notification_viewer, null);

        TextView NoteTitle = (TextView) view.findViewById(R.id.notificationViewer_title);
        TextView NoteMessage = (TextView) view.findViewById(R.id.notificationViewer_message);
        TextView NoteTimestamp = (TextView) view.findViewById(R.id.notificationViewer_timestamp);

        NoteTitle.setText(fullList.get(position).getTitle());
        NoteMessage.setText(fullList.get(position).getMessage());
        NoteTimestamp.setText(fullList.get(position).getTimestamp());

        //Show dialog to add view
        new AlertDialog.Builder(getContext())
                .setView(view)
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton("delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new NotificationData(getContext()).delete(fullList.get(position));
                        fullList.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }
}
