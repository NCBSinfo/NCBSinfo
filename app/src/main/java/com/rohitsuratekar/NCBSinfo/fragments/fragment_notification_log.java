package com.rohitsuratekar.NCBSinfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.adapters.adapters_userNotificationLog;
import com.rohitsuratekar.NCBSinfo.helper.helper_contact_divideritemdecoratio;
import com.rohitsuratekar.NCBSinfo.models.models_userNotifications;

import java.util.ArrayList;
import java.util.List;


public class fragment_notification_log extends Fragment {

    RecyclerView recyclerView;
    List<models_userNotifications> entrylist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_notificationreciever_log, container, false);

        DatabaseHelper db = new DatabaseHelper(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.User_logList);
        entrylist = db.getAllEntries();
        adapters_userNotificationLog log_adapter= new adapters_userNotificationLog(entrylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(log_adapter);
        RecyclerView.ItemDecoration itemDecoration =
                new helper_contact_divideritemdecoratio(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        db.close();

        return rootView;
    }
}
