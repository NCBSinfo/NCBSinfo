package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import java.util.Calendar;

public enum TransportDay {
    ALL_WEEK("All Week", 0, Calendar.MONDAY),
    WEEKDAYS("Weekdays", 1, Calendar.MONDAY),
    WEEKEND("Weekend", 2, Calendar.SUNDAY),
    SUNDAY("Sunday", 3, Calendar.SUNDAY),
    MONDAY("Monday", 4, Calendar.MONDAY),
    TUESDAY("Tuesday", 5, Calendar.TUESDAY),
    WEDNESDAY("Wednesday", 6, Calendar.WEDNESDAY),
    THURSDAY("Thursday", 7, Calendar.THURSDAY),
    FRIDAY("Friday", 8, Calendar.FRIDAY),
    SATURDAY("Saturday", 9, Calendar.SATURDAY);

    private String title;
    private int index;
    private int value;

    TransportDay(String title, int index, int value) {
        this.title = title;
        this.index = index;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public int getValue() {
        return value;
    }
}
