package com.rohitsuratekar.NCBSinfo.activities.add;

import android.os.Bundle;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;

import butterknife.ButterKnife;

public class AddTransport extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_transport_frequency);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.add_route);
    }

    @Override
    protected int setNavigationMenu() {
        return 0;
    }
}
