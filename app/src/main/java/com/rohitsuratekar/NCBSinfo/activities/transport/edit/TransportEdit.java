package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.confirm.ConfirmDetailsFragment;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.route.AddLocationFragment;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.trips.AddTripsFragment;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.rohitsuratekar.NCBSinfo.ui.RestrictedSwipeView;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransportEdit extends AppCompatActivity implements AddLocationFragment.OnStateChanged,
        AddTripsFragment.OnStateChanged, ConfirmDetailsFragment.OnStateChanged {

    public static final String USE_RIGHT = "use_right";
    public static final String USE_LEFT = "use_left";

    private static final int ROUTE = 0;
    private static final int TRIPS = 1;
    private static final int CONFIRM = 2;

    @BindView(R.id.te_img_route)
    ImageView routeIcon;
    @BindView(R.id.te_img_trip)
    ImageView tripIcon;
    @BindView(R.id.te_img_confirm)
    ImageView confirmIcon;

    @BindView(R.id.te_btn_cancel)
    Button cancelButton;
    @BindView(R.id.te_btn_next)
    Button nextButton;
    @BindView(R.id.te_btn_previous)
    Button previousButton;

    @BindView(R.id.transport_viewpager)
    RestrictedSwipeView pager;

    private CurrentStateModel currentInformation;
    private TransportEditState currentState;
    private List<TransportEditState> stateList;
    private ViewpagerAdapter adapter;
    private MenuItem menuItem;
    private boolean isForEdit;
    private int day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_edit);
        ButterKnife.bind(this);

        String action = getIntent().getAction();
        if (action != null) {
            isForEdit = true;
            switch (action) {
                case USE_RIGHT:
                    day = Calendar.SUNDAY + 2; //Check array
                    break;
                case USE_LEFT:
                    day = 0;
                    break;
                default:
                    day = Integer.parseInt(action);
                    break;
            }
        }

        currentInformation = new CurrentStateModel();

        stateList = new ArrayList<>();
        stateList.add(new TransportEditState(ROUTE, routeIcon));
        stateList.add(new TransportEditState(TRIPS, tripIcon));
        stateList.add(new TransportEditState(CONFIRM, confirmIcon));
        currentState = stateList.get(ROUTE);
        currentState.setOpened(true);
        setTitle(getString(R.string.transport_add));


        pager = (RestrictedSwipeView) findViewById(R.id.transport_viewpager);
        setupViewPager(pager);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //Change from location before assigning new location
                currentState.changingToLocation(position);
                currentState = stateList.get(position);
                currentState.setOpened(true);
                setIcons();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setIcons();

        if (isForEdit) {
            setUpForEdit();
        }
    }

    public CurrentStateModel getInfo() {
        return currentInformation;
    }

    @OnClick(R.id.te_img_route)
    public void gotoLocation() {
        pager.setCurrentItem(ROUTE, true);
        currentState.changingToLocation(ROUTE);
        currentState = stateList.get(ROUTE);
        currentState.setOpened(true);
        setIcons();
    }

    @OnClick(R.id.te_img_trip)
    public void gotoTrips() {
        pager.setCurrentItem(TRIPS, true);
        currentState.changingToLocation(TRIPS);
        currentState = stateList.get(TRIPS);
        currentState.setOpened(true);
        setIcons();
    }

    @OnClick(R.id.te_img_confirm)
    public void gotoConfirm() {
        pager.setCurrentItem(CONFIRM, true);
        currentState.changingToLocation(CONFIRM);
        currentState = stateList.get(CONFIRM);
        currentState.setOpened(true);
        setIcons();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done_menu, menu);
        if (menu.findItem(R.id.action_done) != null) {
            menuItem = menu.findItem(R.id.action_done);
            menuItem.getIcon().setColorFilter(General.getColor(getBaseContext(), R.color.colorPrimaryLight), PorterDuff.Mode.SRC_ATOP);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddLocationFragment(), "Locations");
        adapter.addFragment(new AddTripsFragment(), "Trips");
        adapter.addFragment(new ConfirmDetailsFragment(), "Confirm");
        viewPager.setAdapter(adapter);
    }


    public void onStateSelected(List<String> currentTrips) {
        ConfirmDetailsFragment confirm = (ConfirmDetailsFragment) adapter.getItem(2);
        if (confirm != null && confirm.getView() != null) {
            List<TransportRecyclerItem> items = new ArrayList<>();
            for (String s : currentTrips) {
                items.add(new TransportRecyclerItem(s, false));
            }
            confirm.setList(items);
            int loc = 0;
            for (TransportEditState t : stateList) {
                if (t.getLocation() == CONFIRM) {
                    loc = stateList.indexOf(t);
                }
            }
            stateList.get(loc).setCompleted(false);
            stateList.get(loc).setSkipped(true);
            setIcons();

        }
    }


    public void setIcons() {
        for (TransportEditState state : stateList) {
            checkColor(state);

        }
        if (currentState.getLocation() == ROUTE) {
            previousButton.setEnabled(false);
            previousButton.setText("");
            nextButton.setEnabled(true);
            nextButton.setText("NEXT");
            //cancelButton.setText("Cancel");
        } else if (currentState.getLocation() == CONFIRM) {
            previousButton.setEnabled(true);
            previousButton.setText("Previous");
            nextButton.setEnabled(false);
            nextButton.setText("");
            // cancelButton.setText("Done");
        } else {
            previousButton.setEnabled(true);
            previousButton.setText("PREVIOUS");
            nextButton.setEnabled(true);
            nextButton.setText("NEXT");
            // cancelButton.setText("Cancel");
        }

        if (menuItem != null) {
            if (stateList.get(0).isCompleted() && stateList.get(1).isCompleted() && stateList.get(2).isCompleted()) {
                menuItem.setVisible(true);
            } else {
                menuItem.setVisible(false);
            }
        }
    }

    @OnClick(R.id.te_btn_next)
    public void nextTrigger() {
        currentState.changingToLocation(currentState.getLocation() + 1);
        currentState = stateList.get(currentState.getLocation() + 1);
        pager.setCurrentItem(currentState.getLocation(), true);
        currentState.setOpened(true);
        setIcons();
    }

    @OnClick(R.id.te_btn_previous)
    public void previousTrigger() {
        currentState.changingToLocation(currentState.getLocation() - 1);
        currentState = stateList.get(currentState.getLocation() - 1);
        pager.setCurrentItem(currentState.getLocation(), true);
        currentState.setOpened(true);
        setIcons();
    }

    @OnClick(R.id.te_btn_cancel)
    public void cancelTrigger() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void checkColor(TransportEditState state) {
        if (state.isOpened()) {
            if (state.isSkipped()) {
                state.getImageView().setColorFilter(General.getColor(getBaseContext(), R.color.colorAccent));
            } else {
                if (state.isCompleted()) {
                    state.getImageView().setColorFilter(General.getColor(getBaseContext(), R.color.green));
                }
            }
        } else {
            state.getImageView().setColorFilter(General.getColor(getBaseContext(), R.color.colorPrimaryLight));
        }

    }

    @Override
    public void onLocationChanged(String origin, String destination) {
        currentInformation.setOrigin(origin);
        currentInformation.setDestination(destination);
        currentState.setCompleted(true);
        currentState.setSkipped(false);
        setIcons();
    }

    @Override
    public void onLocationRemoved(String origin, String destination) {
        currentInformation.setOrigin(origin);
        currentInformation.setDestination(destination);
        currentState.setCompleted(false);
        currentState.setSkipped(true);
        setIcons();
    }

    @Override
    public void onTripChanged(List<String> currentTrips, int daySelected) {
        currentInformation.setRouteList(currentTrips);

        if (currentTrips.size() == 0) {
            currentState.setCompleted(false);
            currentState.setSkipped(true);
        } else {
            currentState.setCompleted(true);
            currentState.setSkipped(false);
        }

        //Notify confirm fragment
        onStateSelected(currentTrips);
        setDaySelected(daySelected);
        setIcons();
    }

    @Override
    public void onTripSelected(String selected) {
        currentInformation.setFirstTrip(selected);
        currentState.setCompleted(true);
        currentState.setSkipped(false);
        setIcons();
    }

    private void setUpForEdit() {
        setTitle(getString(R.string.transport_edit));
        Route route = CurrentSession.getInstance().getCurrentRoute();

        for (TransportEditState state : stateList) {
            state.setSkipped(false);
            state.setOpened(true);
            state.setCompleted(true);
        }

        setDaySelected(day);
        currentInformation.setOrigin(route.getOrigin());
        currentInformation.setDestination(route.getDestination());

        List<String> formattedList = new ArrayList<>();

        for (String s : route.getDefaultList()) {
            try {
                formattedList.add(DateConverter.changeFormat(ConverterMode.DATE_FIRST, s, "hh:mm a"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        currentInformation.setFirstTrip(formattedList.get(0));
        currentInformation.setRouteList(formattedList);

        setIcons();

    }

    public boolean isForEdit() {
        return isForEdit;
    }

    public int getDay() {
        return day;
    }

    private void setDaySelected(int i) {
        switch (i) {
            case 0:
                currentInformation.setDay(Calendar.MONDAY);
                break;
            case 1:
                currentInformation.setDay(Calendar.MONDAY);
                break;
            case 2:
                currentInformation.setDay(Calendar.SUNDAY);
                break;
            case 3:
                currentInformation.setDay(Calendar.SUNDAY);
                break;
            case 4:
                currentInformation.setDay(Calendar.MONDAY);
                break;
            case 5:
                currentInformation.setDay(Calendar.TUESDAY);
                break;
            case 6:
                currentInformation.setDay(Calendar.WEDNESDAY);
                break;
            case 7:
                currentInformation.setDay(Calendar.THURSDAY);
                break;
            case 8:
                currentInformation.setDay(Calendar.FRIDAY);
                break;
            case 9:
                currentInformation.setDay(Calendar.SATURDAY);
                break;
        }
    }
}
