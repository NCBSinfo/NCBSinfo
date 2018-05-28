package com.rohitsuratekar.NCBSinfo.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rohitsuratekar.NCBSinfo.Helper;
import com.rohitsuratekar.NCBSinfo.R;

import java.util.Calendar;

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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home, container, false);
        ButterKnife.bind(this, rootView);
        adjustGraphics();
        return rootView;
    }

    private void adjustGraphics() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour > 5 && hour < 19) {
            stars.setVisibility(View.GONE);
        } else {
            sun.setImageResource(R.drawable.graphics_moon);
            stars.setVisibility(View.VISIBLE);
            stars.setRotation(Helper.randomInt(0, 360));
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
