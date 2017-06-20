package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Transport extends BaseActivity implements TransportFragment.OnRouteSelected {

    public static final String ROUTE = "route";

    @BindViews({R.id.tp_recycler_left, R.id.tp_recycler_right})
    List<RecyclerView> recyclerViews;
    @BindViews({R.id.tp_title_left, R.id.tp_title_right})
    List<TextView> titles;
    @BindView(R.id.tp_footnote)
    TextView footnote;
    @BindView(R.id.tp_route_name)
    TextView name;
    @BindView(R.id.tp_type)
    TextView type;
    @BindView(R.id.tp_date)
    TextView date;
    @BindView(R.id.tp_image)
    ImageView image;
    @BindView(R.id.tp_fab)
    FloatingActionButton fab;

    private List<TransportAdapter> adapters = new ArrayList<>();
    private List<String> leftList = new ArrayList<>();
    private List<String> rightList = new ArrayList<>();
    private TransportViewModel viewModel;
    private int reverseRoute = 0;
    private List<RouteData> routeDataList = new ArrayList<>();
    private int currentRoute = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport);
        ButterKnife.bind(this);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.transport);
        adapters.add(new TransportAdapter(-1, leftList));
        adapters.add(new TransportAdapter(-1, rightList));
        viewModel = ViewModelProviders.of(this).get(TransportViewModel.class);
        recyclerViews.get(0).setAdapter(adapters.get(0));
        recyclerViews.get(1).setAdapter(adapters.get(1));
        recyclerViews.get(0).setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViews.get(1).setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewModel.retrieveRouteList(getApplicationContext());
        subscribe();
        int i = getIntent().getIntExtra(ROUTE, -1);
        if (i != -1) {
            changeRoute(i);
        }

    }

    public List<RouteData> getRouteDataList() {
        return routeDataList;
    }

    private void changeRoute(int routeNo) {
        viewModel.loadRoute(getApplicationContext(), routeNo, Calendar.getInstance());
    }

    @OnClick(R.id.tp_fab)
    public void reverseRoute() {
        if (reverseRoute != 0) {
            changeRoute(reverseRoute);
        } else {
            General.makeLongToast(getApplicationContext(), "No reverse route found.");
        }

    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_transport;
    }

    @OnClick(R.id.tp_show_routes)
    public void showBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment = TransportFragment.newInstance(currentRoute, reverseRoute);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }

    private void subscribe() {
        viewModel.getCurrentModel().observe(this, new Observer<TransportModel>() {
            @Override
            public void onChanged(@Nullable TransportModel transportModel) {
                if (transportModel != null) {
                    currentRoute = transportModel.getRouteID();
                    setUI(transportModel);
                }
            }
        });
        viewModel.getReturnTrip().observe(this, new Observer<Object[]>() {
            @Override
            public void onChanged(@Nullable Object[] objects) {
                if (objects != null) {
                    //// TODO: 20-06-17
                    boolean isAvailable = (boolean) objects[0];
                    reverseRoute = (int) objects[1];
                    if (isAvailable) {
                        fab.setVisibility(View.VISIBLE);
                    } else {
                        fab.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
        viewModel.getRouteList().observe(this, new Observer<List<RouteData>>() {
            @Override
            public void onChanged(@Nullable List<RouteData> rList) {
                if (rList != null) {
                    routeDataList.clear();
                    routeDataList.addAll(rList);
                }
            }
        });
    }

    private void setUI(TransportModel model) {
        name.setText(getString(R.string.home_card_title, model.getOrigin().toUpperCase(), model.getDestination().toUpperCase()));
        type.setText(model.getType());
        footnote.setText(model.getFootnote());
        leftList.clear();
        leftList.addAll(model.getLeftTrips());
        rightList.clear();
        rightList.addAll(model.getRightTrips());
        adapters.get(0).setNextTripIndex(model.getLeftIndex());
        adapters.get(1).setNextTripIndex(model.getRightIndex());
        adapters.get(0).notifyDataSetChanged();
        adapters.get(1).notifyDataSetChanged();
        titles.get(0).setText(model.getLeftTitle());
        titles.get(1).setText(model.getRightTitle());
        date.setText(DateConverter.convertToString(model.getCalendar(), "EEE,dd MMM yyyy"));
        image.setImageResource(model.getImage());

    }

    @Override
    public void selected(int routeID) {
        changeRoute(routeID);
    }
}
