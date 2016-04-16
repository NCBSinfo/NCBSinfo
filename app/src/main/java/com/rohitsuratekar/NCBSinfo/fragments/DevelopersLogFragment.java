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
import com.rohitsuratekar.NCBSinfo.adapters.DevelopersLogAdapter;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.models.LogModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DevelopersLogFragment extends Fragment {
    RecyclerView recyclerView;
    List<LogModel> entrylist = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.developers_log, container, false);
        Database db = new Database(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.developers_log_recycleview);
        entrylist = db.getAllLogEntries();
        Collections.reverse(entrylist);
        DevelopersLogAdapter log_adapter= new DevelopersLogAdapter(entrylist);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(log_adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        db.close();
        return rootView;
    }
}