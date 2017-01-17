package com.rohitsuratekar.NCBSinfo.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.DefaultSettings;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.database.route.Route;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;
import com.secretbiology.helpers.general.OnSwipeTouchListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends BaseActivity {

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.HOME;
    }

    @BindView(R.id.hm_tx_above_transport)
    TextView aboveTransport;
    @BindView(R.id.hm_tx_next_transport)
    TextView nextTransport;
    @BindView(R.id.hm_tx_sugg1)
    TextView transportSuggestion1;
    @BindView(R.id.hm_tx_sugg2)
    TextView transportSuggestion2;
    @BindView(R.id.hm_tx_sugg3)
    TextView transportSuggestion3;
    @BindView(R.id.hm_tx_next_place)
    TextView currentPlace;
    @BindView(R.id.hm_tx_weekday)
    TextView currentDay;
    @BindView(R.id.hm_tx_date)
    TextView currentDate;
    @BindView(R.id.hm_tx_type)
    TextView currentType;
    @BindView(R.id.hm_tx_seats)
    TextView currentSeats;
    @BindView(R.id.hm_tx_sugg_note)
    TextView suggestionNote;

    @BindView(R.id.hm_im_back_image)
    ImageView backImage;
    @BindView(R.id.hm_im_fav)
    ImageView favorite;
    @BindView(R.id.hm_im_map)
    ImageView map;

    @BindView(R.id.hm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.home_base_layout)
    ConstraintLayout layout;

    private Route currentRoute;
    private int currentIndex = 0;
    private List<SuggestionModel> allSuggestions = new ArrayList<>();
    private HomeAdapter adapter;
    private List<Route> allRoutes;
    private Calendar currentCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allRoutes = DefaultSettings.getDefaultRoutes(getBaseContext());
        currentRoute = allRoutes.get(currentIndex);
        allSuggestions.add(new SuggestionModel("Some random suggestion will appear here", R.drawable.icon_favorite));
        allSuggestions.add(new SuggestionModel("Another tip", R.drawable.icon_home));
        allSuggestions.add(new SuggestionModel("Click here to get directions", R.drawable.icon_left));


        adapter = new HomeAdapter(allSuggestions);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        layout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            protected void onSwipeRight() {
                goRight();
            }

            @Override
            protected void onSwipeLeft() {
                goLeft();
            }

            @Override
            protected void onSwipeTop() {

            }

            @Override
            protected void onSwipeBottom() {

            }
        });
        setUpLayout();


    }

    @OnClick(R.id.hm_bt_right)
    public void goRight() {
        currentIndex++;
        if (currentIndex == allRoutes.size()) {
            currentIndex = 0;
        }
        currentRoute = allRoutes.get(currentIndex);
        setUpLayout();
    }

    @OnClick(R.id.hm_bt_left)
    public void goLeft() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = allRoutes.size() - 1;
        }
        currentRoute = allRoutes.get(currentIndex);
        setUpLayout();
    }

    @OnClick(R.id.hm_bt_view_all)
    public void showAll() {
        Intent i = new Intent(Home.this, Transport.class);
        i.putExtra(Transport.INTENT, currentIndex);
        startActivity(i);
    }


    private void setUpLayout() {


        currentPlace.setText(getString(R.string.home_current_place, currentRoute.getOrigin().toUpperCase(),
                currentRoute.getDestination().toUpperCase()));
        currentDay.setText(DateConverter.convertToString(currentCalendar, "EEE").toUpperCase());
        currentDate.setText(DateConverter.convertToString(currentCalendar, "dd, MMM").toUpperCase());
        currentType.setText(currentRoute.getType().toUpperCase());

        List<String[]> tripList = currentRoute.getTrips().nextTransport(currentCalendar);
        int listSize = tripList.size();
        nextTransport.setText(convertToReadable(tripList.get(0)[1]));
        if (listSize > 1) {
            transportSuggestion1.setText(convertToReadable(tripList.get(1)[1]));
        } else {
            transportSuggestion1.setText("");
        }
        if (listSize > 2) {
            transportSuggestion2.setText(convertToReadable(tripList.get(2)[1]));
        } else {
            transportSuggestion2.setText("");
        }
        if (listSize > 3) {
            transportSuggestion3.setText(convertToReadable(tripList.get(3)[1]));
        } else {
            transportSuggestion3.setText("");
        }

        aboveTransport.setText(getString(R.string.home_next_transport, currentRoute.getType().toLowerCase()));

        //Animation of icon
        Animation animation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);
        animation.setInterpolator(new AccelerateInterpolator());
        final Animation getIconBack = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        getIconBack.setInterpolator(new DecelerateInterpolator());
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                backImage.setImageResource(currentRoute.getIcon());
                backImage.startAnimation(getIconBack);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        backImage.startAnimation(animation);
    }

    private String convertToReadable(String s) {
        try {
            return DateConverter.convertToString(DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s), "hh:mm a");
        } catch (ParseException e) {
            return s;
        }
    }

}
