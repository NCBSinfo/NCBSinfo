package com.rohitsuratekar.NCBSinfo.fragments.home;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.BaseActivity;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.fragments.transport.TransportDetails;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends Fragment {

    public Home() {
    }

    @BindView(R.id.hm_sun)
    ImageView sun;
    @BindView(R.id.hm_graphics)
    ImageView scene;
    @BindView(R.id.hm_stars)
    ImageView stars;
    @BindView(R.id.hm_type)
    TextView type;
    @BindView(R.id.hm_origin)
    TextView origin;
    @BindView(R.id.hm_destination)
    TextView destination;
    @BindView(R.id.hm_time)
    TextView time;

    private int randRotation = 0;
    private List<TransportDetails> transportList;
    private TransportDetails transport;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        randRotation = Helper.randomInt(0, 360);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);
        ButterKnife.bind(this, rootView);
        if (getActivity() != null) {
            transportList = ((BaseActivity) getActivity()).getTransportList();
            changeTo(0);
        }
        adjustGraphics();
        return rootView;
    }

    private void changeTo(int index) {
        transport = transportList.get(index);
        origin.setText(transport.getOrigin().toUpperCase());
        destination.setText(transport.getDestination().toUpperCase());
        type.setText(getString(R.string.hm_next, transport.getType()));
        try {
            time.setText(Helper.displayTime(transport.getNextTripDetails(Calendar.getInstance())[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void adjustGraphics() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour > 5 && hour < 19) {
            stars.setVisibility(View.GONE);
        } else {
            sun.setImageResource(R.drawable.graphics_moon);
            stars.setVisibility(View.VISIBLE);
            stars.setRotation(randRotation);
        }
        float hour_factor = getResources().getDimensionPixelSize(R.dimen.home_image_height) / 12;
        hour_factor = hour_factor * hour;
        if (hour > 12) {
            hour_factor = hour_factor / 2;
        }
        sun.animate()
                .translationX(hour_factor)
                .setDuration(10).start();
    }
}
