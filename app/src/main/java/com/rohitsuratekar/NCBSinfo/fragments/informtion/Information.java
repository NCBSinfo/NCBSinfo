package com.rohitsuratekar.NCBSinfo.fragments.informtion;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.locations.Locations;
import com.rohitsuratekar.NCBSinfo.activities.manage.ManageTransport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Information extends Fragment implements InfoAdapter.OnInfoClick {

    static final int LOCATION = 0;
    static final int MANAGE_TRANSPORT = 1;

    @BindView(R.id.info_recycler)
    RecyclerView recyclerView;

    private List<InfoModel> modelList;


    public Information() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.information, container, false);
        ButterKnife.bind(this, rootView);
        setUpList();
        recyclerView.setAdapter(new InfoAdapter(modelList, this));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootView;
    }

    private void setUpList() {
        modelList = new ArrayList<>();
        modelList.add(new InfoModel(R.string.manage_transport, R.string.info_manage_transport_details,
                0, MANAGE_TRANSPORT));
        modelList.add(new InfoModel(R.string.locations, R.string.info_loc_details, 0, LOCATION));
    }

    @Override
    public void clicked(int actionID) {
        switch (actionID) {
            case LOCATION:
                startActivity(new Intent(getContext(), Locations.class));
                break;
            case MANAGE_TRANSPORT:
                startActivity(new Intent(getContext(), ManageTransport.class));
                break;

        }
    }

}
