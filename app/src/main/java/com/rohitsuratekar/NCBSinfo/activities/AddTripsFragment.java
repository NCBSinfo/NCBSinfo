package com.rohitsuratekar.NCBSinfo.activities;


import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.Log;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rohit on 7/2/2017.
 */

public class AddTripsFragment extends Fragment {

    @BindView(R.id.et_trip_recycler)
    RecyclerView recyclerView;
    private AddTripsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_trips, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new AddTripsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SwipeItemDecorator());
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(TouchHelper.get(getContext(), recyclerView));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setUndoOn(true);
        return rootView;
    }

    @OnClick(R.id.et_pick_time)
    public void openPicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.inform();
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();
    }
}
