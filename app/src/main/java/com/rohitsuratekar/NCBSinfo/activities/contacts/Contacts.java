package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class Contacts extends BaseActivity {

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.CONTACTS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
