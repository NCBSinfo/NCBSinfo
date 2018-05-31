package com.rohitsuratekar.NCBSinfo.fragments.settings;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.common.Helper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsInfo extends AppCompatActivity {

    public static String TERMS = "terms";
    public static String PRIVACY = "privacy";
    public static String ACK = "ack";
    public static String ABOUT = "about";
    public static String EGG_ASSAY1 = "assay1";
    public static String EGG_ASSAY2 = "assay2";

    @BindView(R.id.st_info_text)
    TextView mainText;

    int tap = 0;
    String action;

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

        action = getIntent().getAction();

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
            } else if (action.equals(EGG_ASSAY1)) {
                //New test for custom events for analytics
                Bundle params = new Bundle();
                params.putString("caught_eggs", Helper.timestamp());
                mainText.setText(Html.fromHtml(getString(R.string.egg_ron)));
            } else if (action.equals(EGG_ASSAY2)) {
                Bundle params = new Bundle();
                params.putString("caught_eggs", Helper.timestamp());
                mainText.setText(Html.fromHtml(getString(R.string.egg_hermione)));
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @OnClick(R.id.st_info_text)
    public void activateEgg() {
        tap++;
        if (tap == 4 && action.equals(PRIVACY)) {
            Toast.makeText(getApplicationContext(), "Muggles mode activated. Restart the app and visit settings!", Toast.LENGTH_LONG).show();
            new AppPrefs(getApplicationContext()).eggActivated();
        }
    }
}
