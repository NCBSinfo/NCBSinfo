package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Transport extends BaseActivity implements TransportFragment.OnRouteSelected {

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


    private List<RouteData> routeDataList = new ArrayList<>();
    private TransportViewModel viewModel;
    private TransportAdapter adapter;
    private List<String> tripList;
    private Calendar currentCalender;
    private TransportDetails currentDetails;

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

        //TODO : Add proper indent for route No
        viewModel.loadRoute(getApplicationContext(), 1);
    }

    void changeDay(TextView textView) {
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
        if (currentDetails != null) {
            currentCalender.set(Calendar.DAY_OF_WEEK, daysList.indexOf(textView) + 1);
            updateUI();
        }
    }

    @OnClick(R.id.tp_show_all)
    public void showBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment = TransportFragment.newInstance(currentDetails.getRouteID(), currentDetails.getReturnIndex());
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
    }

    private void updateUI() {
        tripList.clear();
        tripList.addAll(currentDetails.getTrips(currentCalender));
        try {
            String[] d = currentDetails.getNextTripDetails(currentCalender);
            if (d[1].equals("0")) {
                if (currentCalender.get(Calendar.DAY_OF_WEEK) == Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) {
                    adapter.updateNext(tripList.indexOf(d[0]));
                } else {
                    adapter.updateNext(-1);
                }
            }
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Some error!", Toast.LENGTH_SHORT).show();
        }
        adapter.updateMessage(getString(R.string.tp_next_transport, currentDetails.getType()));
        adapter.notifyDataSetChanged();
        changeDay(daysList.get(currentCalender.get(Calendar.DAY_OF_WEEK) - 1));

        title.setText(getString(R.string.tp_route_name,
                currentDetails.getOrigin().toUpperCase(),
                currentDetails.getDestination().toUpperCase()));
        type.setText(currentDetails.getType());

        if (currentDetails.isReturnAvailable()) {
            swapButton.setVisibility(View.VISIBLE);
        } else {
            swapButton.setVisibility(View.INVISIBLE);
        }

    }

}
