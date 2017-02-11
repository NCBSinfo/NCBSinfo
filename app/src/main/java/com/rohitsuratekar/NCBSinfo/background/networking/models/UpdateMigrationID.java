package com.rohitsuratekar.NCBSinfo.background.networking.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
