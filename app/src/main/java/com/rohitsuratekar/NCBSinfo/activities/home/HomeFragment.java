package com.rohitsuratekar.NCBSinfo.activities.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 11-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class HomeFragment extends Fragment {

    private static String ORIGIN = "origin";
    private static String DESTINATION = "destination";
    private static String TYPE = "type";

    public static HomeFragment newInstance(String origin, String destination, String type) {
        HomeFragment myFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ORIGIN, origin);
        args.putString(DESTINATION, destination);
        args.putString(TYPE, type);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);
        ImageView image = ButterKnife.findById(rootView, R.id.home_fragment_image);
        TextView name = ButterKnife.findById(rootView, R.id.home_fragment_name);
        TextView subName = ButterKnife.findById(rootView, R.id.home_fragment_type);
        image.setImageResource(R.drawable.home_image_blank);
        if (getArguments() != null) {
            String origin = getArguments().getString(ORIGIN).toUpperCase();
            String destination = getArguments().getString(DESTINATION).toUpperCase();
            String type = getArguments().getString(TYPE);
            image.setImageResource(getImageResource(destination));
            name.setText(getString(R.string.home_route_name, origin, destination));
            subName.setText(type);
        }
        return rootView;
    }

    private int getImageResource(String destination) {
        switch (destination.trim().toLowerCase()) {
            case "ncbs":
                return R.drawable.home_image_ncbs;
            case "iisc":
                return R.drawable.home_image_iisc;
            case "mandara":
                return R.drawable.home_image_mandara;
            default:
                return R.drawable.home_image_blank;
        }
    }

}
