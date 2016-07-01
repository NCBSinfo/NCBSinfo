package com.rohitsuratekar.NCBSinfo.activities.login;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

public class Login extends BaseActivity {
    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.LOGIN;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
