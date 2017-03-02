package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CurrentStateModel {

    private String origin;
    private String destination;
    private List<String> routeList;
    private TransportDay day;
    private String firstTrip;
    private String type;

    public CurrentStateModel() {
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<String> getRouteList() {
        return routeList;
    }

    public void setRouteList(List<String> routeList) {
        this.routeList = routeList;
    }

    public TransportDay getDay() {
        return day;
    }

    public void setDay(TransportDay day) {
        this.day = day;
    }

    public String getFirstTrip() {
        return firstTrip;
    }

    public void setFirstTrip(String firstTrip) {
        this.firstTrip = firstTrip;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    List<String> getRearrangedTrips() {
        List<String> list = new ArrayList<>();
        List<String> afterList = new ArrayList<>();
        for (int i = 0; i < routeList.size(); i++) {
            if (i < routeList.indexOf(firstTrip)) {
                try {
                    afterList.add(DateConverter.changeFormat(ConverterMode.DATE_FIRST, routeList.get(i), "HH:mm"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    list.add(DateConverter.changeFormat(ConverterMode.DATE_FIRST, routeList.get(i), "HH:mm"));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
        for (String s : afterList) {
            list.add(s);
        }
        return list;

    }
}
