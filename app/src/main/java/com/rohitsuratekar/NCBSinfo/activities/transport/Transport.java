package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.common.CurrentActivity;

public class Transport extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.TRANSPORT;
    }
}
