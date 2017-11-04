package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsInfo extends AppCompatActivity {

    public static String TERMS = "terms";
    public static String PRIVACY = "privacy";
    public static String ACK = "ack";
    public static String ABOUT = "about";

    @BindView(R.id.st_info_text)
    TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_info);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.setting_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        String action = getIntent().getAction();

        //Need to make links clickable
        mainText.setMovementMethod(LinkMovementMethod.getInstance());

        // TODO : Proper layouts
        if (action != null) {
            if (action.equals(TERMS)) {
                mainText.setText(Html.fromHtml(getString(R.string.terms)));
            } else if (action.equals(PRIVACY)) {
                mainText.setText(Html.fromHtml(getString(R.string.privacy_statement)));
            } else if (action.equals(ACK)) {
                mainText.setText(Html.fromHtml(getString(R.string.libraries_used)));
            } else if (action.equals(ABOUT)) {
                mainText.setText(Html.fromHtml(getString(R.string.about_us)));
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
