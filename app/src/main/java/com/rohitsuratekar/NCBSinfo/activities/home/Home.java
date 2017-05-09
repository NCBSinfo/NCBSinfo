package com.rohitsuratekar.NCBSinfo.activities.home;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.common.AppState;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.common.CurrentActivity;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import butterknife.ButterKnife;

public class Home extends BaseActivity implements HomeFragment.OnFragmentActions {

    private AppState state = AppState.getInstance();
    private ViewpagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSectionsPagerAdapter = new ViewpagerAdapter(getSupportFragmentManager());
        for (Route r : state.getRouteList()) {
            mSectionsPagerAdapter.addFragment(HomeFragment.newInstance(state.getRouteList().indexOf(r)), "Route");
        }
        mViewPager = ButterKnife.findById(this, R.id.home_fragment);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.HOME;
    }

    @Override
    public void changeActivity() {

    }
}
