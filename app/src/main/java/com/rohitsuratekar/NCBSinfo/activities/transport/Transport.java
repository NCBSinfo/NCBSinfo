package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.AppState;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.common.CurrentActivity;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import butterknife.ButterKnife;

public class Transport extends BaseActivity {


    private AppState state = AppState.getInstance();
    private ViewpagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSectionsPagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());

        for (int i = 0; i < state.getRouteList().size(); i++) {
            mSectionsPagerAdapter.addFragment(TransportFragment.newInstance(i),
                    getString(R.string.home_card_route_name,
                            state.getRouteList().get(i).getOrigin(),
                            state.getRouteList().get(i).getDestination()));
        }


        TabLayout tabLayout = ButterKnife.findById(this, R.id.transport_tabs);
        mViewPager = ButterKnife.findById(this, R.id.transport_fragment);

        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.TRANSPORT;
    }
}
