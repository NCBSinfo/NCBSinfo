package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class Transport extends BaseActivity{

    public static final String INDENT = "transportIntent";
    private final String TAG = getClass().getSimpleName();

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.TRANSPORT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
