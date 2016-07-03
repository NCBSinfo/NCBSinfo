package com.rohitsuratekar.NCBSinfo.constants;

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




}
