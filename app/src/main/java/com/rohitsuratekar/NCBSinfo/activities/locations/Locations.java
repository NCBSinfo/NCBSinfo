
package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.ramotion.foldingcell.FoldingCell;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Locations extends BaseActivity {

    @BindView(R.id.loc_recycler)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        LocationAdapter adaper = new LocationAdapter(new AllLocations().listHalls());
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adaper);
        adaper.unfoldCell(new LocationAdapter.unfoldCell() {
            @Override
            public void onItemClick(int position, View v) {
                FoldingCell cell = (FoldingCell) v.findViewById(R.id.recycler_folding);
                cell.toggle(false);
            }
        });
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.LOCATIONS;
    }
}
