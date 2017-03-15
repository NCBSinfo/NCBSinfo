package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsInfo extends AppCompatActivity implements SettingsIDs {

    @BindView(R.id.info_text)
    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_info);
        ButterKnife.bind(this);
        String action = getIntent().getAction();
        if (action != null) {
            switch (Integer.parseInt(action)) {
                case ACTION_TERMS_AND_CONDITIONS:
                    setTitle(getString(R.string.terms_and_conditions));
                    mainText.setText(Html.fromHtml(getString(R.string.terms)));
                    break;
                case ACTION_PRIVACY:
                    setTitle(getString(R.string.privacy));
                    mainText.setText(Html.fromHtml(getString(R.string.privacy_statement)));
                    break;
                case ACTION_ABOUT_US:
                    setTitle(getString(R.string.settings_about_us));
                    mainText.setText(Html.fromHtml(getString(R.string.about_us)));
                    break;
                case ACTION_COPYRIGHT:
                    setTitle(getString(R.string.acknowledgments));
                    mainText.setText(Html.fromHtml(getString(R.string.libraries_used)));
                    break;
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
