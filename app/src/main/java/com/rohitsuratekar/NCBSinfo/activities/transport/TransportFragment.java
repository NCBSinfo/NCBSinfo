package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportFragment extends BottomSheetDialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_sheet, container, false);
        RecyclerView recyclerView = ButterKnife.findById(rootView, R.id.tp_sheet_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(rootView.getContext(), 2));
        recyclerView.setAdapter(new TransportSheetAdapter());
        return rootView;
    }
}

