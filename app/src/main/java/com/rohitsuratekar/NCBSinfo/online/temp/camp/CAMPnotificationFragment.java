package com.rohitsuratekar.NCBSinfo.online.temp.camp;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.online.dashboard.NotificationAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CAMPnotificationFragment extends Fragment implements NetworkConstants {

    //Public
    RecyclerView recyclerView;
    List<NotificationModel> notificationList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.common_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.common_recycleview);

        notificationList = new NotificationData(getContext()).getAll();
        Collections.reverse(notificationList);

        NotificationAdapter adapter = new NotificationAdapter(notificationList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        adapter.setOnItemClickListener(new NotificationAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.notification_viewer);
                dialog.setCanceledOnTouchOutside(true);
                TextView NoteTitle = (TextView) dialog.findViewById(R.id.notificationViewer_title);
                TextView NoteMessage = (TextView) dialog.findViewById(R.id.notificationViewer_message);
                TextView NoteTimestamp = (TextView) dialog.findViewById(R.id.notificationViewer_timestamp);
                String title = notificationList.get(position).getTitle();
                if (title.length() < 100) {
                    //This is to keep width of dialog long enough
                    title = String.format("%1$-" + (100 - title.length()) + "s", title);
                }
                NoteMessage.setText(notificationList.get(position).getMessage());
                NoteTitle.setText(title);
                NoteTimestamp.setText(notificationList.get(position).getTimestamp());
                dialog.show();

            }
        });
        return rootView;
    }
}