package com.rohitsuratekar.NCBSinfo.activities.canteen;

import com.rohitsuratekar.NCBSinfo.activities.canteen.models.Breakfast;
import com.rohitsuratekar.NCBSinfo.activities.canteen.models.CanteenModel;
import com.rohitsuratekar.NCBSinfo.activities.canteen.models.Dinner;
import com.rohitsuratekar.NCBSinfo.activities.canteen.models.Lunch;
import com.rohitsuratekar.NCBSinfo.activities.canteen.models.MidEveningTea;
import com.rohitsuratekar.NCBSinfo.activities.canteen.models.MidMorningTea;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

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
    int today;
    int sunday = Calendar.SUNDAY;
    int saturday = Calendar.SATURDAY;

    public CanteenData(Calendar calendar) {
        this.calendar = calendar;
        this.today = calendar.get(Calendar.DAY_OF_WEEK);
    }

    public List<canteens> getAllBreakfastLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getBreakfast() != null) {
                if (isThereBreakFast(place.getBreakfast())) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllMorningTeaLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getMidMorningTea() != null) {
                if (isThereMorningTea(place.getMidMorningTea())) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllLunchLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getLunch() != null) {
                if (isThereLunch(place.getLunch())) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllEveningTeaLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getMidEveningTea() != null) {
                if (isThereEveningTea(place.getMidEveningTea())) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public List<canteens> getAllDinnerLocations() {
        List<canteens> allLocations = new ArrayList<>();
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getDinner() != null) {
                if (isThereDinner(place.getDinner())) {
                    allLocations.add(c);
                }
            }
        }
        return allLocations;
    }

    public boolean isBreakfast() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getBreakfast() != null) {
                if (isThereBreakFast(place.getBreakfast())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMidMorningTea() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getMidMorningTea() != null) {
                if (isThereMorningTea(place.getMidMorningTea())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isLunch() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getLunch() != null) {
                if (isThereLunch(place.getLunch())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isMidEveningTea() {

        for (canteens c : canteens.values()) {

            CanteenModel place = new CanteenModel(c, today);
            if (place.getMidEveningTea() != null) {
                if (isThereEveningTea(place.getMidEveningTea())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDinner() {
        for (canteens c : canteens.values()) {
            CanteenModel place = new CanteenModel(c, today);
            if (place.getDinner() != null) {
                if (isThereDinner(place.getDinner())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isThereDinner(Dinner dinner) {
        if (today == sunday) {
            return dinner.isSundayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(dinner.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(dinner.getEndTime()));
        } else if (today == saturday) {
            return dinner.isSaturdayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(dinner.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(dinner.getEndTime()));
        } else {
            return calendar.after(new DateConverters().convertToCalendar(dinner.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(dinner.getEndTime()));
        }
    }

    private boolean isThereLunch(Lunch lunch) {
        if (today == sunday) {
            return lunch.isSundayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(lunch.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(lunch.getEndTime()));
        } else if (today == saturday) {
            return lunch.isSaturdayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(lunch.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(lunch.getEndTime()));
        } else {
            return calendar.after(new DateConverters().convertToCalendar(lunch.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(lunch.getEndTime()));
        }
    }

    private boolean isThereBreakFast(Breakfast breakfast) {
        if (today == sunday) {
            return breakfast.isSundayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(breakfast.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(breakfast.getEndTime()));
        } else if (today == saturday) {
            return breakfast.isSaturdayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(breakfast.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(breakfast.getEndTime()));
        } else {
            return calendar.after(new DateConverters().convertToCalendar(breakfast.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(breakfast.getEndTime()));
        }
    }

    private boolean isThereMorningTea(MidMorningTea midMorningTea) {
        if (today == sunday) {
            return midMorningTea.isSundayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(midMorningTea.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(midMorningTea.getEndTime()));
        } else if (today == saturday) {
            return midMorningTea.isSaturdayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(midMorningTea.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(midMorningTea.getEndTime()));
        } else {
            return calendar.after(new DateConverters().convertToCalendar(midMorningTea.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(midMorningTea.getEndTime()));
        }
    }

    private boolean isThereEveningTea(MidEveningTea midEveningTea) {

        if (today == sunday) {
            return midEveningTea.isSundayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(midEveningTea.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(midEveningTea.getEndTime()));
        } else if (today == saturday) {

            return midEveningTea.isSaturdayOpen()
                    && calendar.after(new DateConverters().convertToCalendar(midEveningTea.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(midEveningTea.getEndTime()));
        } else {
            return calendar.after(new DateConverters().convertToCalendar(midEveningTea.getStartTime()))
                    && calendar.before(new DateConverters().convertToCalendar(midEveningTea.getEndTime()));
        }
    }

    public boolean isFoodAvailable() {
        return isBreakfast() || isMidMorningTea() || isLunch() || isMidEveningTea() || isDinner();
    }
}
