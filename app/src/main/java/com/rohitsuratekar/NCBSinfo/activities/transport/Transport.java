package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.DefaultSettings;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TransportRouteListAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TransportTripAdapter;
import com.rohitsuratekar.NCBSinfo.database.route.Route;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.views.ScrollUpRecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Transport extends BaseActivity {

    public static final String INTENT = "intent";

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.TRANSPORT;
    }

    @BindView(R.id.tp_tx_place)
    TextView currentPlace;
    @BindView(R.id.tp_tx_date)
    TextView currentDate;
    @BindView(R.id.tp_tx_left_title)
    TextView leftListTitle;
    @BindView(R.id.tp_tx_right_title)
    TextView rightListTitle;
    @BindView(R.id.tp_tx_left)
    TextView leftButtonText;
    @BindView(R.id.tp_tx_right)
    TextView rightButtonText;
    @BindView(R.id.tp_tx_footnote)
    TextView footNote;

    @BindView(R.id.tp_bt_left)
    ImageButton leftBtn;
    @BindView(R.id.tp_bt_right)
    ImageButton rightBtn;

    @BindView(R.id.tp_bottom_sheet)
    BottomSheetLayout bottomSheet;
    @BindView(R.id.tp_left_recycler)
    ScrollUpRecyclerView leftRecycler;
    @BindView(R.id.tp_right_recycler)
    ScrollUpRecyclerView rightRecycler;

    private Calendar currentCalendar = Calendar.getInstance();
    private TransportTripAdapter leftAdapter;
    private TransportTripAdapter rightAdapter;
    private Route currentRoute;
    private List<Route> allRoutes;
    private List<String> leftTrips = new ArrayList<>();
    private List<String> rightTrips = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        allRoutes = DefaultSettings.getDefaultRoutes(getBaseContext());
        int c = 0;
        if (getIntent().getExtras() != null) {
            c = getIntent().getExtras().getInt(INTENT);
        }
        currentRoute = allRoutes.get(c);
        leftAdapter = new TransportTripAdapter(leftTrips, 0);
        rightAdapter = new TransportTripAdapter(rightTrips, 0);
        leftRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rightRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        leftRecycler.setAdapter(leftAdapter);
        rightRecycler.setAdapter(rightAdapter);
        setUpLayout();
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.DATE, -1);
                setUpLayout();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.DATE, 2);
                setUpLayout();
            }
        });
    }

    private void setUpLayout() {
        currentPlace.setText(getString(R.string.home_current_place, currentRoute.getOrigin().toUpperCase(),
                currentRoute.getDestination().toUpperCase()));
        currentDate.setText(DateConverter.convertToString(currentCalendar, "EE, dd MMM"));
        setUpLists();
    }


    private void setUpLists() {
        leftTrips.clear();
        rightTrips.clear();
        for (String s : currentRoute.getTrips().leftDay(currentCalendar).getTripList()) {
            leftTrips.add(formatString(s));
        }
        for (String s : currentRoute.getTrips().rightDay(currentCalendar).getTripList()) {
            rightTrips.add(formatString(s));
        }
        leftAdapter.setCurrentItem(currentRoute.getTrips().leftIndex(currentCalendar));
        rightAdapter.setCurrentItem(currentRoute.getTrips().rightIndex(currentCalendar));

        currentRoute.getTrips().setLeftButton(leftBtn, leftButtonText, currentCalendar);
        currentRoute.getTrips().setRightButton(rightBtn, rightButtonText, currentCalendar);

        leftListTitle.setText(currentRoute.getTrips().leftListTitle(getBaseContext(), currentCalendar));
        rightListTitle.setText(currentRoute.getTrips().rightListTitle(getBaseContext(), currentCalendar));

        footNote.setText(currentRoute.getTrips().footnote(getBaseContext(), currentCalendar));
        leftAdapter.notifyDataSetChanged();
        rightAdapter.notifyDataSetChanged();
    }


    @OnClick(R.id.tp_bt_show_all)
    public void showAll() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        bottomSheet.setPeekSheetTranslation(height / 2);
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.transport_route_sheet, bottomSheet, false));

        final TransportRouteListAdapter listAdapter = new TransportRouteListAdapter(allRoutes);

        RecyclerView listRecycler = (RecyclerView) findViewById(R.id.tp_sheet_recycler);
        listRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listRecycler.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new TransportRouteListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                currentCalendar = Calendar.getInstance();
                currentRoute = allRoutes.get(position);
                setUpLayout();
                bottomSheet.dismissSheet();
            }
        });

    }

    private String formatString(String s) {
        try {
            return DateConverter.convertToString(DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s), "hh:mm a");
        } catch (ParseException e) {
            return s;
        }
    }

}
