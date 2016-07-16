package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments.DashboardAccount;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments.DashboardNotifications;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.ui.ViewpagerAdapter;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class DashBoard extends BaseActivity {

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.DASHBOARD;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setVisibility(View.GONE);

        ViewPager viewPager = (ViewPager) findViewById(R.id.base_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardAccount(), "Account");
        adapter.addFragment(new DashboardNotifications(), "Notifications");
        viewPager.setAdapter(adapter);
    }

}
