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
 * Created by Rohit Suratekar on 12-07-16.
 */
public class SettingsCommon extends AppCompatActivity {

    public static final String INTENT = SettingsCommon.class.getName();
    public static final String TERMS = "termsAndConditions";
    public static final String ABOUT_US = "aboutUs";
    public static final String FAQ = "faq";
    public static final String PRIVACY = "privacy";
    TextView commonText;
    BaseParameters baseParameters;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_base);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        commonText = (TextView) findViewById(R.id.settings_common_text);
        baseParameters = new BaseParameters(getBaseContext());

        String trigger = getIntent().getStringExtra(INTENT);

        if (trigger != null) {
            switch (trigger) {
                case TERMS:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.terms)));
                    setTitle(R.string.settings_terms);
                    break;
                case ABOUT_US:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.about_us)));
                    setTitle(R.string.settings_about_ncbinfo);
                    break;
                case FAQ:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.faq)));
                    setTitle(R.string.settings_faq);
                    break;
                case PRIVACY:
                    commonText.setText(Html.fromHtml(getResources().getString(R.string.privacy_statement)));
                    setTitle(R.string.settings_privacy);
                    break;


            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            this.overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
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
