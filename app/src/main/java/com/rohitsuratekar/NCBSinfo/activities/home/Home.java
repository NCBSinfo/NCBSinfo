package com.rohitsuratekar.NCBSinfo.activities.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportMethods;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;
import com.secretbiology.helpers.general.listeners.OnClickGuard;
import com.secretbiology.helpers.general.listeners.OnSwipeTouchListener;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends BaseActivity {

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
    @BindView(R.id.hm_im_top_back)
    ImageView topBack;
    @BindView(R.id.hm_im_top_dark)
    ImageView topDark;

    @BindView(R.id.hm_bt_left)
    ImageButton leftBtn;
    @BindView(R.id.hm_bt_right)
    ImageButton rightBtn;
    @BindView(R.id.hm_bt_view_all)
    Button allBtn;

    @BindView(R.id.hm_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.home_base_layout)
    ConstraintLayout layout;

    private CurrentSession session = CurrentSession.getInstance();
    private Calendar currentCalendar = Calendar.getInstance();

    private List<SuggestionModel> allSuggestions = new ArrayList<>();
    private HomeAdapter adapter;
    private int currentIndex;
    private Route currentRoute;
    private List<String> nextList;
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getBaseContext());
        currentIndex = session.getCurrentIndex();
        currentRoute = session.getCurrentRoute();
        nextList = session.getNextList();
        allSuggestions.add(new SuggestionModel("Some random suggestion will appear here", R.drawable.icon_favorite));
        allSuggestions.add(new SuggestionModel("Another tip", R.drawable.icon_home));
        allSuggestions.add(new SuggestionModel("Click here to get directions", R.drawable.icon_map));
        adapter = new HomeAdapter(allSuggestions);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getBaseContext());
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        layout.setOnTouchListener(new OnSwipeTouchListener(getBaseContext()) {
            @Override
            protected void onSwipeRight() {
                goLeft();
            }

            @Override
            protected void onSwipeLeft() {
                goRight();
            }

            @Override
            protected void onSwipeTop() {
            }

            @Override
            protected void onSwipeBottom() {
            }
        });

        leftBtn.setOnClickListener(new OnClickGuard(200) {
            @Override
            public void guard(View v) {
                goLeft();
            }
        });
        rightBtn.setOnClickListener(new OnClickGuard(200) {
            @Override
            public void guard(View v) {
                goRight();
            }
        });

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setFavoriteRoute(currentRoute.getRouteNo());
                General.makeShortToast(getBaseContext(), "Default route changed");
                favorite.setImageResource(R.drawable.icon_favorite);
            }
        });

        setUpItems();
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.HOME;
    }

    @OnClick(R.id.hm_bt_view_all)
    public void showAllTrips() {
        Intent intent = new Intent(Home.this, Transport.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            View statusBar = findViewById(android.R.id.statusBarBackground);
            View navigationBar = findViewById(android.R.id.navigationBarBackground);
            View toolbar = findViewById(R.id.toolbar);
            List<Pair<View, String>> pairs = new ArrayList<>();
            pairs.add(Pair.create((View) currentPlace, currentPlace.getTransitionName()));
            pairs.add(Pair.create((View) topBack, topBack.getTransitionName()));
            pairs.add(Pair.create((View) leftBtn, leftBtn.getTransitionName()));
            pairs.add(Pair.create((View) rightBtn, rightBtn.getTransitionName()));
            pairs.add(Pair.create((View) allBtn, allBtn.getTransitionName()));
            // pairs.add(Pair.create((View) topDark, topDark.getTransitionName()));
            pairs.add(Pair.create((View) favorite, favorite.getTransitionName()));
            pairs.add(Pair.create(toolbar, toolbar.getTransitionName()));
            pairs.add(Pair.create(statusBar, statusBar.getTransitionName()));
            pairs.add(Pair.create(navigationBar, navigationBar.getTransitionName()));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, pairs.toArray(new Pair[pairs.size()]));
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }


    }

    private void goRight() {
        currentIndex++;
        if (currentIndex == session.getAllRoutes().size()) {
            currentIndex = 0;
        }
        currentRoute = session.getAllRoutes().get(currentIndex);
        new setUpLayout().execute();
    }

    private void goLeft() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = session.getAllRoutes().size() - 1;
        }
        currentRoute = session.getAllRoutes().get(currentIndex);
        new setUpLayout().execute();
    }

    private class setUpLayout extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            nextList = new TransportMethods().nextTransport(currentCalendar, currentRoute);
            session.setNextList(nextList);
            session.setCurrentIndex(currentIndex);
            session.setCurrentRoute(currentRoute);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            setUpItems();
        }
    }

    private void setUpItems() {
        currentPlace.setText(getString(R.string.home_current_place,
                currentRoute.getOrigin().toUpperCase(), currentRoute.getDestination().toUpperCase()));
        nextTransport.setText(formatString(nextList.get(0)));
        transportSuggestion1.setText(formatString(nextList.get(1)));
        transportSuggestion2.setText(formatString(nextList.get(2)));
        transportSuggestion3.setText(formatString(nextList.get(3)));
        currentDate.setText(DateConverter.convertToString(currentCalendar, "dd MMM").toUpperCase());
        currentDay.setText(DateConverter.convertToString(currentCalendar, "EEE").toUpperCase());
        currentType.setText(currentRoute.getType().toString());
        aboveTransport.setText(getString(R.string.home_above_transport, currentRoute.getType().toString().toLowerCase()));
        currentSeats.setText(getString(R.string.home_seats, currentRoute.getType().getSeats()));
        backImage.setImageResource(getIcon());
        if (prefs.getFavoriteRoute() == currentRoute.getRouteNo()) {
            favorite.setImageResource(R.drawable.icon_favorite);
        } else {
            favorite.setImageResource(R.drawable.icon_favorite_border);
        }
    }


    private String formatString(String string) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar = DateConverter.convertToCalender(ConverterMode.DATE_FIRST, string);
        } catch (ParseException e) {
            Log.error("Unable to parse " + string);
        }
        return DateConverter.convertToString(calendar, "hh:mm a").toUpperCase();
    }

    private int getIcon() {
        switch (currentRoute.getDestination().toLowerCase().trim()) {
            case "ncbs":
                return R.drawable.ncbs;
            case "mandara":
                return R.drawable.mandara;
            case "iisc":
                return R.drawable.iisc;
            case "icts":
                return R.drawable.icts;
            default:
                return R.drawable.unknown;
        }
    }


}
