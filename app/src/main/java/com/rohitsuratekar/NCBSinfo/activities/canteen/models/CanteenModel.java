package com.rohitsuratekar.NCBSinfo.activities.canteen.models;

import com.rohitsuratekar.NCBSinfo.constants.AppConstants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 05-07-16.
 */
public class CanteenModel implements AppConstants {

    int dayOfWeek;

    MainCanteen mainCanteen = new MainCanteen();
    MainCanteenFirst mainCanteenFirst = new MainCanteenFirst();
    MainFastFood mainFastFood = new MainFastFood(dayOfWeek);
    AcademicCafeteria academicCafeteria = new AcademicCafeteria(dayOfWeek);
    AdminCafeteria adminCafeteria = new AdminCafeteria();
    ParkingCafeteria parkingCafeteria = new ParkingCafeteria();

    canteens place;
    Breakfast breakfast;
    MidMorningTea midMorningTea;
    MidEveningTea midEveningTea;
    Lunch lunch;
    Dinner dinner;


    public CanteenModel(canteens place, int DayOfWeek) {
        this.place = place;
        this.dayOfWeek = DayOfWeek;
        convertToOne(place);
    }

    public Breakfast getBreakfast() {
        return breakfast;
    }

    public MidMorningTea getMidMorningTea() {
        return midMorningTea;
    }

    public MidEveningTea getMidEveningTea() {
        return midEveningTea;
    }

    public Lunch getLunch() {
        return lunch;
    }

    public Dinner getDinner() {
        return dinner;
    }

    private void convertToOne(canteens place) {

        switch (place) {
            case MAIN_CANTEEN_GROUND:
                this.breakfast = mainCanteen.getBreakfast();
                this.midMorningTea = mainCanteen.getMidMorningTea();
                this.lunch = mainCanteen.getLunch();
                this.midEveningTea = mainCanteen.getMidEveningTea();
                this.dinner = mainCanteen.getDinner();
                break;
            case MAIN_CANTEEN_FIRST:
                this.lunch = mainCanteenFirst.getLunch();
                break;
            case MAIN_CANTEEN_FASTFOOD:
                this.midMorningTea = mainFastFood.getMidMorningTea();
                this.lunch = mainFastFood.getLunch();
                this.midEveningTea = mainFastFood.getMidEveningTea();
                this.dinner = mainFastFood.getDinner();
                break;
            case ACADEMIC_CAFETERIA:
                this.midMorningTea = academicCafeteria.getMidMorningTea();
                this.lunch = academicCafeteria.getLunch();
                this.midEveningTea = academicCafeteria.getMidEveningTea();
                break;
            case ADMIN_CAFETERIA:
                this.midMorningTea = adminCafeteria.getMidMorningTea();
                this.midEveningTea = adminCafeteria.getMidEveningTea();
                break;
            case PARKING_CAFETERIA:
                this.breakfast = parkingCafeteria.getBreakfast();
                this.midMorningTea = parkingCafeteria.getMidMorningTea();
                this.lunch = parkingCafeteria.getLunch();
                this.midEveningTea = parkingCafeteria.getMidEveningTea();
                break;
        }

    }


}
