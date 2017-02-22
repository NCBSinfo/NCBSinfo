package com.rohitsuratekar.NCBSinfo.preferences;

import android.content.Context;

import java.util.Calendar;
import java.util.HashMap;

public class LearningPrefs extends Preferences {
    private Context context;

    private static final String APP_OPENED = "learn_appOpened";
    private static final String TIME = "learn_time";
    private static final String DAY = "learn_day";

    public LearningPrefs(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Counts how many times app has opened
     */
    public void appOpened() {
        put(APP_OPENED, opened() + 1);
        Calendar cal = Calendar.getInstance();
        updateTime(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.DAY_OF_WEEK));

    }

    private void updateTime(int hour, int day) {
        put(TIME + String.valueOf(hour), getTimeProfile(hour) + 1);
        put(DAY + String.valueOf(day), getDayProfile(day) + 1);

    }

    private int getTimeProfile(int hour) {
        return get(TIME + String.valueOf(hour), 0);
    }

    private int getDayProfile(int day) {
        return get(DAY + String.valueOf(day), 0);
    }

    public int opened() {
        return get(APP_OPENED, 0);
    }

    /**
     * Returns hash map representing tapp opened frequency on each hour
     *
     * @return HashMap with keys from 0 -23
     */
    public HashMap<Integer, Integer> timeMap() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 24; i++) {
            map.put(i, getTimeProfile(i));
        }
        return map;
    }

    /**
     * Returns hash map representing app opened frequency on each day of week
     *
     * @return HashMap with keys from 1 -7
     */
    public HashMap<Integer, Integer> dayMap() {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = Calendar.SUNDAY; i < Calendar.SATURDAY + 1; i++) {
            map.put(i, getDayProfile(i));
        }
        return map;
    }


}
