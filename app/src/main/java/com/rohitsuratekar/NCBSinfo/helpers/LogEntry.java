package com.rohitsuratekar.NCBSinfo.helpers;

import android.content.Context;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.models.LogModel;

public class LogEntry {
    private String timestamp;
    private String message;
    private String details;
    private String category;
    private int statuscode;
    private int status;
    private Context context;
    private StatusConverter st;

    public LogEntry(Context context, int statuscode) {
        this.statuscode = statuscode;
        this.context = context;
        common();
        addToDatabase();
    }

    public LogEntry(Context context, int statuscode,String details) {
        this.context = context;
        this.statuscode = statuscode;
        common();
        this.details = details;
        addToDatabase();
    }

    private void common(){
        st = new StatusConverter(statuscode);
        this.timestamp = st.getTimestamp();
        this.message = st.getMessage();
        this.category = st.getCategory();
        this.status = st.getStatus();
        this.details = st.getDetails();
    }

    private void addToDatabase(){
        Database db = new Database(context);
        db.addLogEntry(new LogModel(0,timestamp,message,details,category,statuscode,status));
        db.close();
    }

}
