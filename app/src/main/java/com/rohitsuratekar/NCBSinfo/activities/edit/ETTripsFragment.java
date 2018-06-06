package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.app.TimePickerDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.Helper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rohit Suratekar on 14-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETTripsFragment extends Fragment {

    private ETViewModel viewModel;
    private ETDataHolder holder;

    @BindView(R.id.et_trips_recycler)
    RecyclerView recyclerView;
    private ETAddTripAdapter adapter;
    private List<String> itemList;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_trips, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        itemList = new ArrayList<>();
        adapter = new ETAddTripAdapter(itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SwipeItemDecorator());
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(TouchHelper.get(getContext(), recyclerView));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        adapter.setUndoOn(false);
        holder = viewModel.getData().getValue();
        if (holder != null) {
            if (holder.getItemList().size() > 0) {
                itemList.addAll(holder.getItemList());
                adapter.notifyDataSetChanged();
            }
        }
        return rootView;
    }

    @OnClick(R.id.et_trips_pick_time)
    public void openPicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String currentItem = formatDate(selectedHour, selectedMinute);
                if (itemList.contains(currentItem)) {
                    Snackbar.make(recyclerView, getString(R.string.et_trips_exists_warning), Snackbar.LENGTH_SHORT).show();
                } else {
                    itemList.add(currentItem);
                    List<String> sorted = Helper.sortStringsByDate(itemList);
                    itemList.clear();
                    itemList.addAll(sorted);
                    holder.setItemList(itemList);
                    adapter.notifyDataSetChanged();
                }
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.show();
    }

    private String formatDate(int hour, int min) {
        String h = String.valueOf(hour);
        String m = String.valueOf(min);
        if (h.length() == 1) {
            h = "0" + String.valueOf(hour);
        }
        if (m.length() == 1) {
            m = "0" + String.valueOf(min);
        }
        return h + ":" + m;
    }

    @OnClick(R.id.et_trips_next)
    public void goNext() {
        holder.setItemList(itemList);
        viewModel.getCurrentStep().postValue(4);
    }

    @OnClick(R.id.et_trips_previous)
    public void goPrevious() {
        holder.setItemList(itemList);
        viewModel.getCurrentStep().postValue(2);
    }
}
