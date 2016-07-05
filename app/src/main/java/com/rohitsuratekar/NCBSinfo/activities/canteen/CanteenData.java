package com.rohitsuratekar.NCBSinfo.activities.canteen;

import com.rohitsuratekar.NCBSinfo.activities.canteen.models.CanteenModel;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.utilities.Converters;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class CanteenData implements AppConstants {

    Calendar calendar;

    public CanteenData(Calendar calendar) {
        this.calendar = calendar;
    }

    public List<canteens> getAllBreakfastLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getBreakfast() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getBreakfast().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getBreakfast().getEndTime()))) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllMorningTeaLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getMidMorningTea() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getMidMorningTea().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getMidMorningTea().getEndTime()))) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllLunchLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getLunch() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getLunch().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getLunch().getEndTime()))) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllEveningTeaLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getMidEveningTea() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getMidEveningTea().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getMidEveningTea().getEndTime()))) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllDinnerLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getDinner() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getDinner().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getDinner().getEndTime()))) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public boolean isBreakfast() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getBreakfast() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getBreakfast().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getBreakfast().getEndTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMidMorningTea() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getMidMorningTea() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getMidMorningTea().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getMidMorningTea().getEndTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLunch() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getLunch() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getLunch().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getLunch().getEndTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMidEveningTea() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getMidEveningTea() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getMidEveningTea().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getMidEveningTea().getEndTime()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDinner() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c);
            if (place.getDinner() != null) {
                if (calendar.after(new Converters().convertToCalender(place.getDinner().getStartTime()))
                        && calendar.before(new Converters().convertToCalender(place.getDinner().getEndTime()))) {
                    return true;
                }
            }
        }
        return false;
    }
}
