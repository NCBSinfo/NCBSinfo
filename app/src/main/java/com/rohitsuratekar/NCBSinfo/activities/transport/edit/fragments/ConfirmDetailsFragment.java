package com.rohitsuratekar.NCBSinfo.activities.transport.edit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.ButterKnife;

public class ConfirmDetailsFragment extends Fragment {


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_edit_confirm, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


}
