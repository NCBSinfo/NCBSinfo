package com.rohitsuratekar.NCBSinfo.fragments.transport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Transport extends Fragment {

    public Transport() {
    }

    @BindView(R.id.tp_recycler)
    RecyclerView recyclerView;
    @BindViews({R.id.tp_day_sun, R.id.tp_day_mon, R.id.tp_day_tue, R.id.tp_day_wed, R.id.tp_day_thu, R.id.tp_day_fri, R.id.tp_day_sat})
    List<TextView> dayList;
    @BindViews({R.id.tp_link_0, R.id.tp_link_1, R.id.tp_link_2, R.id.tp_link_3, R.id.tp_link_4, R.id.tp_link_5, R.id.tp_link_6})
    List<ImageView> linkList;

    private Calendar calendar;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport, container, false);
        ButterKnife.bind(this, rootView);
        recyclerView.setAdapter(new TransportAdapter(3));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        calendar = Calendar.getInstance();
        daySelected(dayList.get(calendar.get(Calendar.DAY_OF_WEEK) - 1));
        return rootView;
    }

    @OnClick({R.id.tp_day_mon, R.id.tp_day_tue, R.id.tp_day_wed, R.id.tp_day_thu, R.id.tp_day_fri, R.id.tp_day_sat, R.id.tp_day_sun})
    public void daySelected(TextView textView) {
        if (getContext() != null) {
            resetLists();
            for (int i = 0; i < dayList.size(); i++) {
                if (i == dayList.indexOf(textView)) {
                    textView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
                }
                if (i < dayList.indexOf(textView) + 1) {
                    linkList.get(i).setImageResource(R.color.colorPrimary);
                }
            }
        }
    }

    private void resetLists() {
        if (getContext() != null) {
            for (TextView t : dayList) {
                t.setTextColor(ContextCompat.getColor(getContext(), android.R.color.primary_text_light));
                t.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorLight));
            }
            for (ImageView i : linkList) {
                i.setImageResource(android.R.color.transparent);
            }
        }
    }
}
