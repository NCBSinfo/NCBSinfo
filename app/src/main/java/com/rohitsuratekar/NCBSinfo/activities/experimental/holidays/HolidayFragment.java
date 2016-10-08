package com.rohitsuratekar.NCBSinfo.activities.experimental.holidays;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 06-07-16.
 */
public class HolidayFragment extends Fragment {

    //Public
    public static String BUNDLE = "bundleFragment";

    RecyclerView recyclerView;
    List<HolidayModel> holidayList = new ArrayList<>();
    List<HolidayModel> refined_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.holidays, container, false);
        Bundle bundle = this.getArguments();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.holiday_recyclerView);

        for (String[] s : new Holidays().HolidayList()) {
            holidayList.add(new HolidayModel(s[0], s[1], s[2]));
        }


        Collections.sort(holidayList, new Comparator<HolidayModel>() {
            @Override
            public int compare(HolidayModel lhs, HolidayModel rhs) {
                return lhs.getDate().compareTo(rhs.getDate());
            }
        });

        if (bundle.getString(BUNDLE) != null) {
            if (bundle.getString(BUNDLE).equals("1")) {
                upcoming();
            } else {
                past();
            }
        }


        HolidayAdapter holiday_adapter = new HolidayAdapter(refined_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(holiday_adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        holiday_adapter.setOnItemClickListener(new HolidayAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(refined_list.get(position).getDate());
                DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
                dialog.show();
            }
        });
        return rootView;
    }

    public void upcoming() {
        for (HolidayModel entry : holidayList) {
            Date eventDateTime = entry.getDate();
            if ((eventDateTime.getTime() - Calendar.getInstance().getTime().getTime()) > 0) {
                refined_list.add(entry);
            }
        }
    }

    public void past() {
        for (HolidayModel entry : holidayList) {
            Date eventDateTime = entry.getDate();
            if ((eventDateTime.getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
                refined_list.add(entry);
            }
        }

        Collections.reverse(refined_list);

    }


}