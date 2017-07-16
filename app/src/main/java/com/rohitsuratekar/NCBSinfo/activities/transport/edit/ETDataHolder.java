package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
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
}
