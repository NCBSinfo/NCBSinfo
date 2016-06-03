package com.rohitsuratekar.NCBSinfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.adapters.ExternalAdapter;
import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.database.ExternalData;
import com.rohitsuratekar.NCBSinfo.helpers.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.models.ExternalModel;

import java.util.ArrayList;
import java.util.List;

public class ExternalFragment extends Fragment {
    RecyclerView recyclerView;
    List<ExternalModel> fullList;
    ExternalAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_update_log, container, false); //Just need one recycler view. Can be taken from any fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_log_recycleview);
        fullList = new ExternalData(getContext()).getAll();
        List<ExternalModel> filteredList = new ArrayList<>();
        for (ExternalModel item : fullList){
            if(item.getCode().equals(ExternalConstants.CONFERENCE_CAMP2016)){
                filteredList.add(item);
            }
        }
        adapter = new ExternalAdapter(filteredList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        return rootView;
    }
}
