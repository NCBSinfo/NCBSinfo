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
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportEdit;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.SelectFirstAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConfirmDetailsFragment extends Fragment {

    @BindView(R.id.tp_fragment_confirm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.tp_fragment_confirm_title)
    TextView title;
    @BindView(R.id.tp_fragment_confirm_subtitle)
    TextView subtitle;

    private SelectFirstAdapter adapter;
    private List<String> items;
    int firstItem;
    getConfirmation conf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_confirm_details, container, false);
        ButterKnife.bind(this, rootView);
        items = new ArrayList<>();
        adapter = new SelectFirstAdapter(items, false);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SelectFirstAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                conf.isConfirmed(true);
            }
        });
        changeList();
        return rootView;
    }

    public void setFirstItem(int i) {
        this.firstItem = i;
        changeList();
    }

    @Override
    public void onResume() {
        super.onResume();
        firstItem = ((TransportEdit) getActivity()).getCurrentFirstTrip();
        changeList();
    }

    private void changeList() {
        items.clear();
        for (String s : ((TransportEdit) getActivity()).getCurrentTrips()) {
            items.add(s);
        }

        if (firstItem != -1 && items.size() != 0) {
            List<String> temp1 = new ArrayList<>();
            for (int i = firstItem; i < items.size(); i++) {
                temp1.add(items.get(i));
            }
            for (int j = 0; j < firstItem; j++) {
                temp1.add(items.get(j));
            }
            items.clear();
            for (String s : temp1) {
                items.add(s);
            }
        }
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            conf = (ConfirmDetailsFragment.getConfirmation) context;
        } catch (ClassCastException e) {
            Log.e("Error", e.getLocalizedMessage());
        }
    }

    public interface getConfirmation{
        public boolean isConfirmed(boolean isIt);
    }
}
