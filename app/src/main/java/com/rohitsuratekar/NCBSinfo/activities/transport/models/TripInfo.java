package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.util.SparseArray;

import java.util.Calendar;
import java.util.List;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

class TripInfo {

    private SparseArray<TripDay> map;
    private TripDay defaultDay;

    TripInfo(List<TripDay> days) {
        map = new SparseArray<>();
        for (TripDay t : days) {
            map.put(t.getDay(), t);
        }
        setDefaultDay();
    }

    /**
     * Sets default day for any route.
     * Checks if non-Sunday trips are available first and then uses sunday.
     * Any first encounter from Monday will be considered as default day for rest of the week.
     * IMPORTANT: use after map has set up
     */
    private void setDefaultDay() {
        for (int i = Calendar.MONDAY; i < Calendar.SATURDAY + 1; i++) {
            if (map.get(i) != null) {
                defaultDay = map.get(i);
                return;
            }
        }
        if (defaultDay == null) {
            defaultDay = map.get(Calendar.SUNDAY);
        }
    }


    /**
     * General method to get non null TripDay model
     *
     * @param calendar : Calender object
     * @return : Respective TripDay (if available) else default TripDay
     */
    private TripDay get(Calendar calendar) {
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        return map.get(day) == null ? defaultDay : map.get(day);
    }

    /**
     * Returns unformatted next trip item
     *
     * @param calendar : Calender after which next trip will be calculated
     * @return : unformatted next trip
     * @see TripReport
     */
    TripReport nextTrip(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(calendar.getTimeInMillis());
        TripDay currentTrip = get(cal);
        int index = currentTrip.nextTripIndex(cal);
        if (index != -1) {
            return new TripReport(currentTrip.getRawTrips().get(index), true, index);
        } else {
            cal.add(Calendar.DATE, 1);
            return new TripReport(get(cal).getRawTrips().get(0), false);
        }
    }

}
