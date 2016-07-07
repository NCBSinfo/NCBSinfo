package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.contacts.ContactListFragment;
import com.rohitsuratekar.NCBSinfo.activities.contacts.PinnedContactsFragment;
import com.rohitsuratekar.NCBSinfo.activities.events.EventsListFragment;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.ui.ViewpagerAdapter;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 06-07-16.
 */
public class TransportReminder extends BaseActivity {

    public static final String ROUTE = "transportReminderRoute";
    public static final String ROUTE_TIME = "transportReminderTime";

    public final String TAG = getClass().getSimpleName();


    int routeNo;
    String routeTime;

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.TRANSPORT_REMINDER;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.base_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setVisibility(View.GONE);

        Intent intent = getIntent();
        routeNo = intent.getIntExtra(ROUTE, 0);
        routeTime = intent.getStringExtra(ROUTE_TIME);


        //Set up pager after we get intent values
        setupViewPager(viewPager);

    }

    private void setupViewPager(ViewPager viewPager) {

        Bundle bundle1 = new Bundle();
        bundle1.putInt(SetReminderFragment.BUNDLE_ROUTE, routeNo);
        bundle1.putString(SetReminderFragment.BUNDLE_TIME, routeTime);
        SetReminderFragment setReminder = new SetReminderFragment();
        setReminder.setArguments(bundle1);

        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(setReminder, "Set Reminder");
        adapter.addFragment(new ContactListFragment(), "All reminders");
        viewPager.setAdapter(adapter);
    }
}
