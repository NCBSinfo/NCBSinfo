package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Context;
import android.util.SparseArray;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 20-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportModel {
    private String origin;
    private String destination;
    private String type;
    private int image;
    private String footnote;
    private String leftTitle;
    private String rightTitle;
    private int leftIndex;
    private int rightIndex;
    private List<String> leftTrips;
    private List<String> rightTrips;
    private List<TripData> tripData;
    private Calendar calendar;
    private boolean isRegular;
    private int routeID;

    TransportModel(Context context, Calendar calendar, RouteData routeData, List<TripData> tripData) {
        origin = routeData.getOrigin();
        destination = routeData.getDestination();
        routeID = routeData.getRouteID();
        type = routeData.getType();
        this.calendar = calendar;
        image = Helper.getImage(destination);
        this.tripData = tripData;
        this.isRegular = isRegular(tripData);
        footnote = context.getString(R.string.tp_footnote, "21 Jun 2017", "NCBSinfo");
        if (isRegular) {
            leftTitle = context.getString(R.string.weekdays);
            rightTitle = context.getString(R.string.sunday);
            for (TripData t : tripData) {
                if (t.getDay() == Calendar.SUNDAY) {
                    rightTrips = t.getTrips();
                } else {
                    leftTrips = t.getTrips();
                }
            }
        } else {
            setOdds();
        }
        setIndex();

    }

    private boolean isRegular(List<TripData> tripData) {
        if (tripData.size() == 2) {
            boolean hasSunday = false;
            for (TripData t : tripData) {
                if (t.getDay() == Calendar.SUNDAY) {
                    hasSunday = true;
                }
            }
            return hasSunday;
        } else {
            return false;
        }
    }

    private void setIndex() {
        int today = calendar.get(Calendar.DAY_OF_WEEK);
        try {
            String[] next = new NextTrip(tripData).calculate(calendar);
            if (isRegular) {
                switch (next[1]) {
                    case "0":
                    case "1":
                        if (today == Calendar.SUNDAY) {
                            leftIndex = -1;
                            rightIndex = rightTrips.indexOf(next[0]);
                        } else {
                            leftIndex = leftTrips.indexOf(next[0]);
                            rightIndex = -1;
                        }
                        break;
                    case "2":
                        if (today == Calendar.SATURDAY) {
                            leftIndex = -1;
                            rightIndex = rightTrips.indexOf(next[0]);
                        } else {
                            leftIndex = leftTrips.indexOf(next[0]);
                            rightIndex = -1;
                        }
                        break;
                    case "-1":
                        if (today == Calendar.MONDAY) {
                            leftIndex = -1;
                            rightIndex = rightTrips.indexOf(next[0]);
                        } else {
                            leftIndex = leftTrips.indexOf(next[0]);
                            rightIndex = -1;
                        }
                        break;
                }

            } else {
                if (next[1].equals("2")) {
                    leftIndex = -1;
                    rightIndex = rightTrips.indexOf(next[0]);
                } else {
                    leftIndex = leftTrips.indexOf(next[0]);
                    rightIndex = -1;
                }
            }
        } catch (ParseException e) {
            Log.error(e.getMessage());
            leftIndex = -1;
            rightIndex = -1;
        }
    }

    private void setOdds() {
        SparseArray<List<String>> map = new SparseArray<>();
        for (TripData t : tripData) {
            map.put(t.getDay(), t.getTrips());
        }
        List<String> defaultTrips = null;
        for (int i = Calendar.MONDAY; i <= Calendar.SATURDAY; i++) {
            if (map.get(i) != null) {
                defaultTrips = map.get(i);
                break;
            }
        }
        //In Case only Sunday Trips are available
        if (defaultTrips == null) {
            defaultTrips = map.get(Calendar.SUNDAY);
        }

        leftTitle = DateConverter.convertToString(calendar, "EEEE");
        if (map.get(calendar.get(Calendar.DAY_OF_WEEK)) != null) {
            leftTrips = map.get(calendar.get(Calendar.DAY_OF_WEEK));
        } else {
            leftTrips = defaultTrips;
        }
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);
        rightTitle = DateConverter.convertToString(c, "EEEE");
        if (map.get(c.get(Calendar.DAY_OF_WEEK)) != null) {
            rightTrips = map.get(c.get(Calendar.DAY_OF_WEEK));
        } else {
            rightTrips = defaultTrips;
        }
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public String getType() {
        return type;
    }

    public int getImage() {
        return image;
    }

    public String getFootnote() {
        return footnote;
    }

    public String getLeftTitle() {
        return leftTitle;
    }

    public String getRightTitle() {
        return rightTitle;
    }

    public int getLeftIndex() {
        return leftIndex;
    }

    public int getRightIndex() {
        return rightIndex;
    }

    public List<String> getLeftTrips() {
        return leftTrips;
    }

    public List<String> getRightTrips() {
        return rightTrips;
    }

    public List<TripData> getTripData() {
        return tripData;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public boolean isRegular() {
        return isRegular;
    }

    public int getRouteID() {
        return routeID;
    }
}
