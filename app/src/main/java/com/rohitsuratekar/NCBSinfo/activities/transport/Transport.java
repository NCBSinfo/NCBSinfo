package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.OnlineHome;
import com.rohitsuratekar.NCBSinfo.activities.transport.reminder.TransportReminder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteBuilder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportRoute;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import java.util.List;

public class Transport extends BaseActivity {

    public static final String INDENT = "transportIntent";
    private final String TAG = getClass().getSimpleName();
    private int routeNo;
    FloatingActionButton fab;
    Preferences pref;

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.TRANSPORT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pref = new Preferences(getBaseContext());

        fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setImageResource(R.drawable.icon_set_reminder);

        List<AlarmModel> allList = new TransportHelper().getAllReminders(getBaseContext());
        if (allList.size() == 0 || pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
            fab.setVisibility(View.GONE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TransportRoute route = new RouteBuilder(getCurrentFragment(routeNo), getBaseContext()).build();
                Intent intent = new Intent(Transport.this, TransportReminder.class);
                intent.putExtra(TransportReminder.ROUTE, route.getRouteNo());
                intent.putExtra(TransportReminder.ROUTE_TIME, route.getDynamics().getNextTripString());
                intent.putExtra(TransportReminder.SWITCH, "1");
                startActivity(intent);
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
            }
        });

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.base_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

            //We need to know current fragment to assign default value for floating button
            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    routeNo = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        }

        Intent intent = getIntent();
        String intentRoute = intent.getStringExtra(INDENT);
        if (intentRoute != null) {
            int currentInt = Integer.parseInt(intentRoute);
            if (currentInt > Routes.BUGGY_FROM_NCBS.getRouteNo()) {
                currentInt = currentInt - 1;
            }  //Remove extra buggy pointer
            TabLayout.Tab tab = tabLayout.getTabAt(currentInt);
            assert tab != null;
            tab.select();
        }

        if (!pref.transport().isReminded() && !pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Introducing " + getString(R.string.transport_reminder))
                    .setMessage(getString(R.string.transport_reminder_dialog))
                    .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setIcon(R.drawable.icon_shuttle)
                    .show();
        }
        pref.transport().setReminded();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return TransportFragment.newInstance(Routes.NCBS_IISC.getRouteNo());
                case 1:
                    return TransportFragment.newInstance(Routes.IISC_NCBS.getRouteNo());
                case 2:
                    return TransportFragment.newInstance(Routes.NCBS_MANDARA.getRouteNo());
                case 3:
                    return TransportFragment.newInstance(Routes.MANDARA_NCBS.getRouteNo());
                case 4:
                    return TransportFragment.newInstance(Routes.BUGGY_FROM_NCBS.getRouteNo()); //This is buggy and also 6
                case 5:
                    return TransportFragment.newInstance(Routes.NCBS_ICTS.getRouteNo());
                case 6:
                    return TransportFragment.newInstance(Routes.ICTS_NCBS.getRouteNo());
                case 7:
                    return TransportFragment.newInstance(Routes.NCBS_CBL.getRouteNo());
                default:
                    return TransportFragment.newInstance(Routes.NCBS_IISC.getRouteNo());
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getResources().getString(R.string.route_ncbs_iisc);
                case 1:
                    return getResources().getString(R.string.route_iisc_ncbs);
                case 2:
                    return getResources().getString(R.string.route_ncbs_mandara);
                case 3:
                    return getResources().getString(R.string.route_mandara_ncbs);
                case 4:
                    return getResources().getString(R.string.buggy);
                case 5:
                    return getResources().getString(R.string.route_ncbs_icts);
                case 6:
                    return getResources().getString(R.string.route_icts_ncbs);
                case 7:
                    return getResources().getString(R.string.route_ncbs_cbl);

            }
            return null;
        }
    }


    private Routes getCurrentFragment(int position) {

        switch (position) {
            case 0:
                return Routes.NCBS_IISC;
            case 1:
                return Routes.IISC_NCBS;
            case 2:
                return Routes.NCBS_MANDARA;
            case 3:
                return Routes.MANDARA_NCBS;
            case 4:
                return Routes.BUGGY_FROM_NCBS; //This is buggy and also 6
            case 5:
                return Routes.NCBS_ICTS;
            case 6:
                return Routes.ICTS_NCBS;
            case 7:
                return Routes.NCBS_CBL;
            default:
                return Routes.NCBS_IISC;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new TransportHelper().getAllReminders(getBaseContext()).size() == 0) {
            fab.setVisibility(View.GONE);
        }
    }

    //This is required because transport reminders will open this activity

    @Override
    public void onBackPressed() {
        if (isTaskRoot()) {
            Intent intent = new Intent(Transport.this, OnlineHome.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
        } else {
            super.onBackPressed();
        }
    }
}
