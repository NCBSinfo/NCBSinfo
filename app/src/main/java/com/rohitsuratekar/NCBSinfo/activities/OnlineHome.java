package com.rohitsuratekar.NCBSinfo.activities;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class OnlineHome extends BaseActivity{

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.ONLINE_HOME;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}



