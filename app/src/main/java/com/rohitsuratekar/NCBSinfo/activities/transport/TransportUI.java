package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Trips;
import com.secretbiology.helpers.general.DateConverter;

import java.util.Calendar;
import java.util.List;

/**
 * All transport UI elements will be connected to this class
 */

class TransportUI {

    private Trips trips;
    private Boolean isRegular;
    private Calendar calendar;
    private Context context;

    TransportUI(Trips trips, Context context) {
        this.trips = trips;
        this.isRegular = trips.isRegular();
        this.calendar = trips.getCalendar();
        this.context = context;
    }

    /**
     * @return : List for left side recycler
     */
    List<String> leftTrips() {
        if (isRegular && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return trips.getDay(Calendar.MONDAY).getAllTrips();
        }
        return trips.getToday().getAllTrips();
    }

    /**
     * @return : List for right side recycler
     */
    List<String> rightTrips() {
        if (isRegular) {
            return trips.getDay(Calendar.SUNDAY).getAllTrips();
        }
        return trips.getTomorrow().getAllTrips();
    }

    /**
     * @return : List for left side recycler index
     */
    int leftIndex() {
        if (isRegular) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return -1;
            } else {
                return trips.getToday().getAllTrips().indexOf(trips.nextTransport().get(0));
            }
        }
        if (trips.isNextTransportToday()) {
            return trips.getToday().getAllTrips().indexOf(trips.nextTransport().get(0));
        } else {
            return trips.getToday().getAllTrips().size();
        }
    }

    /**
     * @return : List for right side recycler index
     */
    int rightIndex() {
        if (trips.isNextTransportToday()) {
            return -1;
        }
        if (isRegular && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            return -1;
        }
        return trips.getToday().getAllTrips().indexOf(trips.nextTransport().get(0));
    }

    String leftTitle() {
        if (isRegular) {
            return context.getString(R.string.weekdays);
        } else {
            return DateConverter.convertToString(calendar, "EEEE");
        }
    }

    String rightTitle() {
        if (isRegular) {
            return context.getString(R.string.sunday);
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.add(Calendar.DATE, 1);
            return DateConverter.convertToString(cal, "EEEE");
        }
    }

    String footnote() {
        if (isRegular) {
            return context.getString(R.string.transport_regular_note);
        } else {
            return context.getString(R.string.transport_other_note);
        }
    }

    String type() {
        return trips.getType().toString();
    }

    void leftButton(ImageButton imageButton, TextView textView) {
        if (isRegular) {
            imageButton.setImageResource(android.R.color.transparent);
            imageButton.setEnabled(false);
            textView.setText("");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.add(Calendar.DATE, -1);
            imageButton.setImageResource(R.drawable.icon_left);
            imageButton.setEnabled(true);
            textView.setText(DateConverter.convertToString(cal, "EEE").toUpperCase());
        }
    }

    void rightButton(ImageButton imageButton, TextView textView) {
        if (isRegular) {
            imageButton.setImageResource(android.R.color.transparent);
            imageButton.setEnabled(false);
            textView.setText("");
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(calendar.getTime());
            cal.add(Calendar.DATE, 2);
            imageButton.setImageResource(R.drawable.icon_right);
            imageButton.setEnabled(true);
            textView.setText(DateConverter.convertToString(cal, "EEE").toUpperCase());
        }
    }

}
