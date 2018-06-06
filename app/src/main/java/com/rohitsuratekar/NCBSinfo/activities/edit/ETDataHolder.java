package com.rohitsuratekar.NCBSinfo.activities.edit;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.TripData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Rohit Suratekar on 15-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ETDataHolder {
    private String origin;
    private String destination;
    private int type;
    private int frequency;
    private int[] frequencyDetails;
    private List<String> itemList;
    private List<TripData> tripData = new ArrayList<>();
    private boolean isRegular = false;

    ETDataHolder() {
        type = -1;
        frequency = R.id.et_fq_all_days;
        frequencyDetails = new int[]{1, 1, 1, 1, 1, 1, 1};
        itemList = new ArrayList<>();
    }

    String getOrigin() {
        return origin;
    }

    void setOrigin(String origin) {
        this.origin = origin;
    }

    String getDestination() {
        return destination;
    }

    void setDestination(String destination) {
        this.destination = destination;
    }

    int getType() {
        return type;
    }

    void setType(int type) {
        this.type = type;
    }

    int getFrequency() {
        return frequency;
    }

    void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    int[] getFrequencyDetails() {
        return frequencyDetails;
    }

    void setFrequencyDetails(int[] frequencyDetails) {
        this.frequencyDetails = frequencyDetails;
    }

    List<String> getItemList() {
        return itemList;
    }

    void setItemList(List<String> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    List<TripData> getTripData() {
        return tripData;
    }

    void setTripData(List<TripData> tripData) {
        this.tripData.clear();
        this.tripData.addAll(tripData);
        if (tripData.size() == 2) {
            if (tripData.get(0).getDay() == Calendar.SUNDAY || tripData.get(1).getDay() == Calendar.SUNDAY) {
                isRegular = true;
            }
        }
    }

    boolean isRegular() {
        return isRegular;
    }

    void setRegular(boolean regular) {
        isRegular = regular;
    }
}
