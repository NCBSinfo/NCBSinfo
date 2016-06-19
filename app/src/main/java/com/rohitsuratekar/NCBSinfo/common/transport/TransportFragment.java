package com.rohitsuratekar.NCBSinfo.common.transport;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.common.transport.models.BuggyModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.ShuttleModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.SundayModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.WeekDayModel;

import java.util.Arrays;
import java.util.Calendar;

public class TransportFragment extends Fragment {

    /**
     * @param index   : This is route Number (See Transport Helpers to know route numbers)
     * @param isBuggy : This will be true if current transport is buggy
     * @return : Will return fragment containing all trips and next trip marked
     */
    public static TransportFragment newInstance(int index, boolean isBuggy) {
        TransportFragment fragment = new TransportFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putBoolean("isBuggy", isBuggy);
        fragment.setArguments(args);
        return fragment;
    }

    public TransportFragment() {
    }

    TransportModel transport;
    ShuttleModel shuttle;
    BuggyModel buggy;
    WeekDayModel weekday;
    SundayModel sunday;
    TextView weekTitle, sundayTitle, footnote1, footnote2;
    boolean isBuggy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle args = getArguments();
        int index = args.getInt("index", 0);
        isBuggy = args.getBoolean("isBuggy", false);

        if (isBuggy) {
            buggy = new BuggyModel(
                    new Utilities().stringToarray(pref.getString(new TransportHelper().routeToStrings(index)[3], getString(R.string.def_buggy_from_ncbs))),
                    new Utilities().stringToarray(pref.getString(new TransportHelper().routeToStrings(index)[2], getString(R.string.def_buggy_from_mandara)))
            );
            transport = new TransportModel(getContext(), buggy);
        } else {
            weekday = new WeekDayModel(Arrays.asList(new Utilities().stringToarray(pref.getString(new TransportHelper().routeToStrings(index)[3], getString(R.string.def_ncbs_iisc_week)))));
            sunday = new SundayModel(Arrays.asList(new Utilities().stringToarray(pref.getString(new TransportHelper().routeToStrings(index)[2], getString(R.string.def_ncbs_iisc_week)))));
            shuttle = new ShuttleModel(index, sunday, weekday);
            transport = new TransportModel(getContext(), shuttle);
        }

        View rootView = inflater.inflate(R.layout.transport_list, container, false);
        weekTitle = (TextView) rootView.findViewById(R.id.weekday_trip_title);
        sundayTitle = (TextView) rootView.findViewById(R.id.sunday_trip_title);
        footnote1 = (TextView) rootView.findViewById(R.id.transport_footnote1);
        footnote2 = (TextView) rootView.findViewById(R.id.transport_footnote2);
        perform(rootView);
        return rootView;
    }

    public void perform(View v) {

        //UI initialization
        ListView weekList = (ListView) v.findViewById(R.id.weekdays_trips);
        ListView sundayList = (ListView) v.findViewById(R.id.sunday_trips);

        //Get raw trips
        String[] rawWeekTrips = transport.getRawTripsWeekDays();
        String[] rawSundayTrips = transport.getRawTripsSunday();

        //Convert to regular format
        rawWeekTrips = new TransportHelper().convertToSimpleDate(rawWeekTrips);
        rawSundayTrips = new TransportHelper().convertToSimpleDate(rawSundayTrips);

        if (isBuggy) {
            String[] nextTrips = buggy.getNextTrip();

            for (int i = 0; i < rawWeekTrips.length; i++) {
                if (rawWeekTrips[i].equals(new TransportHelper().convertToSimpleDate(nextTrips[0]))) {
                    rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                    break;
                }
            }

            for (int i = 0; i < rawSundayTrips.length; i++) {
                if (rawSundayTrips[i].equals(new TransportHelper().convertToSimpleDate(nextTrips[1]))) {
                    rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                    break;
                }
            }
        } else {
            String[] nextTrips = shuttle.getNextTrip();
            String targetString = new TransportHelper().convertToSimpleDate(nextTrips[1]);
            if (nextTrips[0].equals(String.valueOf(Calendar.SUNDAY))) {
                boolean gotDate = false;
                for (int i = 0; i < rawSundayTrips.length; i++) {
                    if (rawSundayTrips[i].equals(targetString)) {
                        rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                        gotDate = true;
                        break;
                    }
                }
                if (!gotDate) {
                    for (int i = 0; i < rawWeekTrips.length; i++) {
                        if (rawWeekTrips[i].equals(targetString)) {
                            rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                            break;
                        }
                    }
                }
            } else {
                boolean gotDate = false;
                for (int i = 0; i < rawWeekTrips.length; i++) {
                    if (rawWeekTrips[i].equals(targetString)) {
                        rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                        gotDate = true;
                        break;
                    }
                }
                if (!gotDate) {
                    for (int i = 0; i < rawSundayTrips.length; i++) {
                        if (rawSundayTrips[i].equals(targetString)) {
                            rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                            break;
                        }
                    }
                }
            } //Else Sunday
        } //Else isBuggy

        TransportAdapter weekAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, rawWeekTrips);
        TransportAdapter sundayAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, rawSundayTrips);
        sundayList.setAdapter(sundayAdapter);
        weekList.setAdapter(weekAdapter);
        weekTitle.setText(transport.getWeekTitle());
        sundayTitle.setText(transport.getSundayTitle());
        footnote1.setText(transport.getFootnote1());
        footnote2.setText(transport.getFootnote2());

    }

    private String coloredText(String string) {
        return "<font color=\"red\">" + string + "**</font>";
    }

}
