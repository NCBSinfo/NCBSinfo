package com.rohitsuratekar.NCBSinfo.background.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Created by Rohit Suratekar on 15-01-18 for NCBSinfo.
 * All code is released under MIT License.
 */

public class PublicInfo {

    @SerializedName("version")
    @Expose
    private int version;
    @SerializedName("updateTimestamp")
    @Expose
    private String updateTimestamp;
    @SerializedName("updateType")
    @Expose
    private String updateType;


    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUpdateTimestamp() {
        return updateTimestamp;
    }

    public void setUpdateTimestamp(String updateTimestamp) {
        this.updateTimestamp = updateTimestamp;
    }

    public String getUpdateType() {
        return updateType;
    }

    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }


}
