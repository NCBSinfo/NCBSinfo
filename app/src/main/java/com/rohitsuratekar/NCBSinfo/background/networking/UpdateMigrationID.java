package com.rohitsuratekar.NCBSinfo.background.networking;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rohit Suratekar on 04-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class UpdateMigrationID {
    @SerializedName("migrationID")
    @Expose
    private String migrationID;

    public UpdateMigrationID() {
    }

    public String getMigrationID() {
        return migrationID;
    }

    public void setMigrationID(String migrationID) {
        this.migrationID = migrationID;
    }
}