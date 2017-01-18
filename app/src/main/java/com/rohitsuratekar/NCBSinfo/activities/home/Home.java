package com.rohitsuratekar.NCBSinfo.activities.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.background.TripHolder;
import com.rohitsuratekar.NCBSinfo.activities.background.events.TransportEvent;
import com.rohitsuratekar.NCBSinfo.activities.transport.Transport;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Trips;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.OnClickGuard;
import com.rohitsuratekar.NCBSinfo.ui.SetUpActivity;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.OnSwipeTouchListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.rohitsuratekar.NCBSinfo.activities.Helper.getType;

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

    private Trips currentTrip;
    private int currentIndex = 0;
    private List<SuggestionModel> allSuggestions = new ArrayList<>();
    private HomeAdapter adapter;
    private List<Trips> allTrips = new ArrayList<>();
    private Calendar currentCalendar = Calendar.getInstance();
    private int tempRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SetUpActivity(this, R.layout.home, "Home", false);
        ButterKnife.bind(this);
        //TODO
        tempRoute = 0;
        currentTrip = getCurrentTrip(tempRoute);

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

        setUpLayout();

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, Transport.class);
                i.putExtra(Transport.INTENT, currentTrip.getRouteNo());
                i.putExtra(Transport.ORIGIN, currentTrip.getOrigin().toUpperCase());
                i.putExtra(Transport.DESTINATION, currentTrip.getDestination().toUpperCase());
                i.putExtra(Transport.DESTINATION, currentTrip.getDestination().toUpperCase());
                i.putExtra(Transport.TYPE, currentTrip.getType().toString());
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void goRight() {
        currentIndex++;
        if (currentIndex == allTrips.size()) {
            currentIndex = 0;
        }
        currentTrip = allTrips.get(currentIndex);
        setUpLayout();
    }

    private void goLeft() {
        currentIndex--;
        if (currentIndex < 0) {
            currentIndex = allTrips.size() - 1;
        }
        currentTrip = allTrips.get(currentIndex);
        setUpLayout();
    }

    public void setUpLayout() {
        List<String> next = currentTrip.nextTransport();
        currentPlace.setText(getString(R.string.home_current_place,
                currentTrip.getOrigin().toUpperCase(), currentTrip.getDestination().toUpperCase()));
        nextTransport.setText(formatString(next.get(0)));
        transportSuggestion1.setText(formatString(next.get(1)));
        transportSuggestion2.setText(formatString(next.get(2)));
        transportSuggestion3.setText(formatString(next.get(3)));
        currentDate.setText(DateConverter.convertToString(currentCalendar, "dd MMM").toUpperCase());
        currentDay.setText(DateConverter.convertToString(currentCalendar, "EEE").toUpperCase());
        currentType.setText(currentTrip.getType().toString());
        aboveTransport.setText(getString(R.string.home_above_transport, currentTrip.getType().toString().toLowerCase()));
        backImage.setImageResource(getIcon());
    }

    private Trips getCurrentTrip(int i) {
        RouteModel model = new RouteData(getBaseContext()).getRouteByNumber(i);
        Log.inform(model.getOrigin());
        List<RouteModel> modelList = new RouteData(getBaseContext())
                .getAllDays(model.getOrigin(), model.getDestination(), model.getType());
        return new Trips(modelList, currentCalendar);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllTrips(TransportEvent event) {
        Log.inform(event.retrive());
        List<String[]> routeList = new RouteData(getBaseContext()).getRouteNames();
        for (String[] s : routeList) {
            List<RouteModel> models = new RouteData(getBaseContext()).getAllDays(s[0], s[1], getType(s[2]));
            allTrips.add(new Trips(models, currentCalendar));
        }
        for (int i = 0; i < allTrips.size(); i++) {
            if (allTrips.get(i).getRouteNo() == tempRoute) {
                currentIndex = i;
                break;
            }
        }
        TripHolder.getInstance().setAllTrips(allTrips);
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
        switch (currentTrip.getDestination().toLowerCase().trim()) {
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


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        EventBus.getDefault().post(new TransportEvent());
    }


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
