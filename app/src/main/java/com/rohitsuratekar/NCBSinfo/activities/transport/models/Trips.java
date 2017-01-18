package com.rohitsuratekar.NCBSinfo.activities.transport.models;

import android.support.annotation.NonNull;
import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * All transport related calculation will happen in this class
 */

public class Trips {

    private String origin;
    private String destination;
    private TransportType type;
    private List<RouteModel> allRoutes;
    private Calendar calendar;
    private SparseArray<Day> map = new SparseArray<>();
    private List<String> defaultList;
    private int routeNo;

    public Trips(String origin, String destination, TransportType type, @NonNull List<RouteModel> allRoutes, Calendar calendar) {
        this.origin = origin;
        this.destination = destination;
        this.type = type;
        this.allRoutes = new ArrayList<>(allRoutes);
        this.calendar = calendar;
        for (RouteModel r : allRoutes) {
            map.put(r.getDay(), new Day(r.getDay(), r.getTrips()));
        }
        this.defaultList = new ArrayList<>(getDefaultList());
        this.routeNo = allRoutes.get(0).getRoute();
    }

    public Trips(@NonNull List<RouteModel> allRoutes, Calendar calendar) {
        this.origin = allRoutes.get(0).getOrigin();
        this.destination = allRoutes.get(0).getDestination();
        this.type = allRoutes.get(0).getType();
        this.routeNo = allRoutes.get(0).getRoute();
        this.allRoutes = new ArrayList<>(allRoutes);
        this.calendar = calendar;
        for (RouteModel r : allRoutes) {
            map.put(r.getDay(), new Day(r.getDay(), r.getTrips()));
        }
        this.defaultList = new ArrayList<>(getDefaultList());
    }

    /**
     * @return : List of trips next to current time. Note that list size will be variable (min 4 elements)
     */
    public List<String> nextTransport() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendar.getTime());

        List<String> nextTrips = new ArrayList<>();
        List<String> allTrips = new ArrayList<>(getRefinedList(calendar));
        String currentString = DateConverter.convertToString(calendar, "HH:mm");
        allTrips.add(currentString);
        allTrips = DateConverter.sortStrings(ConverterMode.DATE_FIRST, allTrips);

        for (int i = 0; i < allTrips.size(); i++) {
            if (i > allTrips.indexOf(currentString)) {
                nextTrips.add(allTrips.get(i));
            }
        }
        while (nextTrips.size() < 5) {
            calendar.add(Calendar.DATE, 1);
            nextTrips.addAll(getRefinedList(calendar));
        }
        return nextTrips;
    }


    public boolean isNextTransportToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendar.getTime());
        List<String> allTrips = new ArrayList<>(getRefinedList(calendar));
        String currentString = DateConverter.convertToString(calendar, "HH:mm");
        allTrips.add(currentString);
        allTrips = DateConverter.sortStrings(ConverterMode.DATE_FIRST, allTrips);
        for (int i = 0; i < allTrips.size(); i++) {
            if (i > allTrips.indexOf(currentString)) {
                return true;
            }
        }
        return false;
    }

    public int getRouteNo() {
        return this.routeNo;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public TransportType getType() {
        return type;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public List<RouteModel> getAllRoutes() {
        return allRoutes;
    }

    /**
     * @return Today's' Day based on current calender object
     */
    public Day getToday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendar.getTime());
        return map.get(calendar.get(Calendar.DAY_OF_WEEK),
                new Day(calendar.get(Calendar.DAY_OF_WEEK), defaultList));
    }

    /**
     * @return Yesterday's Day based on current calender object
     */
    public Day getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendar.getTime());
        calendar.add(Calendar.DATE, -1);
        return map.get(calendar.get(Calendar.DAY_OF_WEEK),
                new Day(calendar.get(Calendar.DAY_OF_WEEK), defaultList));
    }

    /**
     * @return Tomorrows's Day based on current calender object
     */
    public Day getTomorrow() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(this.calendar.getTime());
        calendar.add(Calendar.DATE, 1);
        return map.get(calendar.get(Calendar.DAY_OF_WEEK),
                new Day(calendar.get(Calendar.DAY_OF_WEEK), defaultList));
    }

    /**
     * @param i : DAY_OF_WEEK
     * @return : Corresponding Day
     */

    public Day getDay(int i) {
        return map.get(i, new Day(i, getDefaultList()));
    }

    //TODO: Can be more complicated function by comparing actual trip values

    /**
     * @return : If current route is regular or not
     */
    public boolean isRegular() {
        return map.size() == 2 && map.get(Calendar.SUNDAY) != null;
    }

    /**
     * @return: Default day. If more than 2 elements, it will return non-sunday element as default
     */
    private List<String> getDefaultList() {
        for (RouteModel r : allRoutes) {
            if (r.getDay() != Calendar.SUNDAY) {
                return r.getTrips();
            }
        }
        return allRoutes.get(0).getTrips();
    }

    /**
     * @param cal: Reference Calender
     * @return : Proper trips for DAY_OF_WEEK for given calender object
     */

    private List<String> getRefinedList(Calendar cal) {
        List<String> combinedTrips = new ArrayList<>();
        for (String s : map.get(cal.get(Calendar.DAY_OF_WEEK), new Day(Calendar.DAY_OF_WEEK, defaultList)).getTodaysTrips()) {
            combinedTrips.add(s);
        }
        cal.add(Calendar.DATE, -1);
        for (String s : map.get(cal.get(Calendar.DAY_OF_WEEK), new Day(Calendar.DAY_OF_WEEK, defaultList)).getTomorrowTrips()) {
            combinedTrips.add(s);
        }
        return new ArrayList<>(
                DateConverter.sortStrings(ConverterMode.DATE_FIRST, combinedTrips));
    }

}
