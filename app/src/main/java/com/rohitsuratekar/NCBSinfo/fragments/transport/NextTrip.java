package com.rohitsuratekar.NCBSinfo.fragments.transport;

import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.rohitsuratekar.NCBSinfo.common.Helper.timeToDate;

/**
 * Created by Rohit Suratekar on 06-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class NextTrip {

    private SparseArray<List<String>> weekMap;
    private List<String> defaultTrips = null;
    private SimpleDateFormat format = new SimpleDateFormat(Helper.FORMAT_TIME, Locale.ENGLISH);

    /**
     * WeekMap will contains trips for any day of week. Keys are equal to Calender.DAY_OF_WEEK
     * of respective days. Default trips should be in following priority
     * Monday > Tuesday > .... > Sunday
     */
    public NextTrip(SparseArray<List<String>> weekMap) {
        this.weekMap = weekMap;
        initialize();

    }

    public NextTrip(List<TripData> data) {
        this.weekMap = new SparseArray<>();
        for (TripData t : data) {
            weekMap.put(t.getDay(), t.getTrips());
        }
        initialize();
    }

    private void initialize() {
        //Get default trips in given priority
        for (int i = Calendar.MONDAY; i <= Calendar.SATURDAY; i++) {
            if (weekMap.get(i) != null) {
                defaultTrips = weekMap.get(i);
                break;
            }
        }

        //In Case only Sunday Trips are available
        if (defaultTrips == null) {
            defaultTrips = weekMap.get(Calendar.SUNDAY);
        }
    }

    /**
     * This method should return next transport trip for given timing
     *
     * @param inputCalender : User defined time
     */
    public String[] calculate(Calendar inputCalender) throws ParseException {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(inputCalender.getTimeInMillis());
        Date now = new Date(calendar.getTimeInMillis()); //Record input time

        //Check with trips for post midnight from yesterday
        calendar.add(Calendar.DATE, -1); //Yesterday
        List<String> beforeFirstTrip = new TripDay(getTrips(calendar.get(Calendar.DAY_OF_WEEK))).getTomorrow();
        for (String s : beforeFirstTrip) {
            Date d1 = timeToDate(now, s);
            if (d1.after(now) || d1.equals(now)) {
                return new String[]{s, "-1"};
            }
        }


        //Check today's trips
        calendar.add(Calendar.DATE, 1); //Today
        List<String> todaysTrip = new TripDay(getTrips(calendar.get(Calendar.DAY_OF_WEEK))).getToday();

        for (String s : todaysTrip) {
            Date d1 = timeToDate(now, s);
            if (d1.after(now) || d1.equals(now)) {
                return new String[]{s, "0"};
            }
        }


        //Check tomorrow's trips (i.e. post midnight today)
        List<String> tomorrowTrip = new TripDay(getTrips(calendar.get(Calendar.DAY_OF_WEEK))).getTomorrow();
        for (String s : tomorrowTrip) {
            Date d1 = timeToDate(now, s);
            //In this case we need to check if time is between last trip of today and first trip of tomorrow
            //Hence we should use before instead after for comparing
            if (d1.before(now) || d1.equals(now)) {
                return new String[]{s, "1"};
            }
        }

        //Else get first trip of tomorrow
        calendar.add(Calendar.DATE, 1);
        return new String[]{getTrips(calendar.get(Calendar.DAY_OF_WEEK)).get(0), "2"};
    }

    public List<String> getTrips(int day) {
        List<String> now = weekMap.get(day);
        if (now != null) {
            return now;
        } else {
            return defaultTrips;
        }
    }


}
