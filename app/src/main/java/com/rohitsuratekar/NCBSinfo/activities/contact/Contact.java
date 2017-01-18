package com.rohitsuratekar.NCBSinfo.activities.contact;

import android.os.Bundle;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.SetUpActivity;

public class Contact extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SetUpActivity(this, R.layout.contact, "Contact", false);
    }
}
