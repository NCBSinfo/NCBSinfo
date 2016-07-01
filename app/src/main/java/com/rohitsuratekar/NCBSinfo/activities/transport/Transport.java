package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class Transport extends BaseActivity {

    public static final String INDENT = "transportIntent";
    private final String TAG = getClass().getSimpleName();

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.TRANSPORT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setVisibility(View.GONE);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        ViewPager mViewPager = (ViewPager) findViewById(R.id.base_viewpager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (mViewPager != null) {
            mViewPager.setAdapter(mSectionsPagerAdapter);
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

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
                    return TransportFragment.newInstance(Routes.NCBS_IISC);
                case 1:
                    return TransportFragment.newInstance(Routes.IISC_NCBS);
                case 2:
                    return TransportFragment.newInstance(Routes.NCBS_MANDARA);
                case 3:
                    return TransportFragment.newInstance(Routes.MANDARA_NCBS);
                case 4:
                    return TransportFragment.newInstance(Routes.BUGGY_FROM_NCBS); //This is buggy and also 6
                case 5:
                    return TransportFragment.newInstance(Routes.NCBS_ICTS);
                case 6:
                    return TransportFragment.newInstance(Routes.ICTS_NCBS);
                case 7:
                    return TransportFragment.newInstance(Routes.NCBS_CBL);
                default:
                    return TransportFragment.newInstance(Routes.NCBS_IISC);
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
}
