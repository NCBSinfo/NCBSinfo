package com.rohitsuratekar.NCBSinfo.activities.transport;

import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Rohit Suratekar on 06-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TripDay {
    private List<String> today = new ArrayList<>();
    private List<String> tomorrow = new ArrayList<>();

    TripDay(List<String> rawTrips) throws ParseException {
        Date firstTrip = DateConverter.convertToDate(ConverterMode.DATE_FIRST, rawTrips.get(0));
        today.add(rawTrips.get(0)); //Add first trip.
        //Now if date is before first date, it should be after midnight trip
        for (String trip : rawTrips) {
            if (firstTrip.before(DateConverter.convertToDate(ConverterMode.DATE_FIRST, trip))) {
                today.add(trip);
            } else if (firstTrip.after(DateConverter.convertToDate(ConverterMode.DATE_FIRST, trip))) {
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
