package com.rohitsuratekar.NCBSinfo.activities.events;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.ui.ViewpagerAdapter;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class Events extends BaseActivity {

    public static final String EVENT_CODE = "event_code";

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.EVENTS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //If intent is received through notification, check for event and show dialog box
        Intent i = getIntent();
        String eventCode = i.getStringExtra(EVENT_CODE);
        if (eventCode != null) {
            TalkModel talk = new TalkData(getBaseContext()).getEntry(Integer.parseInt(eventCode));
            new EventsListFragment().showDialog(Events.this, talk);
            new Preferences(getBaseContext()).app().addNotificationOpened(); //Whenever user opens app with notifications
        }

        ViewPager viewPager = (ViewPager) findViewById(R.id.base_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setVisibility(View.GONE);
    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle1 = new Bundle();
        bundle1.putString(EventsListFragment.BUNDLE, "1");
        EventsListFragment Upcomingevents = new EventsListFragment();
        Upcomingevents.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString(EventsListFragment.BUNDLE, "2");
        EventsListFragment pastEvents = new EventsListFragment();
        pastEvents.setArguments(bundle2);

        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Upcomingevents, "Upcoming events");
        adapter.addFragment(pastEvents, "Past events");
        viewPager.setAdapter(adapter);
    }


    //Override this because this intent can be received through notifications.
    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(Events.this, OnlineHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
        } else {
            super.onBackPressed();
        }

    }

}
