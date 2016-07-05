package com.rohitsuratekar.NCBSinfo.constants;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public interface AppConstants {

    enum modes {

        ONLINE("online"), OFFLINE("offline");

        private final String value;

        modes(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * SUCCESS: Logged in successfully
     * FAIL: Has not logged in
     */
    enum loginStatus {
        SUCCESS, FAIL
    }

    /**
     * VISITOR: user has never logged in or using offline mode
     * NER_USER : Just registered and registration details are not sent to server
     * OLD_USER : Logged in user but registration details are not sent to server
     * REGULAR_USER: Successfully sent details to server
     */
    enum userType {
        VISITOR("visitor"), NEW_USER("newUser"), OLD_USER("oldUser"), REGULAR_USER("regularUser");

        private final String value;

        userType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    enum canteens {
        MAIN_CANTEEN_GROUND(R.string.canteen_main_canteen),
        MAIN_CANTEEN_FIRST(R.string.canteen_main_canteen_firstfloor),
        MAIN_CANTEEN_FASTFOOD(R.string.canteen_main_canteen_fastfood),
        ACADEMIC_CAFETERIA(R.string.canteen_academic_cafeteria),
        ADMIN_CAFETERIA(R.string.canteen_admin_cafeteria),
        PARKING_CAFETERIA(R.string.canteen_parking_cafeteria);

        private final int nameID;

        canteens(int name) {
            this.nameID = name;
        }

        public int getNameID() {
            return nameID;
        }
    }




}
