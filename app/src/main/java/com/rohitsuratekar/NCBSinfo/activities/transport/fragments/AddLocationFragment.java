package com.rohitsuratekar.NCBSinfo.activities.transport.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MultiAutoCompleteTextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportEdit;

import butterknife.BindView;
import butterknife.ButterKnife;


public class AddLocationFragment extends Fragment {


    @BindView(R.id.tp_fragment_origin)
    MultiAutoCompleteTextView origin;
    @BindView(R.id.tp_fragment_destination)
    MultiAutoCompleteTextView destination;
    getData data;
    String loc_origin, loc_destination;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_add_location, container, false);
        ButterKnife.bind(this, rootView);
        origin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                loc_origin = s.toString();
                isComplete();


            }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                loc_destination = s.toString();
                isComplete();

            }
        });

        return rootView;

    }

    private void isComplete() {
        if (loc_origin != null && loc_destination != null) {
            if (loc_destination.trim().length() > 0 && loc_origin.trim().length() > 0) {
                data.addedLocation(loc_origin, loc_destination);
                data.isOriginSet(true);
            } else {
                data.isOriginSet(false);
            }
        } else {
            data.isOriginSet(false);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            data = (getData) context;
        } catch (ClassCastException e) {
            Log.e("Error", e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        loc_origin = ((TransportEdit) getActivity()).getCurrentOrigin();
        loc_destination = ((TransportEdit) getActivity()).getCurrentDestination();
        origin.setText(loc_origin);
        destination.setText(loc_destination);
    }

    public interface getData {
        public boolean isOriginSet(boolean isIt);

        public String addedLocation(String org, String des);
    }
}
