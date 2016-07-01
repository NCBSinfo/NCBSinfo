package com.rohitsuratekar.NCBSinfo.activities.events;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class Events extends BaseActivity {

    public static final String EVENT_CODE = "event_code";

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.EVENTS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
