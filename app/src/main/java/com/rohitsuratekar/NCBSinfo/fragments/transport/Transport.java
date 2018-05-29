package com.rohitsuratekar.NCBSinfo.fragments.transport;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.BaseActivity;
import com.rohitsuratekar.NCBSinfo.R;

import java.text.ParseException;
import java.util.ArrayList;
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
    @BindView(R.id.tp_origin)
    TextView origin;
    @BindView(R.id.tp_destination)
    TextView destination;
    @BindView(R.id.tp_type)
    TextView type;
    @BindView(R.id.tp_swap)
    ImageView swapBtn;

    private Calendar calendar;
    private List<TransportDetails> transportList;
    private TransportDetails transport;
    private List<String> tripList;
    private TransportAdapter adapter;
    private boolean showActual = true;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport, container, false);
        ButterKnife.bind(this, rootView);
        calendar = Calendar.getInstance();
        tripList = new ArrayList<>();
        if (getActivity() != null) {
            transportList = ((BaseActivity) getActivity()).getTransportList();
            transport = transportList.get(0);
            tripList.addAll(transport.getTrips(calendar));
            try {
                showActual = true;
                String[] d = transport.getNextTripDetails(Calendar.getInstance());
                transport.setOriginalTrip(d);
                Calendar cal = Calendar.getInstance();
                switch (d[1]) {
                    case "2":
                        cal.add(Calendar.DATE, 1);
                        transport.setOriginalDay(cal.get(Calendar.DAY_OF_WEEK));
                        break;
                    case "-1":
                        cal.add(Calendar.DATE, -1);
                        transport.setOriginalDay(cal.get(Calendar.DAY_OF_WEEK));
                        break;
                    default:
                        transport.setOriginalDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                        break;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            adapter = new TransportAdapter(tripList, -1);
            updateUI();
        }
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        calendar = Calendar.getInstance();
        daySelected(dayList.get(calendar.get(Calendar.DAY_OF_WEEK) - 1));
        return rootView;
    }

    private void updateUI() {
        tripList.clear();
        tripList.addAll(transport.getTrips(calendar));
        if (calendar.get(Calendar.DAY_OF_WEEK) == transport.getOriginalDay()) {
            adapter.updateNext(tripList.indexOf(transport.getOriginalTrip()[0]));
        } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                && transport.getOriginalTrip()[1].equals("2")) {
            if (showActual) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                //  onDayClick(daysList.get(c.get(Calendar.DAY_OF_WEEK) - 1));
                showActual = false;
            } else {
                adapter.updateNext(tripList.size());
            }
        } else {
            adapter.updateNext(-1);
        }
        adapter.updateMessage(transport.getType());
        adapter.notifyDataSetChanged();

        origin.setText(transport.getOrigin().toUpperCase());
        destination.setText(transport.getDestination().toUpperCase());
        type.setText(transport.getType());

        if (transport.isReturnAvailable()) {
            swapBtn.setVisibility(View.VISIBLE);
        } else {
            swapBtn.setVisibility(View.INVISIBLE);
        }

        //Scroll to next trip
        //recyclerView.smoothScrollToPosition(adapter.getScrollPosition());

    }

    @OnClick({R.id.tp_day_mon, R.id.tp_day_tue, R.id.tp_day_wed, R.id.tp_day_thu, R.id.tp_day_fri, R.id.tp_day_sat, R.id.tp_day_sun})
    public void daySelected(TextView textView) {

        calendar.set(Calendar.DAY_OF_WEEK, dayList.indexOf(textView) + 1);
        if (transport != null) {
            updateUI();
        }

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
