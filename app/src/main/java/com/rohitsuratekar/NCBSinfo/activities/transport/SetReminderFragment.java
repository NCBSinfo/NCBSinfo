package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

import org.joda.time.DateTime;
import org.joda.time.Minutes;

import java.util.Calendar;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 07-07-16.
 */
public class SetReminderFragment extends Fragment {

    //Public
    public static String BUNDLE_ROUTE = "bundleFragment_route";
    public static String BUNDLE_TIME = "bundleFragment_time";


    TextView from, to, time, type;
    TextView welcome, message;
    TransportModel transport;
    int offset; // in min
    Calendar finalCalender;
    Button changeTime;
    boolean today;
    String reminderTime, units;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_reminder_set, container, false);
        Bundle bundle = this.getArguments();
        offset = 10; //Default
        units = "min"; //Default
        today = true;

        from = (TextView) rootView.findViewById(R.id.reminder_from_text);
        to = (TextView) rootView.findViewById(R.id.reminder_to_text);
        time = (TextView) rootView.findViewById(R.id.reminder_trip_text);
        type = (TextView) rootView.findViewById(R.id.reminder_transportType_text);
        welcome = (TextView) rootView.findViewById(R.id.reminder_welcome_message);
        message = (TextView) rootView.findViewById(R.id.reminder_welcome_details);
        changeTime = (Button) rootView.findViewById(R.id.reminder_change_button);

        transport = new TransportModel(new TransportHelper(getContext()).getRoute(bundle.getInt(BUNDLE_ROUTE)), getContext());
        reminderTime = bundle.getString(BUNDLE_TIME);
        if (reminderTime != null) {
            time.setText(new Converters().convertToSimpleDate(reminderTime));
        }
        finalCalender = getReminderTime(reminderTime);

        from.setText(transport.getFrom().toUpperCase());
        to.setText(transport.getTO().toUpperCase());
        type.setText(transport.getType());


        changeTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int min) {
                        setOffset(hour, min);
                        setupText();
                    }
                }, finalCalender.get(Calendar.HOUR_OF_DAY), finalCalender.get(Calendar.MINUTE), false);
                timePickerDialog.setTitle("When do you want reminder?\n");
                timePickerDialog.show();
            }
        });

        setupText();

        return rootView;
    }

    private Calendar getReminderTime(String time) {
        Calendar calendar = new Converters().convertToCalender(time);
        calendar.setTimeInMillis(calendar.getTimeInMillis() - offset * 60 * 1000);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            today = false;
        }
        return calendar;
    }

    private void setupText() {
        welcome.setText(getResources().getString(R.string.transport_reminder_message, String.valueOf(offset), units));
        String tempString = "<b>today</b>";
        if (!today) {
            tempString = "<b>tomorrow</b>";
        }

        message.setText(Html.fromHtml(getResources().getString(R.string.transport_reminder_details, tempString,
                new Converters().calenderToDate(finalCalender), new Converters().calenderToTime(finalCalender))));
    }

    private void setOffset(int hour, int min) {

        DateTime targetTime = new DateTime(getTime(reminderTime));
        Calendar temCal = Calendar.getInstance();
        temCal.setTimeInMillis(finalCalender.getTimeInMillis());
        finalCalender.set(Calendar.HOUR_OF_DAY, hour);
        finalCalender.set(Calendar.MINUTE, min);
        DateTime currentTime = new DateTime(finalCalender);
        Minutes m = Minutes.minutesBetween(targetTime, currentTime);

        if (m.getMinutes() < 0) {
            offset = Math.abs(m.getMinutes());
            if(offset>60){
                units = "hours";
                offset = offset/60;
            }
        } else {
            Toast.makeText(getContext(), "You can't set time after departure time", Toast.LENGTH_LONG).show();
            finalCalender.setTimeInMillis(temCal.getTimeInMillis());
        }

    }

    private Calendar getTime(String time) {
        Calendar calendar = new Converters().convertToCalender(time);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            today = false;
        }
        return calendar;
    }


}
