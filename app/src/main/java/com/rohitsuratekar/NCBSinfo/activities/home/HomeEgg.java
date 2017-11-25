package com.rohitsuratekar.NCBSinfo.activities.home;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeEgg extends AppCompatActivity {

    @BindView(R.id.egg_got_text)
    TextView gotText;

    @BindView(R.id.egg_phd_text)
    TextView phdText;

    @BindView(R.id.egg_btn)
    Button eggBtn;

    private int[] phd;
    private int[] got;
    private int[] btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_egg);
        ButterKnife.bind(this);
        Toolbar toolbar = findViewById(R.id.home_egg_toolbar);
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
        setUp();
        randomize();

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("eggs", General.timeStamp());
        mFirebaseAnalytics.logEvent("home", params);
    }

    private void setUp() {
        phd = new int[]{R.string.egg_phd1, R.string.egg_phd2, R.string.egg_phd3, R.string.egg_phd4,
                R.string.egg_phd5, R.string.egg_phd6, R.string.egg_phd7, R.string.egg_phd8, R.string.egg_phd9,
                R.string.egg_phd10, R.string.egg_phd11, R.string.egg_phd12, R.string.egg_phd13, R.string.egg_phd14,
                R.string.egg_phd15, R.string.egg_phd16, R.string.egg_phd17, R.string.egg_phd18};

        got = new int[]{R.string.egg_got1, R.string.egg_got2, R.string.egg_got3, R.string.egg_got4, R.string.egg_got5,
                R.string.egg_got6, R.string.egg_got7, R.string.egg_got8, R.string.egg_got9, R.string.egg_got10,
                R.string.egg_got11, R.string.egg_got12, R.string.egg_got13, R.string.egg_got14, R.string.egg_got15,
                R.string.egg_got16, R.string.egg_got17};

        btn = new int[]{R.string.egg_got_btn1, R.string.egg_got_btn2, R.string.egg_got_btn3, R.string.egg_got_btn4,
                R.string.egg_got_btn5, R.string.egg_got_btn6};
    }


    @OnClick(R.id.egg_btn)
    public void randomize() {
        try {
            Random random = new Random();
            gotText.setText(got[random.nextInt(got.length)]);
            random = new Random();
            phdText.setText(phd[random.nextInt(phd.length)]);
            random = new Random();
            eggBtn.setText(btn[random.nextInt(btn.length)]);
        } catch (Exception ignore) {

        }
    }
}
