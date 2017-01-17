package com.rohitsuratekar.NCBSinfo.database.route;

import android.content.Context;
import android.util.SparseArray;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Trips {

    private List<Day> dayList;
    private Day defaultDay;
    private SparseArray<Day> dayMap = new SparseArray<>();

    public Trips(List<Day> dayList) {
        this.dayList = new ArrayList<>(dayList);
        this.defaultDay = setDefault();
        distribute();
    }

    public Trips(Day... dayList) {
        this.dayList = Arrays.asList(dayList);
        this.defaultDay = setDefault();
        distribute();
    }

    public Day getDefaultDay() {
        return defaultDay;
    }

    public void setDefaultDay(Day defaultDay) {
        this.defaultDay = defaultDay;
    }

    public List<Day> getDayList() {
        return dayList;
    }

    public void setDayList(List<Day> dayList) {
        this.dayList = new ArrayList<>(dayList);
    }

    private Day setDefault() {
        for (Day day : dayList) {
            if (day.getDay() != Calendar.SUNDAY) {
                return day;
            }
        }
        return dayList.get(0);
    }

    private void distribute() {
        for (Day day : dayList) {
            dayMap.put(day.getDay(), day);
        }
    }

    private Day getDay(int day) {
        Day d = new Day(day, defaultDay.getTripList());
        return dayMap.get(day, d);
    }

    public List<String[]> nextTransport(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        List<String[]> returnList = new ArrayList<>();
        List<String> list = refinedTrips(calendar.get(Calendar.DAY_OF_WEEK));
        String currentItem = DateConverter.convertToString(calendar, "HH:mm");
        if (!list.contains(currentItem)) {
            list.add(currentItem);
        }
        list = DateConverter.sortStrings(ConverterMode.DATE_FIRST, list);
        for (int i = 0; i < list.size(); i++) {
            if (i > list.indexOf(currentItem)) {
                returnList.add(new String[]{String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)), list.get(i)});
            }
        }
        while (returnList.size() < 5) {
            calendar.add(Calendar.DATE, 1);
            for (String s : refinedTrips(calendar.get(Calendar.DAY_OF_WEEK))) {
                returnList.add(new String[]{String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)), s});
            }
        }
        return returnList;
    }


    public Day leftDay(Calendar calendar) {
        if (isRegular() && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return defaultDay;
        } else {
            return getDay(calendar.get(Calendar.DAY_OF_WEEK));
        }
    }

    public int leftIndex(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        List<String[]> list = nextTransport(calendar);
        if (isRegular()) {
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                return leftDay(calendar).getTripList().indexOf(list.get(0)[1]);
            } else {
                return -1;
            }
        } else {
            if (String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)).equals(list.get(0)[0])) {
                return leftDay(calendar).getTripList().indexOf(list.get(0)[1]);
            } else {
                return list.size();
            }
        }
    }


    private boolean isRegular() {
        return dayMap.size() == 2 && dayMap.get(Calendar.SUNDAY) != null;
    }


    public Day rightDay(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        if (isRegular() && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            return getDay(Calendar.SUNDAY);
        } else {
            calendar.add(Calendar.DATE, 1);
            return getDay(calendar.get(Calendar.DAY_OF_WEEK));
        }
    }

    public int rightIndex(Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        List<String[]> list = nextTransport(calendar);
        if (isRegular()) {
            if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                return rightDay(calendar).getTripList().indexOf(list.get(0)[1]);
            } else {
                return -1;
            }
        } else {
            calendar.add(Calendar.DATE, 1);
            if (String.valueOf(calendar.get(Calendar.DAY_OF_WEEK)).equals(list.get(0)[0])) {
                return rightDay(calendar).getTripList().indexOf(list.get(0)[1]);
            } else {
                return -1;
            }
        }
    }

    public String leftListTitle(Context context, Calendar calendar) {
        if (isRegular()) {
            return context.getString(R.string.weekdays);
        } else {
            return DateConverter.convertToString(calendar, "EEEE");
        }
    }

    public String rightListTitle(Context context, Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        if (isRegular()) {
            return context.getString(R.string.sunday);
        } else {
            calendar.add(Calendar.DATE, 1);
            return DateConverter.convertToString(calendar, "EEEE");
        }
    }

    public String footnote(Context context, Calendar calendar) {
        if (isRegular()) {
            return context.getString(R.string.transport_regular_note);
        } else {
            return context.getString(R.string.transport_other_note);
        }
    }

    public void setLeftButton(ImageButton button, TextView textView, Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        if (isRegular()) {
            button.setImageResource(android.R.color.transparent);
            button.setEnabled(false);
            textView.setText("");
        } else {
            button.setImageResource(R.drawable.icon_left);
            button.setEnabled(true);
            calendar.add(Calendar.DATE, -1);
            textView.setText(DateConverter.convertToString(calendar, "EEE").toUpperCase());
        }
    }

    public void setRightButton(ImageButton button, TextView textView, Calendar cal) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cal.getTime());
        if (isRegular()) {
            button.setImageResource(android.R.color.transparent);
            button.setEnabled(false);
            textView.setText("");
        } else {
            button.setImageResource(R.drawable.icon_right);
            button.setEnabled(true);
            calendar.add(Calendar.DATE, 2);
            textView.setText(DateConverter.convertToString(calendar, "EEE").toUpperCase());
        }
    }

    private List<String> refinedTrips(int day) {
        List<String> strings = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, day);
        Day toDay = getDay(calendar.get(Calendar.DAY_OF_WEEK));
        calendar.add(Calendar.DATE, -1);
        Day previousDay = getDay(calendar.get(Calendar.DAY_OF_WEEK));
        for (String s : previousDay.getNextDayTrips()) {
            strings.add(s);
        }
        for (String s : toDay.getTodayTrips()) {
            strings.add(s);
        }
        return strings;
    }
}
