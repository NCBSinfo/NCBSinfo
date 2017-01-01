package com.rohitsuratekar.NCBSinfo.activities.transport.routes;

import com.google.gson.Gson;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteConverter {
    private String weekday;
    private String sunday;
    private List<Date> week_dates = new ArrayList<>();
    private List<Date> sunday_dates = new ArrayList<>();
    private Gson gson = new Gson();

    public RouteConverter(String sunday, String weekday) throws ParseException {
        this.sunday = sunday;
        this.weekday = weekday;
        for (String s : sunday.split(",")) {
            sunday_dates.add(DateConverter.convertToDate(ConverterMode.DATE_FIRST, s));
        }
        for (String s : weekday.split(",")) {
            week_dates.add(DateConverter.convertToDate(ConverterMode.DATE_FIRST, s));
        }
    }
}
