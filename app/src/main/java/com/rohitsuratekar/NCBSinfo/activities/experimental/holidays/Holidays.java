package com.rohitsuratekar.NCBSinfo.activities.experimental.holidays;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.rohitsuratekar.NCBSinfo.ui.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 06-07-16.
 */
public class Holidays extends BaseActivity {

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.HOLIDAYS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.base_viewpager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.base_fab_button);
        fab.setVisibility(View.GONE);
        setTitle(getResources().getString(R.string.holidays_welcome));

    }

    private void setupViewPager(ViewPager viewPager) {
        Bundle bundle1 = new Bundle();
        bundle1.putString(HolidayFragment.BUNDLE, "1");
        HolidayFragment Upcomingevents = new HolidayFragment();
        Upcomingevents.setArguments(bundle1);

        Bundle bundle2 = new Bundle();
        bundle2.putString(HolidayFragment.BUNDLE, "2");
        HolidayFragment pastEvents = new HolidayFragment();
        pastEvents.setArguments(bundle2);

        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Upcomingevents, "Upcoming");
        adapter.addFragment(pastEvents, "Past");
        viewPager.setAdapter(adapter);
    }


    public String[][] HolidayList() {
        return new String[][]{
                {"Makar Sankranti (Pongal)", "15/01/2016", "0"},
                {"Republic Day", "26/01/2016", "1"},
                {"Good Friday", "25/03/2016", "2"},
                {"Ugadi (Gudi Padava)", "08/04/2016", "3"},
                {"Mahavir Jayanti", "19/04/2016", "4"},
                {"Buddha Purnima", "21/05/2016", "5"},
                {"Eid-ul-fitr", "06/07/2016", "6"},
                {"Independence Day", "15/08/2016", "7"},
                {"Ganesh Chaturthi", "05/09/2016", "8"},
                {"Id-ul-Zuha (Bakra-eid)", "12/09/2016", "9"},
                {"Gandhi Jayanti", "02/10/2016", "10"},
                {"Dussehra", "11/10/2016", "11"},
                {"Muharram", "12/10/2016", "12"},
                {"Diwali", "30/10/2016", "13"},
                {"Guru Nanak Jayanti", "14/11/2016", "14"},
                {"Id-e-Milad", "12/12/2016", "15"},
                {"Christmas Day", "25/12/2016", "16"}
        };
    }

    public boolean isTodayHoliday() {

        List<HolidayModel> holidayList = new ArrayList<>();
        for (String[] s : HolidayList()) {
            holidayList.add(new HolidayModel(s[0], s[1], s[2]));
        }
        for (HolidayModel h : holidayList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(h.getDate());
            if (cal.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    public boolean isTomorrowHoliday() {

        List<HolidayModel> holidayList = new ArrayList<>();
        for (String[] s : HolidayList()) {
            holidayList.add(new HolidayModel(s[0], s[1], s[2]));
        }
        for (HolidayModel h : holidayList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(h.getDate());
            if (cal.get(Calendar.DAY_OF_YEAR) - 1 == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    public int whichHoliday() {
        List<HolidayModel> holidayList = new ArrayList<>();
        for (String[] s : HolidayList()) {
            holidayList.add(new HolidayModel(s[0], s[1], s[2]));
        }
        for (HolidayModel h : holidayList) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(h.getDate());
            if (cal.get(Calendar.DAY_OF_YEAR) - 1 == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                return h.getId();
            }
        }
        return 1989;
    }


}
