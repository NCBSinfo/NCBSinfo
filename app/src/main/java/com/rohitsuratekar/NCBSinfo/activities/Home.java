package com.rohitsuratekar.NCBSinfo.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportLocation;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import java.util.ArrayList;
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

    private List<TransportLocation> allLocations;
    private int currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allLocations = new ArrayList<>();
        currentRoute = 0;
        allLocations.add(new TransportLocation("NCBS", "IISC", R.drawable.iisc, false));
        allLocations.add(new TransportLocation("IISC", "NCBS", R.drawable.ncbs, false));
        allLocations.add(new TransportLocation("NCBS", "Mandara", R.drawable.mandara, false));
        allLocations.add(new TransportLocation("Mandara", "NCBS", R.drawable.ncbs, true));
        next.setOnClickListener(this);
        previous.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.hm_img_next:
                currentRoute++;
                if (currentRoute == allLocations.size()) {
                    currentRoute = 0;
                }
                changeLayout();
                break;
            case R.id.hm_img_previous:
                currentRoute--;
                if (currentRoute < 0) {
                    currentRoute = allLocations.size() - 1;
                }
                changeLayout();
                break;
        }
    }

    private void changeLayout() {
        TransportLocation location = allLocations.get(currentRoute);
        backIcon.setImageResource(location.getIcon());
        origin.setText(location.getOrigin());
        destination.setText(location.getDestination());
        if (location.isFavorite()) {
            favorite.setImageResource(R.drawable.icon_favorite);
        } else {
            favorite.setImageResource(R.drawable.icon_blank_favorite);
        }
    }
}
