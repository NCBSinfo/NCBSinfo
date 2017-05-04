package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.activities.transport.models.DayTrips;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Trip;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class TransportMethods {


    public static SparseArray<List<Trip>> getWeekMap(List<DayTrips> allTrips) {
        List<Trip> defaultTrips = new ArrayList<>();
        SparseArray<List<Trip>> tripArray = new SparseArray<>();
        //Put all known trips
        for (DayTrips model : allTrips) {
            tripArray.put(model.getDay(), model.getTrips());
        }
        //Get default non sunday trips
        for (int i = 2; i < 8; i++) {
            if (tripArray.get(i) != null) {
                defaultTrips.clear();
                defaultTrips.addAll(tripArray.get(i));
                break;
            }
        }
        //If it is only Sunday, use sunday as default
        if (defaultTrips.size() == 0) {
            defaultTrips.addAll(tripArray.get(1));
        }
        //Create fine array
        for (int i = 1; i < 8; i++) {
            if (tripArray.get(i) == null) {
                tripArray.put(i, defaultTrips);
            }
        }
        return tripArray;
    }


    public static Trip nextTrip(SparseArray<List<Trip>> week, Calendar calendar) {

        Trip nextTrip;
        for (Trip t : week.get(calendar.get(Calendar.DAY_OF_WEEK))) {
            if (t.isAfter(calendar)) {
                nextTrip = new Trip();
                nextTrip.setTime(t.getTime());
                nextTrip.setToday(t.isToday());
                return nextTrip;
            }
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(calendar.getTimeInMillis());
        cal.add(Calendar.DATE, 1);
        nextTrip = new Trip();
        nextTrip.setTime(week.get(cal.get(Calendar.DAY_OF_WEEK)).get(0).getTime());
        nextTrip.setToday(week.get(cal.get(Calendar.DAY_OF_WEEK)).get(0).isToday());
        nextTrip.setInSameList(false);
        return nextTrip;
    }
}
