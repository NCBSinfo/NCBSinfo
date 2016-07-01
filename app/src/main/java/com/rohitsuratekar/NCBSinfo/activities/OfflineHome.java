package com.rohitsuratekar.NCBSinfo.activities;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class OfflineHome extends BaseActivity{

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.OFFLINE_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}

