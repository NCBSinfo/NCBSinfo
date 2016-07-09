package com.rohitsuratekar.NCBSinfo.background.remote;

import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 09-07-16.
 */
public class RemoteManager {

    List<RemoteModel> allList;

    public RemoteManager(List<RemoteModel> allList) {
        this.allList = allList;
    }

    public boolean isUpdateAvailable(){
        for(RemoteModel m : allList){

            new DateConverters().convertToDate("asdas");
        }
        return false;
    }

}
