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
import com.rohitsuratekar.NCBSinfo.common.Utilities;
import com.rohitsuratekar.NCBSinfo.common.transport.models.ShuttleModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.SundayModel;
import com.rohitsuratekar.NCBSinfo.common.transport.models.WeekDayModel;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class TransportFragment extends Fragment {

    public static TransportFragment newInstance(int index) {
        TransportFragment fragment = new TransportFragment();
        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        fragment.setArguments(args);
        return fragment;
    }

    public TransportFragment() {
    }

    ShuttleModel shuttle;
    WeekDayModel weekday;
    SundayModel sunday;
    TextView weekTitle, sundayTitle, footnote1, footnote2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle args = getArguments();
        int index = args.getInt("index", 0);
        weekday = new WeekDayModel(Arrays.asList(new Utilities().stringToarray(pref.getString(new TransportHelper().routeToStrings(index)[3], getString(R.string.def_ncbs_iisc_week)))));
        sunday = new SundayModel(Arrays.asList(new Utilities().stringToarray(pref.getString(new TransportHelper().routeToStrings(index)[2], getString(R.string.def_ncbs_iisc_week)))));
        shuttle = new ShuttleModel(index, sunday, weekday);
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
        String[] rawWeekTrips =
                shuttle.getRawWeekday().getTrips()
                        .toArray(new String[shuttle.getRawWeekday().getTrips().size()]);

        String[] rawSundayTrips =
                shuttle.getRawSunday().getTrips()
                        .toArray(new String[shuttle.getRawSunday().getTrips().size()]);

        //Convert to regular format
        rawWeekTrips = new TransportHelper().convertToSimpleDate(rawWeekTrips);
        rawSundayTrips = new TransportHelper().convertToSimpleDate(rawSundayTrips);


        String[] nextTrips = shuttle.getNextTrip();
        String targetString = new TransportHelper().convertToSimpleDate(nextTrips[1]);
        if(nextTrips[0].equals(String.valueOf(Calendar.SUNDAY))){
            boolean gotDate = false;
            for(int i=0;i<rawSundayTrips.length;i++){
                if(rawSundayTrips[i].equals(targetString)){
                    rawSundayTrips[i]="<font color=\"red\">"+rawSundayTrips[i]+"**</font>";
                    gotDate = true;
                    break;
                }
            }
            if(!gotDate){
                for(int i=0;i<rawWeekTrips.length;i++){
                    if(rawWeekTrips[i].equals(targetString)){
                        rawWeekTrips[i]="<font color=\"red\">"+rawWeekTrips[i]+"**</font>";
                        break;
                    }
                }
            }
        }
        else{
            boolean gotDate = false;
            for(int i=0;i<rawWeekTrips.length;i++){
                if(rawWeekTrips[i].equals(targetString)){
                    rawWeekTrips[i]="<font color=\"red\">"+rawWeekTrips[i]+"**</font>";
                    gotDate = true;
                    break;
                }
            }
            if(!gotDate){
                for(int i=0;i<rawSundayTrips.length;i++){
                    if(rawSundayTrips[i].equals(targetString)){
                        rawSundayTrips[i]="<font color=\"red\">"+rawSundayTrips[i]+"**</font>";
                        break;
                    }
                }
            }

        }

        TransportAdapter weekAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, rawWeekTrips);
        TransportAdapter sundayAdapter = new TransportAdapter(getActivity(), R.layout.transport_item, rawSundayTrips);

        sundayList.setAdapter(sundayAdapter);
        weekList.setAdapter(weekAdapter);

        SimpleDateFormat modformat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        String currentTime = modformat.format(Calendar.getInstance().getTime());

        weekTitle.setText(getResources().getString(R.string.transport_list_week_title));
        sundayTitle.setText(getResources().getString(R.string.transport_list_sunday_title));
        footnote1.setText(getResources().getString(R.string.transport_footer1));
        footnote2.setText(getResources().getString(R.string.transport_footer2, currentTime));


    }


}
