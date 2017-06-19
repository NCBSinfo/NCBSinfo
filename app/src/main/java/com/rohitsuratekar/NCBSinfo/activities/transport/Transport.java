package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.RouteInfo;
import com.rohitsuratekar.NCBSinfo.activities.home.HomePageAdapter;
import com.rohitsuratekar.NCBSinfo.background.RouteViewModel;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class Transport extends BaseActivity {

    private RouteViewModel routeViewModel;
    private HomePageAdapter adapter;
    private List<RouteInfo> infoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport);
        setTitle(R.string.transport);
        routeViewModel = ViewModelProviders.of(this).get(RouteViewModel.class);

        adapter = new HomePageAdapter(getSupportFragmentManager());
        addFragments();
        ViewPager pager = ButterKnife.findById(this, R.id.transport_viewpager);
        TabLayout tabLayout = ButterKnife.findById(this, R.id.tabs);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);

    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_transport;
    }

    private void addFragments() {

        routeViewModel.getAllRoutes().observe(this, new Observer<List<RouteInfo>>() {
            @Override
            public void onChanged(@Nullable List<RouteInfo> infos) {
                if (infos != null) {
                    Log.inform(infos.size() + " routes loaded successfully");
                    infoList.clear();
                    infoList.addAll(infos);
                    for (RouteInfo r : infos) {
                        adapter.addFragment(TransportFragment.newInstance(infos.indexOf(r)),
                                r.getRoute().getOrigin() + "-" + r.getRoute().getDestination());
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    public List<RouteInfo> getInfoList() {
        return infoList;
    }
}
