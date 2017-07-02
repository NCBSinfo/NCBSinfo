package com.rohitsuratekar.NCBSinfo.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.ButterKnife;

/**
 * Created by Rohit on 7/2/2017.
 */

public class AddTripsFragment extends Fragment {

    private RecyclerView recyclerView;
    private AddTripsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_trips, container, false);
        recyclerView = ButterKnife.findById(rootView, R.id.et_trip_recycler);
        adapter = new AddTripsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SwipeItemDecorator());
        ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(TouchHelper.get(getContext(), recyclerView));
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        // ((AddTripsAdapter) recyclerView.getAdapter()).setUndoOn(true);
        return rootView;
    }
}
