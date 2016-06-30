package com.rohitsuratekar.NCBSinfo;

import android.os.Bundle;
import android.view.ViewStub;

import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;

public class Home extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewStub viewStub = (ViewStub) findViewById(R.id.baseView);
        viewStub.setLayoutResource(R.layout.home);
        viewStub.inflate();
    }

}
