package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Locations extends BaseActivity {

    @BindView(R.id.loc_recycler)
    RecyclerView recyclerView;

    private LocationAdapter adaper;
    private List<LocationModel> locationModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        locationModels = new LocationList().getLocations();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        adaper = new LocationAdapter(locationModels);
        recyclerView.setAdapter(adaper);
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.LOCATIONS;
    }
}
