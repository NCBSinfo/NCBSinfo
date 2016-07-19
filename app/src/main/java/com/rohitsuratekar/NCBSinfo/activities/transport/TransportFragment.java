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
import com.rohitsuratekar.NCBSinfo.activities.transport.reminder.TransportReminder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteBuilder;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.RouteInformation;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportRoute;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.Calendar;

public class TransportFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    /**
     * @param routeNo : Current Route No
     * @return : fragment
     */
    public static TransportFragment newInstance(int routeNo) {
        TransportFragment fragment = new TransportFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("route", routeNo);
        fragment.setArguments(args);
        return fragment;
    }

    public TransportFragment() {
    }

    TransportRoute transport;
    Preferences pref;
    BaseParameters baseParameters;

    //UI elements

    TextView weekTitle, sundayTitle;
    TextView footnote1, footnote2, lastUpdated;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        pref = new Preferences(getContext());
        baseParameters = new BaseParameters(getContext());
        Bundle args = getArguments();
        int routeNo = args.getInt("route", Routes.NCBS_IISC.getRouteNo());


        transport = new RouteBuilder(new TransportHelper().getRoute(routeNo), getContext()).build();

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
        TransportRoute ncbsBuggy = transport;
        TransportRoute mandaraBuggy = transport;
        //UI initialization
        ListView weekList = (ListView) v.findViewById(R.id.weekdays_trips);
        ListView sundayList = (ListView) v.findViewById(R.id.sunday_trips);

        String[] rawWeekTrips = transport.getTrips().getRawWeek().toArray(new String[transport.getTrips().getRawWeek().size()]);
        String[] rawSundayTrips = transport.getTrips().getRawSunday().toArray(new String[transport.getTrips().getRawSunday().size()]);

        if (transport.getRouteType().equals(Routes.type.BUGGY)) {
            ncbsBuggy = new RouteBuilder(Routes.BUGGY_FROM_NCBS, getContext()).build();
            mandaraBuggy = new RouteBuilder(Routes.BUGGY_FROM_MANDARA, getContext()).build();

            rawWeekTrips = ncbsBuggy.getTrips().getRawWeek().toArray(new String[ncbsBuggy.getTrips().getRawWeek().size()]);
            rawSundayTrips = mandaraBuggy.getTrips().getRawWeek().toArray(new String[mandaraBuggy.getTrips().getRawWeek().size()]);
        }

        String[] formattedWeek = rawWeekTrips;
        String[] formattedSunday = rawSundayTrips;

        int focusWeek = 0;
        int focusSunday = 0;

        if (transport.getRouteType().equals(Routes.type.BUGGY)) {
            focusWeek = getIndex(rawWeekTrips, ncbsBuggy.getNextTripString());
            focusSunday = getIndex(rawSundayTrips, mandaraBuggy.getNextTripString());
            formattedSunday = formatList(formattedSunday, focusSunday);
            formattedWeek = formatList(formattedWeek, focusWeek);

        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(transport.getNextTripDate());
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                formattedWeek = convertList(formattedWeek);
                focusSunday = getIndex(rawSundayTrips, transport.getNextTripString());
                formattedSunday = formatList(formattedSunday, focusSunday);
            } else {
                formattedSunday = convertList(formattedSunday);
                focusWeek = getIndex(rawWeekTrips, transport.getNextTripString());
                formattedWeek = formatList(formattedWeek, focusWeek);
            }
        }


        TransportAdapter weekAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, formattedWeek);
        TransportAdapter sundayAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, formattedSunday);
        sundayList.setAdapter(sundayAdapter);
        weekList.setAdapter(weekAdapter);
        weekTitle.setText(transport.getWeekTitle());
        sundayTitle.setText(transport.getSundayTitle());
        footnote1.setText(transport.getFooter1());
        footnote2.setText(transport.getFooter2());


        weekList.setSelection(focusWeek);
        weekList.smoothScrollToPositionFromTop(focusWeek, 0);
        sundayList.setSelection(focusSunday);
        sundayList.smoothScrollToPositionFromTop(focusSunday, 0);


        final String[] finalRawWeekTrips = rawWeekTrips;
        weekList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                //No reminders for offline mode
                if (!pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
                    showWeek(finalRawWeekTrips[i], true);
                }
            }
        });

        final String[] finalRawSundayTrips = rawSundayTrips;
        sundayList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //No reminders for offline mode
                if (!pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
                    showWeek(finalRawSundayTrips[i], false);
                }

            }
        });


    }

    private String[] formatList(String[] list, int index) {

        for (int i = 0; i < list.length; i++) {

            if (i == index) {
                list[i] = "<font color=\"red\">"
                        + new DateConverters().convertFormat(list[i], DateFormats.TIME_12_HOURS_STANDARD)
                        + "**</font>";
            } else {
                list[i] = new DateConverters().convertFormat(list[i], DateFormats.TIME_12_HOURS_STANDARD);
            }
        }

        return list;
    }

    private String[] convertList(String[] list) {
        for (int i = 0; i < list.length; i++) {
            list[i] = new DateConverters().convertFormat(list[i], DateFormats.TIME_12_HOURS_STANDARD);
        }
        return list;
    }

    private int getIndex(String[] list, String target) {
        for (int i = 0; i < list.length; i++) {
            if (list[i].equals(target)) {
                return i;
            }
        }
        return 1989;
    }


    private void showWeek(final String trip, final boolean isWeekDay) {

        String fromText = transport.getOrigin().toUpperCase();
        String toText = transport.getDestination().toUpperCase();

        if (transport.getRouteType().equals(Routes.type.BUGGY)) {
            if (!isWeekDay) {
                fromText = new RouteInformation(Routes.BUGGY_FROM_MANDARA).get().getFrom().toUpperCase();
                toText = new RouteInformation(Routes.BUGGY_FROM_MANDARA).get().getTo().toUpperCase();
            }
        }


        new AlertDialog.Builder(getContext())
                .setTitle("Set reminder ?")
                .setMessage(Html.fromHtml("For <b>" + new DateConverters().convertFormat(trip, DateFormats.TIME_12_HOURS_STANDARD) +
                        "</b> " + transport.getType().toLowerCase() + "<br>from <b>" +
                        fromText + "</b> to <b>" +
                        toText + "</b>"))

                .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getActivity(), TransportReminder.class);

                        if (transport.getRouteType().equals(Routes.type.BUGGY)) {
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
                        getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
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
