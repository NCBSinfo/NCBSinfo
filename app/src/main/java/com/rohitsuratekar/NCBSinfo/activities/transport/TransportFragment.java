package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.reminder.TransportReminder;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

import java.util.Calendar;

public class TransportFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    /**
     * @param routes : Current Route
     * @return : fragment
     */
    public static TransportFragment newInstance(Routes routes) {
        TransportFragment fragment = new TransportFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putString("route", routes.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public TransportFragment() {
    }

    TransportModel transport;
    Preferences pref;

    //UI elements

    TextView weekTitle, sundayTitle;
    TextView footnote1, footnote2, lastUpdated;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        pref = new Preferences(getContext());
        Bundle args = getArguments();
        String name = args.getString("route", Routes.NCBS_IISC.toString());


        if (name == null) {
            name = Routes.NCBS_IISC.toString();
        }


        transport = new TransportModel(Routes.valueOf(name), getContext());

        View rootView = inflater.inflate(R.layout.transport_list, container, false);

        weekTitle = (TextView) rootView.findViewById(R.id.weekday_trip_title);
        sundayTitle = (TextView) rootView.findViewById(R.id.sunday_trip_title);
        footnote1 = (TextView) rootView.findViewById(R.id.transport_footnote1);
        footnote2 = (TextView) rootView.findViewById(R.id.transport_footnote2);
        lastUpdated = (TextView) rootView.findViewById(R.id.transport_last_update);


        perform(rootView);


        return rootView;
    }


    public void perform(View v) {

        lastUpdated.setText(getString(R.string.transport_last_updated, pref.transport().getLastUpdate()));

        //UI initialization
        ListView weekList = (ListView) v.findViewById(R.id.weekdays_trips);
        ListView sundayList = (ListView) v.findViewById(R.id.sunday_trips);

        String[] rawWeekTrips;
        String[] rawSundayTrips;
        //Get raw trips
        if (transport.isBuggy()) {
            rawWeekTrips = pref.transport().getWeekdayTrips(Routes.BUGGY_FROM_NCBS);
            rawSundayTrips = pref.transport().getWeekdayTrips(Routes.BUGGY_FROM_MANDARA);
        } else {
            rawWeekTrips = transport.getRawTripsWeekDays();
            rawSundayTrips = transport.getRawTripsSunday();
        }

        final String[] weekListwithoutFormat = rawWeekTrips;
        final String[] sundayListwithoutFormat = rawSundayTrips;

        //Convert to regular format
        rawWeekTrips = new Converters().convertToSimpleDate(rawWeekTrips);
        rawSundayTrips = new Converters().convertToSimpleDate(rawSundayTrips);


        int focusPoint = 0;

        if (transport.isBuggy()) {

            for (int i = 0; i < rawWeekTrips.length; i++) {
                if (rawWeekTrips[i].equals(new Converters().convertToSimpleDate(
                        new TransportHelper(getContext()).nextTrip(Routes.BUGGY_FROM_NCBS)[1]
                ))) {
                    rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                    focusPoint = i;
                    break;
                }
            }

            for (int i = 0; i < rawSundayTrips.length; i++) {
                if (rawSundayTrips[i].equals(new Converters().convertToSimpleDate(
                        new TransportHelper(getContext()).nextTrip(Routes.BUGGY_FROM_MANDARA)[1]
                ))) {
                    rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                    focusPoint = i;
                    break;
                }
            }
        } else {

            String targetString = new Converters().convertToSimpleDate(transport.getNextTrip());
            if (transport.getNextTripDay() == Calendar.SUNDAY) {
                boolean gotDate = false;
                for (int i = 0; i < rawSundayTrips.length; i++) {
                    if (rawSundayTrips[i].equals(targetString)) {
                        rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                        focusPoint = i;
                        gotDate = true;
                        break;
                    }
                }
                if (!gotDate) {
                    for (int i = 0; i < rawWeekTrips.length; i++) {
                        if (rawWeekTrips[i].equals(targetString)) {
                            rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                            focusPoint = i;
                            break;
                        }
                    }
                }
            } else {
                boolean gotDate = false;
                for (int i = 0; i < rawWeekTrips.length; i++) {
                    if (rawWeekTrips[i].equals(targetString)) {
                        rawWeekTrips[i] = coloredText(rawWeekTrips[i]);
                        focusPoint = i;
                        gotDate = true;
                        break;
                    }
                }
                if (!gotDate) {
                    for (int i = 0; i < rawSundayTrips.length; i++) {
                        if (rawSundayTrips[i].equals(targetString)) {
                            rawSundayTrips[i] = coloredText(rawSundayTrips[i]);
                            focusPoint = i;
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
        weekTitle.setText(transport.getGetWeekTitle());
        sundayTitle.setText(transport.getGetSundayTitle());
        footnote1.setText(transport.getGetFootnote1());
        footnote2.setText(transport.getGetFootnote2());

        if (focusPoint > 3) { //3 is magic number but it works
            weekList.setSelection(focusPoint);
            sundayList.setSelection(focusPoint);
            weekList.smoothScrollToPositionFromTop(focusPoint, 0);
            sundayList.smoothScrollToPositionFromTop(focusPoint, 0);
        }

        weekList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                //No reminders for offline mode
                if (!pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
                    showWeek(weekListwithoutFormat[i], true);
                }
            }
        });

        sundayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //No reminders for offline mode
                if (!pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
                    showWeek(sundayListwithoutFormat[i], false);
                }

            }
        });


    }

    private String coloredText(String string) {
        return "<font color=\"red\">" + string + "**</font>";
    }


    private void showWeek(final String trip, final boolean isWeekDay) {

        String fromText = transport.getFrom().toUpperCase();
        String toText = transport.getTO().toUpperCase();

        if(transport.isBuggy()){
            if(!isWeekDay){
                fromText = Routes.BUGGY_FROM_MANDARA.getFrom().toUpperCase();
                toText = Routes.BUGGY_FROM_MANDARA.getTo().toUpperCase();
            }
        }


        new AlertDialog.Builder(getContext())
                .setTitle("Set reminder ?")
                .setMessage(Html.fromHtml("For " + transport.getType().toLowerCase() + "<br>from <b>" +
                        fromText + "</b> to <b>" +
                        toText + "</b> on " +
                        new Converters().convertToSimpleDate(trip)))
                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), TransportReminder.class);

                        if (transport.isBuggy()) {
                            if (isWeekDay) {
                                intent.putExtra(TransportReminder.ROUTE, Routes.BUGGY_FROM_NCBS.getRouteNo());
                            } else {
                                intent.putExtra(TransportReminder.ROUTE, Routes.BUGGY_FROM_MANDARA.getRouteNo());
                            }
                        } else {
                            intent.putExtra(TransportReminder.ROUTE, transport.getRouteNo());
                        }
                        if (isWeekDay) {
                            intent.putExtra(TransportReminder.ROUTE_DAY, "weekday");
                        } else {
                            intent.putExtra(TransportReminder.ROUTE_DAY, "sunday");
                        }
                        intent.putExtra(TransportReminder.ROUTE_TIME, trip);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setIcon(R.drawable.icon_shuttle)
                .show();
    }
}
