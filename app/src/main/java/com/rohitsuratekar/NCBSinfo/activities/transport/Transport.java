package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.ok;

public class Transport extends BaseActivity implements TransportFragment.OnRouteSelected {

    public static final String ROUTE = "routeNo";

    @BindView(R.id.tp_recycler)
    RecyclerView recyclerView;
    @BindViews({R.id.tp_day_1, R.id.tp_day_2, R.id.tp_day_3, R.id.tp_day_4, R.id.tp_day_5, R.id.tp_day_6, R.id.tp_day_7})
    List<TextView> daysList;
    @BindView(R.id.tp_title)
    TextView title;
    @BindView(R.id.tp_swap)
    ImageView swapButton;
    @BindView(R.id.tp_transport_type)
    TextView type;
    @BindView(R.id.tp_last_update)
    TextView lastUpdate;


    private List<RouteData> routeDataList = new ArrayList<>();
    private TransportViewModel viewModel;
    private TransportAdapter adapter;
    private List<String> tripList;
    private Calendar currentCalender;
    private TransportDetails currentDetails;
    private boolean showActual = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport);
        ButterKnife.bind(this);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.transport);
        currentCalender = Calendar.getInstance();

        tripList = new ArrayList<>();
        adapter = new TransportAdapter(tripList, -1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        //Use view model after view binding
        viewModel = ViewModelProviders.of(this).get(TransportViewModel.class);
        subscribe();

        int id = getIntent().getIntExtra(ROUTE, -1);
        viewModel.loadRoute(getApplicationContext(), id);
        changeDay(daysList.get(currentCalender.get(Calendar.DAY_OF_WEEK) - 1));

        //New test for custom events for analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("transport_accessed", General.timeStamp());
        mFirebaseAnalytics.logEvent("transport", params);

    }

    private void changeDay(TextView textView) {
        for (TextView t : daysList) {
            ViewGroup.LayoutParams params = t.getLayoutParams();
            if (t.equals(textView)) {
                params.width = (int) getResources().getDimension(R.dimen.tp_day_width_expanded);
                t.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
            } else {
                params.width = (int) getResources().getDimension(R.dimen.tp_day_width);
                t.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryLight));
            }
            t.setLayoutParams(params);
        }
    }

    @OnClick({R.id.tp_day_1, R.id.tp_day_2, R.id.tp_day_3, R.id.tp_day_4, R.id.tp_day_5, R.id.tp_day_6, R.id.tp_day_7})
    public void onDayClick(TextView textView) {
        changeDay(textView);
        currentCalender.set(Calendar.DAY_OF_WEEK, daysList.indexOf(textView) + 1);
        if (currentDetails != null) {
            updateUI();
        }
    }

    @OnClick(R.id.tp_show_all)
    public void showBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment;
        if (currentDetails != null) {
            bottomSheetDialogFragment = TransportFragment.newInstance(currentDetails.getRouteID(), currentDetails.getReturnIndex());
        } else {
            bottomSheetDialogFragment = TransportFragment.newInstance(-1, -1);
        }
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    @OnClick(R.id.tp_swap)
    public void swapRoute() {
        viewModel.loadRoute(getApplicationContext(), currentDetails.getReturnIndex());
    }


    @Override
    protected int setNavigationMenu() {
        return R.id.nav_transport;
    }

    @Override
    public void selected(int routeID) {
        viewModel.loadRoute(getApplicationContext(), routeID);
    }

    public List<RouteData> getRouteDataList() {
        return routeDataList;
    }

    private void subscribe() {
        viewModel.getCurrentDetails().observe(this, new Observer<TransportDetails>() {
            @Override
            public void onChanged(@Nullable TransportDetails transportDetails) {
                if (transportDetails != null) {
                    currentDetails = transportDetails;
                    try {
                        showActual = true;
                        String[] d = transportDetails.getNextTripDetails(Calendar.getInstance());
                        currentDetails.setOriginalTrip(d);
                        Calendar cal = Calendar.getInstance();
                        switch (d[1]) {
                            case "2":
                                cal.add(Calendar.DATE, 1);
                                currentDetails.setOriginalDay(cal.get(Calendar.DAY_OF_WEEK));
                                break;
                            case "-1":
                                cal.add(Calendar.DATE, -1);
                                currentDetails.setOriginalDay(cal.get(Calendar.DAY_OF_WEEK));
                                break;
                            default:
                                currentDetails.setOriginalDay(Calendar.getInstance().get(Calendar.DAY_OF_WEEK));
                                break;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    updateUI();
                }
            }
        });

        viewModel.getAllRoutes().observe(this, new Observer<List<RouteData>>() {
            @Override
            public void onChanged(@Nullable List<RouteData> r) {
                if (r != null) {
                    routeDataList.clear();
                    routeDataList.addAll(r);
                }
            }
        });

        viewModel.getShowError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    new AlertDialog.Builder(Transport.this)
                            .setTitle("Oh no!")
                            .setCancelable(false)
                            .setMessage(getString(R.string.tp_no_route_warning))
                            .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    showBottomSheet();
                                }
                            }).show();
                }
            }
        });
    }


    private void updateUI() {
        tripList.clear();
        tripList.addAll(currentDetails.getTrips(currentCalender));
        if (currentCalender.get(Calendar.DAY_OF_WEEK) == currentDetails.getOriginalDay()) {
            adapter.updateNext(tripList.indexOf(currentDetails.getOriginalTrip()[0]));
        } else if (currentCalender.get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
                && currentDetails.getOriginalTrip()[1].equals("2")) {
            if (showActual) {
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DATE, 1);
                onDayClick(daysList.get(c.get(Calendar.DAY_OF_WEEK) - 1));
                showActual = false;
            } else {
                adapter.updateNext(tripList.size());
            }
        } else {
            adapter.updateNext(-1);
        }
        adapter.updateMessage(getString(R.string.tp_next_transport, currentDetails.getType()));
        adapter.notifyDataSetChanged();

        title.setText(getString(R.string.tp_route_name,
                currentDetails.getOrigin().toUpperCase(),
                currentDetails.getDestination().toUpperCase()));
        type.setText(currentDetails.getType());

        if (currentDetails.isReturnAvailable()) {
            swapButton.setVisibility(View.VISIBLE);
        } else {
            swapButton.setVisibility(View.INVISIBLE);
        }

        //Scroll to next trip
        recyclerView.smoothScrollToPosition(adapter.getScrollPosition());
        try {
            Log.inform(currentDetails.getRouteData().getModifiedOn());
            lastUpdate.setText(getString(R.string.tp_last_updated,
                    currentDetails.getRouteData().getAuthor(),
                    DateConverter.changeFormat(ConverterMode.DATE_FIRST, currentDetails.getRouteData().getModifiedOn(), "dd MMM yy")));
        } catch (ParseException | NullPointerException e) {
            lastUpdate.setText("--");
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.tp_menu, menu);

        Drawable drawable = menu.findItem(R.id.action_edit).getIcon();

        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.white));
        menu.findItem(R.id.action_edit).setIcon(drawable);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit: {
                General.makeShortToast(getApplicationContext(), "Editing Transport is coming soon!");
                break;
            }
            // case blocks for other MenuItems (if any)
        }
        return false;
    }

}
