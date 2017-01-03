package com.rohitsuratekar.NCBSinfo.activities.transport.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.SelectFirstAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectFirstTripFragment extends Fragment {

    @BindView(R.id.tp_fragment_select_first_note)
    TextView note;
    @BindView(R.id.tp_fragment_select_first_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.tp_fragment_select_first_button)
    Button changeDay;

    private SelectFirstAdapter adapter;
    private List<String> items;
    sendFirstItem sendItem;
    int firstTrip;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_select_first_trip, container, false);
        ButterKnife.bind(this, rootView);

        items = new ArrayList<>();

        adapter = new SelectFirstAdapter(items, false);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SelectFirstAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                adapter.itemSelected(position);
                adapter.notifyDataSetChanged();
                sendItem.getFirstItem(position);
                firstTrip = position;
                sendItem.isFirstTripSelected(true, firstTrip);
            }
        });


        return rootView;
    }

    public void setItems(List<String> items) {
        this.items.clear();
        for (String s : items) {
            this.items.add(s);
        }
        adapter.notifyDataSetChanged();
        if (items.size() == 0) {
            sendItem.isFirstTripSelected(false, firstTrip);
        }
    }

    public void setFirstTrip(int i) {
        this.firstTrip = i;
        adapter.itemSelected(firstTrip);
        adapter.notifyDataSetChanged();
        sendItem.isFirstTripSelected(false, firstTrip);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            sendItem = (SelectFirstTripFragment.sendFirstItem) context;
        } catch (ClassCastException e) {
            Log.e("Error", e.getLocalizedMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        items.clear();
        for (String s : ((TransportEdit) getActivity()).getCurrentTrips()) {
            items.add(s);
        }
        firstTrip = ((TransportEdit) getActivity()).getCurrentFirstTrip();
        adapter.itemSelected(firstTrip);
        adapter.notifyDataSetChanged();
        if (items.size() == 0) {
            sendItem.isFirstTripSelected(false, firstTrip);
        }
    }


    public interface sendFirstItem {
        public int getFirstItem(int f);

        public boolean isFirstTripSelected(boolean isIt, int currentSelection);
    }
}
