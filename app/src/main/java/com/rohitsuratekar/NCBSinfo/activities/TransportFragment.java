package com.rohitsuratekar.NCBSinfo.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * Created by Rohit Suratekar on 06-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportFragment extends BottomSheetDialogFragment {

    private static String INDEX = "index";
    private static String REVERSE = "reverseIndex";

    public static TransportFragment newInstance(int index, int reverseIndex) {
        TransportFragment myFragment = new TransportFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        args.putInt(REVERSE, reverseIndex);
        myFragment.setArguments(args);
        return myFragment;
    }

    private OnRouteSelected selected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_sheet, container, false);

        if (getActivity() instanceof Transport && getArguments() != null) {


        }


        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            selected = (OnRouteSelected) context;
        } catch (Exception e) {
            Toast.makeText(context, "attach fragment interface!", Toast.LENGTH_LONG).show();
        }

    }

    interface OnRouteSelected {
        void selected(int routeID);
    }
}