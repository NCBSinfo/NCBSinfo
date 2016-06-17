package com.rohitsuratekar.NCBSinfo.common.transport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransportHelper {

    public String[] routeToStrings(int route){
        String from;
        String to;
        switch (route){
            case 1: from = "ncbs"; to = "iisc"; break;
            case 2: from = "iisc"; to = "ncbs"; break;
            case 3: from = "ncbs"; to = "mandara"; break;
            case 4: from = "mandara"; to = "ncbs"; break;
            case 5: from = "ncbs"; to = "icts"; break;
            case 6: from = "icts"; to = "ncbs"; break;
            case 7: from = "ncbs"; to = "cbl"; break;
            default: from = "ncbs"; to = "iisc";
        }
        return new String[]{from, to};
    }

    public List<List<String>> rawToRegular (List<String> sundayTrips, List<String> weekdayTrips){
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat returnFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        List<Date> sundayRaw = new ArrayList<>();
        List<Date> sundayHalf = new ArrayList<>();
        List<Date> sunday = new ArrayList<>();
        List<Date> sundayFinal = new ArrayList<>();
        List<Date> monday = new ArrayList<>();
        List<Date> weekdayRaw = new ArrayList<>();
        List<Date> weekdayHalf = new ArrayList<>();
        List<Date> weekday = new ArrayList<>();
        List<Date> weekdayFinal= new ArrayList<>();

        //Convert to raw Dates

        for (String value : sundayTrips) {
            value = reformat(value);
            //Essential to keep date fixed while comparing timings
            //It is my birthday :P But it can be any constant date
            value = "10/08/1989 "+value;
            try {
                sundayRaw.add(format.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        for (String value : weekdayTrips) {
            value = reformat(value);
            //Essential to keep date fixed while comparing timings
            //It is my birthday :P But it can be any constant date
            value = "10/08/1989 "+value;
            try {
                weekdayRaw.add(format.parse(value));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        //Sort Date after 12 AM and before first trip
        for (Date date: sundayRaw)
        {
            if(sunday.size()==0){
                sunday.add(date);
            }
            else {
                if(sunday.get(sunday.size()-1).compareTo(date)<=0){
                    sunday.add(date);
                }
                else {
                    sundayHalf.add(date);
                }
            }
        }

        for (Date date: weekdayRaw)
        {
            if(weekday.size()==0){
                weekday.add(date);
            }
            else {
                if(weekday.get(weekday.size()-1).compareTo(date)<=0){
                    weekday.add(date);
                }
                else {
                    weekdayHalf.add(date);
                }
            }
        }

        //Make Monday
        //First add all values after 12 AM Sunday
        for (Date value: sundayHalf){ monday.add(value);}
        //Now add regular weekdays before 12 AM
        for (Date value: weekday){ monday.add(value);}
        //Now add Weekday values after 12 AM
        for (Date value: weekdayHalf){ weekdayFinal.add(value);}
        //Add Weekdays before 12 AM
        for (Date value: weekday){ weekdayFinal.add(value);}
        //Add sunday after 12 AM
        for (Date value: weekdayHalf){sundayFinal.add(value);}
        //Add rest of sunday
        for (Date value: sunday ){ sundayFinal.add(value);}

        List<List<String>> returnLists = new ArrayList<>();
        List<String> sundayStrings = new ArrayList<>();
        List<String> mondayStrings = new ArrayList<>();
        List<String> weekdayStrigs = new ArrayList<>();

        for(Date date : sundayFinal){ sundayStrings.add(reformat(returnFormat.format(date))); }
        for(Date date : monday){ mondayStrings.add(reformat(returnFormat.format(date))); }
        for(Date date : weekdayFinal){ weekdayStrigs.add(reformat(returnFormat.format(date))); }

        returnLists.add(sundayStrings);
        returnLists.add(mondayStrings);
        returnLists.add(weekdayStrigs);

        return returnLists;
    }

    public String reformat(String value){
        //Check if there are any periods
        if(value.contains(".")){
            value = value.replace(".",":");
        }
        //Check if both timings are in hh:mm format
        String[] split = value.split(":");
        //If only one letter, add zero before
        if(split[0].length()==1){
            value = "0"+value;
        }
        //If only one letter, add zero after
        if(split[1].length()==1){
            value = value+"0";
        }

        return value;
    }
}
