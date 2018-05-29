package com.rohitsuratekar.NCBSinfo.fragments.transport;

import com.rohitsuratekar.NCBSinfo.common.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rohit Suratekar on 06-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TripDay {
    private List<String> today = new ArrayList<>();
    private List<String> tomorrow = new ArrayList<>();

    TripDay(List<String> rawTrips) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat(Helper.FORMAT_TIME, Locale.ENGLISH);

        Date firstTrip = format.parse(rawTrips.get(0));
        today.add(rawTrips.get(0)); //Add first trip.
        //Now if date is before first date, it should be after midnight trip
        for (String trip : rawTrips) {
            if (firstTrip.before(format.parse(trip))) {
                today.add(trip);
            } else if (firstTrip.after(format.parse(trip))) {
                tomorrow.add(trip);
            }
        }
    }

    List<String> getToday() {
        return today;
    }

    List<String> getTomorrow() {
        return tomorrow;
    }
}
