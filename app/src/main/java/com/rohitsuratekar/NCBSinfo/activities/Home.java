package com.rohitsuratekar.NCBSinfo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.routes.RouteConverter;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportLocation;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportType;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.DateConverter;
import com.secretbiology.helpers.general.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends BaseActivity implements View.OnClickListener {

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.HOME;
    }

    @BindView(R.id.hm_title_light)
    ImageView imgTitleLight;
    @BindView(R.id.hm_title_dark)
    ImageView imgTitleDark;
    @BindView(R.id.hm_back_icon)
    ImageView backIcon;
    @BindView(R.id.hm_img_fav)
    ImageView favorite;
    @BindView(R.id.hm_img_transport_type)
    ImageView imgType;
    @BindView(R.id.hm_img_next)
    ImageView next;
    @BindView(R.id.hm_img_previous)
    ImageView previous;


    @BindView(R.id.hm_txt_origin)
    TextView origin;
    @BindView(R.id.hm_txt_destination)
    TextView destination;
    @BindView(R.id.hm_txt_next_transport)
    TextView nextTransport;
    @BindView(R.id.hm_txt_transport_indicator)
    TextView transportIndicator;
    @BindView(R.id.hm_txt_transport1)
    TextView transportSuggest1;
    @BindView(R.id.hm_txt_transport2)
    TextView transportSuggest2;
    @BindView(R.id.hm_txt_transport_type)
    TextView trasnportType;
    @BindView(R.id.hm_txt_day)
    TextView day;
    @BindView(R.id.hm_txt_date)
    TextView date;
    @BindView(R.id.hm_txt_pre)
    TextView preButton;
    @BindView(R.id.hm_txt_next)
    TextView nextButton;

    @BindView(R.id.home_layout)
    ConstraintLayout layout;
    @BindView(R.id.hm_btn_show_all)
    Button showAll;

    private List<TransportLocation> allLocations;
    private int currentRoute;
    private Calendar currentCalender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allLocations = new ArrayList<>();
        currentRoute = 0;
        currentCalender = Calendar.getInstance();
        allLocations.add(new TransportLocation("NCBS", "IISC", R.drawable.iisc, TransportType.SHUTTLE, false));
        allLocations.add(new TransportLocation("IISC", "NCBS", R.drawable.ncbs, TransportType.BUGGY, false));
        allLocations.add(new TransportLocation("NCBS", "Mandara", R.drawable.mandara, TransportType.SHUTTLE, false));
        allLocations.add(new TransportLocation("Mandara", "NCBS", R.drawable.ncbs, TransportType.SHUTTLE, true));
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        preButton.setOnClickListener(this);
        showAll.setOnClickListener(this);


        layout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            protected void onSwipeRight() {
                goNext(true);
            }

            @Override
            protected void onSwipeLeft() {
                goNext(false);
            }

            @Override
            protected void onSwipeTop() {

            }

            @Override
            protected void onSwipeBottom() {

            }
        });
        changeLayout();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hm_img_next:
                goNext(true);
                break;
            case R.id.hm_img_previous:
                goNext(false);
                break;
            case R.id.hm_txt_next:
                goNext(true);
                break;
            case R.id.hm_txt_pre:
                goNext(false);
                break;
            case R.id.hm_btn_show_all:
                startActivity(new Intent(Home.this, Transport.class));
                break;
        }
    }

    private void goNext(boolean goNext) {
        if (goNext) {
            currentRoute++;
            if (currentRoute == allLocations.size()) {
                currentRoute = 0;
            }
        } else {
            currentRoute--;
            if (currentRoute < 0) {
                currentRoute = allLocations.size() - 1;
            }
        }
        changeLayout();
    }

    private void changeLayout() {
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        TransportLocation location = allLocations.get(currentRoute);
        backIcon.setImageResource(location.getIcon());
        origin.setText(location.getOrigin());
        destination.setText(location.getDestination());
        if (location.isFavorite()) {
            favorite.setImageResource(R.drawable.icon_favorite);
        } else {
            favorite.setImageResource(R.drawable.icon_blank_favorite);
        }
        imgType.setImageResource(location.getType().getIcon());
        trasnportType.setText(getString(location.getType().getName()).toUpperCase());
        day.setText(DateConverter.convertToString(currentCalender, "EEE").toUpperCase());
        date.setText(DateConverter.convertToString(currentCalender, "dd MMM").toUpperCase());
        transportIndicator.setText(getString(R.string.next_transport,
                getString(location.getType().getName()).toLowerCase()));
        backIcon.startAnimation(animation);
    }
}
