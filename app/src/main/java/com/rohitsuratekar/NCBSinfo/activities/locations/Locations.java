package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class Locations extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.LOCATIONS;
    }
}
