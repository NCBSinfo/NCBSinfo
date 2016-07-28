package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 23-07-16.
 */
public class PrivacyNotice extends AppCompatActivity {

    BaseParameters baseParameters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_scroll_layout);
        baseParameters = new BaseParameters(getBaseContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView notice = (TextView) findViewById(R.id.scrollview_text);
        setTitle(getString(R.string.settings_privacy));
        notice.setText(Html.fromHtml(getString(R.string.privacy_statement)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
    }
}
