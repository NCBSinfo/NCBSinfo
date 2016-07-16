package com.rohitsuratekar.NCBSinfo.activities.transport.reminder;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.background.alarms.AlarmsHelper;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.database.AlarmData;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Calendar;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 07-07-16.
 */
public class SetReminderFragment extends Fragment implements AlarmConstants {

    //Public
    public static String BUNDLE_ROUTE = "bundleFragment_route";
    public static String BUNDLE_TIME = "bundleFragment_time";
    public static String BUNDLE_DAY = "bundleFragment_day";


    TextView from, to, time, type;
    TextView welcome, message;
    TransportModel transport;
    int offset; // in min
    Calendar finalCalender;
    Button changeTime, setButton;
    boolean today;
    String reminderTime, units;
    Bundle bundle;


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_reminder_set, container, false);
        bundle = this.getArguments();
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
        setButton = (Button) rootView.findViewById(R.id.reminder_set_button);

        transport = new TransportModel(new TransportHelper(getActivity()).getRoute(bundle.getInt(BUNDLE_ROUTE)), getActivity());
        reminderTime = bundle.getString(BUNDLE_TIME);
        if (reminderTime != null) {
            time.setText(new DateConverters().convertFormat(reminderTime, DateFormats.TIME_12_HOURS_STANDARD));
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

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<AlarmModel> allAlarms = new AlarmData(getContext()).getAll();
                boolean isAlreadyset = false;

                for (AlarmModel a : allAlarms) {
                    if (a.getExtraValue().equals(reminderTime) && a.getExtraParameter().equals(String.valueOf(transport.getRouteNo()))) {
                        isAlreadyset = true;
                    }
                }

                if (!isAlreadyset) {

                    AlarmModel alarmModel = new AlarmModel();
                    alarmModel.setAlarmID(new AlarmsHelper().createTransportID(
                            new TransportHelper(getContext()).getRoute(transport.getRouteNo()),
                            finalCalender.get(Calendar.DAY_OF_WEEK), getString(finalCalender)));
                    alarmModel.setLevel(alarmLevel.TRANSPORT.name());
                    alarmModel.setType(alarmType.SINGLE_SHOT.name());
                    alarmModel.setTrigger(alarmTriggers.TRANSPORT_REMINDER.name());
                    alarmModel.setAlarmTime(getTime(finalCalender));
                    alarmModel.setAlarmDate(getDate(finalCalender));
                    alarmModel.setExtraParameter(String.valueOf(transport.getRouteNo()));
                    alarmModel.setExtraValue(reminderTime);
                    long alarmKey = new AlarmData(getActivity()).addAndGetID(alarmModel);

                    //Send broadcast to set alarm
                    Intent broadcast = new Intent(getActivity(), Alarms.class);
                    broadcast.putExtra(Alarms.INTENT, alarmTriggers.SET_ALARM.name());
                    broadcast.putExtra(Alarms.ALARM_KEY, String.valueOf(alarmKey));
                    getActivity().sendBroadcast(broadcast);
                    Toast.makeText(getContext(), "Reminder set", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getContext(), Transport.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                } else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Existing reminder")
                            .setMessage("You have already set reminder for this trip. Please delete existing first and then create new one.")
                            .setIcon(R.drawable.icon_warning)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
                }
            }

        });

        return rootView;
    }

    private Calendar getReminderTime(String time) {
        if (bundle.getString(BUNDLE_DAY) != null) {
            if (bundle.getString(BUNDLE_DAY).equals("weekday") || transport.isBuggy()) {
                Calendar calendar = new DateConverters().convertToCalendar(time);
                calendar.setTimeInMillis(calendar.getTimeInMillis() - offset * 60 * 1000);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 1);
                    today = false;
                }
                return calendar;
            } else {

                Calendar calendar = new DateConverters().convertToCalendar(time);
                calendar.setTimeInMillis(calendar.getTimeInMillis() - offset * 60 * 1000);
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                if (calendar.before(Calendar.getInstance())) {
                    calendar.add(Calendar.DATE, 7);
                }
                return calendar;
            }

        }
        return Calendar.getInstance();
    }

    private void setupText() {
        welcome.setText(getResources().getString(R.string.transport_reminder_message, String.valueOf(offset), units));
        String tempString = "<b>today</b>";
        int reminderDay = finalCalender.get(Calendar.DATE) - Calendar.getInstance().get(Calendar.DATE);
        boolean thisweek = finalCalender.get(Calendar.WEEK_OF_YEAR) == Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);

        if (reminderDay == 1) {
            tempString = "<b>tomorrow</b>";
        } else if (thisweek && reminderDay > 1) {
            tempString = "<b>this week</b>";
        } else if (!thisweek) {
            tempString = "<b>next week</b>";
        }

        message.setText(Html.fromHtml(getResources().getString(R.string.transport_reminder_details, tempString,
                new DateConverters().convertToString(finalCalender, DateFormats.READABLE_DATE),
                new DateConverters().convertToString(finalCalender, DateFormats.TIME_12_HOURS_STANDARD))));
    }

    private void setOffset(int hour, int min) {

        DateTime targetTime = new DateTime(getTimeFromCalendar(reminderTime));
        Calendar temCal = Calendar.getInstance();
        temCal.setTimeInMillis(finalCalender.getTimeInMillis());
        finalCalender.set(Calendar.HOUR_OF_DAY, hour);
        finalCalender.set(Calendar.MINUTE, min);
        DateTime currentTime = new DateTime(finalCalender);
        Minutes m = Minutes.minutesBetween(targetTime, currentTime);

        if (m.getMinutes() < 0) {

            if (finalCalender.before(Calendar.getInstance())) {
                Toast.makeText(getContext(), "You can't set time is past. Unless you are time traveller :D", Toast.LENGTH_LONG).show();
                finalCalender.setTimeInMillis(temCal.getTimeInMillis());
            } else {
                offset = Math.abs(m.getMinutes());
                if (offset > 60) {
                    units = "hours";
                    offset = offset / 60;
                }
            }
        } else {
            Toast.makeText(getContext(), "You can't set time after departure time", Toast.LENGTH_LONG).show();
            finalCalender.setTimeInMillis(temCal.getTimeInMillis());
        }

    }

    private Calendar getTimeFromCalendar(String time) {
        Calendar calendar = new DateConverters().convertToCalendar(time);
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1);
            today = false;
        }
        return calendar;
    }

    private String getString(Calendar calendar) {
        DateTime currentTime = new DateTime(calendar);
        DateTimeFormatter format = DateTimeFormat.forPattern("HHmm");
        return format.print(currentTime);
    }

    private String getTime(Calendar calendar) {
        return new DateConverters().convertToString(calendar, DateFormats.TIME_24_HOURS_STANDARD);
    }

    private String getDate(Calendar calendar) {
        return new DateConverters().convertToString(calendar, DateFormats.DATE_STANDARD);
    }


}
