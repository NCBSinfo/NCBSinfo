package com.rohitsuratekar.NCBSinfo.activities.transport.reminder;

import android.content.DialogInterface;
import android.content.Intent;
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

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 08-07-16.
 */
public class ReminderListFragment extends Fragment implements AlarmConstants {

    RecyclerView recyclerView;
    List<AlarmModel> alarmList = new ArrayList<>();
    List<AlarmModel> refined_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.base_fragment, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.base_recyclerView);

        alarmList = new AlarmData(getActivity()).getAll();

        for (AlarmModel alarm : alarmList) {
            if (alarm.getLevel().equals(alarmLevel.TRANSPORT.name())) {
                refined_list.add(alarm);
            }

        }

        final ReminderAdapter reminderAdapter = new ReminderAdapter(refined_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(reminderAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getActivity(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);

        reminderAdapter.setOnItemClickListener(new ReminderAdapter.ClickListener() {
            @Override
            public void onItemClick(final int position, View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Are you sure?")
                        .setMessage("You are about to cancel this reminder. You won't be able to get notification for this reminder")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(getContext(), Alarms.class);
                                intent.putExtra(Alarms.INTENT, alarmTriggers.DELETE_ALARM.name());
                                intent.putExtra(Alarms.ALARM_KEY, String.valueOf(refined_list.get(position).getId()));
                                getContext().sendBroadcast(intent);
                                refined_list.remove(position);
                                reminderAdapter.notifyItemRemoved(position);
                                reminderAdapter.notifyItemRangeChanged(position, reminderAdapter.getItemCount());

                            }
                        })
                        .setNegativeButton("Not sure", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        })
                        .setIcon(R.drawable.icon_warning)
                        .show();

            }
        });

        return rootView;
    }
}
