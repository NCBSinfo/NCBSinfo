package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments.DashboardAccount;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.fragments.DashboardNotifications;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.ui.ViewpagerAdapter;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class DashBoard extends BaseActivity {

    public static final String INTENT = "dashBoard";

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.DASHBOARD;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        if (new Preferences(getBaseContext()).user().getUserType().equals(AppConstants.userType.OLD_USER)) {
            fab.setImageResource(R.drawable.icon_priority);
            fab.setVisibility(View.VISIBLE);
        } else {
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(DashBoard.this)
                        .setIcon(R.drawable.icon_warning)
                        .setTitle("Network Delay")
                        .setMessage(Html.fromHtml(getString(R.string.warning_network_delay)))
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.base_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position != 0) {
                    fab.setVisibility(View.GONE);
                } else {
                    if (new Preferences(getBaseContext()).user().getUserType().equals(AppConstants.userType.OLD_USER)) {
                        fab.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        Intent intent = getIntent();
        String fromNotification = intent.getStringExtra(INTENT);
        if (fromNotification != null) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            assert tab != null;
            tab.select();
            new Preferences(getBaseContext()).app().addNotificationOpened(); //Whenever user opens app with notifications
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DashboardAccount(), "Account");
        adapter.addFragment(new DashboardNotifications(), "Notifications");
        viewPager.setAdapter(adapter);
    }

}
