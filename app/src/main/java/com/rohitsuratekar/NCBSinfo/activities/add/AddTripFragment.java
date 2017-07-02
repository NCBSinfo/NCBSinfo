package com.rohitsuratekar.NCBSinfo.activities.add;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 24-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class AddTripFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_trip_details, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
