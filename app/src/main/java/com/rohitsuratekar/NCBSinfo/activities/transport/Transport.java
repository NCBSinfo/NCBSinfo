package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TransportRouteListAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TransportTripAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;
import com.secretbiology.helpers.general.views.ScrollUpRecyclerView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Transport extends BaseActivity {

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
    @BindView(R.id.tp_tx_type)
    TextView type;

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

    @BindView(R.id.tp_bt_swap)
    FloatingActionButton swap;


    private CurrentSession session = CurrentSession.getInstance();
    private Calendar currentCalendar = Calendar.getInstance();
    private TransportTripAdapter leftAdapter;
    private TransportTripAdapter rightAdapter;
    private Route currentRoute;
    private List<String> leftTrips = new ArrayList<>();
    private List<String> rightTrips = new ArrayList<>();
    private int leftIndex = 0;
    private int rightIndex = 0;
    private boolean oppositeRouteExists;
    private int oppositeRouteIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        currentRoute = session.getCurrentRoute();
        leftAdapter = new TransportTripAdapter(leftTrips, leftIndex);
        rightAdapter = new TransportTripAdapter(rightTrips, rightIndex);
        leftRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rightRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        leftRecycler.setAdapter(leftAdapter);
        rightRecycler.setAdapter(rightAdapter);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.DATE, -1);
                new setUpLayout().execute();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.DATE, 2);
                new setUpLayout().execute();
            }
        });

        swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oppositeRouteExists) {
                    currentRoute = session.getAllRoutes().get(oppositeRouteIndex);
                    session.setCurrentRoute(currentRoute);
                    session.setCurrentIndex(oppositeRouteIndex);
                    new setUpLayout().execute();
                }
            }
        });

        updateUI();
        new setUpLayout().execute();

    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.TRANSPORT;
    }

    @OnClick(R.id.tp_bt_show_all)
    public void showAllRoutes() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        bottomSheet.setPeekSheetTranslation(height / 2);
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.transport_route_sheet, bottomSheet, false));

        TextView sheetTitle = (TextView) findViewById(R.id.tp_bs_title);
        sheetTitle.setText(getString(R.string.transport_sheet_title, session.getAllRoutes().size()));
        final TransportRouteListAdapter listAdapter = new TransportRouteListAdapter(session.getAllRoutes());
        RecyclerView listRecycler = (RecyclerView) findViewById(R.id.tp_sheet_recycler);
        listRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listRecycler.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new TransportRouteListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                currentCalendar = Calendar.getInstance();
                currentRoute = session.getAllRoutes().get(position);
                session.setCurrentRoute(currentRoute);
                session.setCurrentIndex(session.getAllRoutes().indexOf(currentRoute));
                new setUpLayout().execute();
                bottomSheet.dismissSheet();
            }
        });

    }

    private class setUpLayout extends AsyncTask<Object, Void, Void> {

        @Override
        protected Void doInBackground(Object... params) {
            boolean nextIsToday = new TransportMethods().isNextTransportToday(currentCalendar, currentRoute);
            String nextTransport = formatList(new TransportMethods().nextTransport(currentCalendar, currentRoute)).get(0);
            leftTrips.clear();
            rightTrips.clear();

            if (currentRoute.isRegular()) {
                leftTrips.addAll(formatList(currentRoute.getDefaultList()));
                rightTrips.addAll(formatList(currentRoute.getMap().get(Calendar.SUNDAY)));
                if (currentCalendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                    leftIndex = leftTrips.indexOf(nextTransport);
                    rightIndex = -1;
                    if (leftIndex == -1) {
                        rightIndex = rightTrips.indexOf(nextTransport);
                    }
                } else {
                    leftIndex = -1;
                    rightIndex = rightTrips.indexOf(nextTransport);
                    if (rightIndex == -1) {
                        leftIndex = leftTrips.indexOf(nextTransport);
                    }
                }
            } else {
                leftTrips.addAll(formatList(currentRoute.getMap().get(Calendar.DAY_OF_WEEK, currentRoute.getDefaultList())));
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_WEEK, currentCalendar.get(Calendar.DAY_OF_WEEK));
                c.add(Calendar.DATE, 1);
                rightTrips.addAll(formatList(currentRoute.getMap().get(c.get(Calendar.DAY_OF_WEEK), currentRoute.getDefaultList())));
                if (nextIsToday) {
                    leftIndex = leftTrips.indexOf(nextTransport);
                    rightIndex = -1;
                } else {
                    leftIndex = leftTrips.size();
                    rightIndex = rightTrips.indexOf(nextTransport);
                }
            }

            //Check if route from other direction exists
            int oppositeRoute = new RouteData(getBaseContext()).checkIfExistsRoute(
                    currentRoute.getDestination(), currentRoute.getOrigin(), currentRoute.getType());
            oppositeRouteExists = oppositeRoute != -1;
            if (oppositeRouteExists) {
                for (int i = 0; i < session.getAllRoutes().size(); i++) {
                    if (session.getAllRoutes().get(i).getRouteNo() == oppositeRoute) {
                        oppositeRouteIndex = i;
                        break;
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateUI();
        }
    }

    private void updateUI() {
        currentPlace.setText(getString(R.string.home_current_place, currentRoute.getOrigin().toUpperCase(),
                currentRoute.getDestination().toUpperCase()));
        currentDate.setText(DateConverter.convertToString(currentCalendar, "EE, dd MMM"));
        type.setText(currentRoute.getType().toString());

        if (currentRoute.isRegular()) {
            leftListTitle.setText(getString(R.string.weekdays));
            rightListTitle.setText(getString(R.string.sunday));
            leftBtn.setImageResource(android.R.color.transparent);
            rightBtn.setImageResource(android.R.color.transparent);
            leftBtn.setEnabled(false);
            rightBtn.setEnabled(false);
            leftButtonText.setText("");
            rightButtonText.setText("");
            footNote.setText(getString(R.string.transport_regular_note));
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(currentCalendar.getTime());
            leftListTitle.setText(DateConverter.convertToString(cal, "EEEE"));
            cal.add(Calendar.DATE, 1);
            rightListTitle.setText(DateConverter.convertToString(cal, "EEEE"));
            leftBtn.setImageResource(R.drawable.icon_left);
            rightBtn.setImageResource(R.drawable.icon_right);
            leftBtn.setEnabled(true);
            rightBtn.setEnabled(true);
            cal.add(Calendar.DATE, 1);
            rightButtonText.setText(DateConverter.convertToString(cal, "EEE").toUpperCase());
            cal.add(Calendar.DATE, -3);
            leftButtonText.setText(DateConverter.convertToString(cal, "EEE").toUpperCase());
            footNote.setText(getString(R.string.transport_other_note));
        }


        if (oppositeRouteExists) {
            swap.setEnabled(true);
        } else {
            swap.setEnabled(false);
        }

        leftAdapter.setCurrentItem(leftIndex);
        rightAdapter.setCurrentItem(rightIndex);
        leftAdapter.notifyDataSetChanged();
        rightAdapter.notifyDataSetChanged();

        leftRecycler.scrollToPosition(leftIndex);
        rightRecycler.scrollToPosition(rightIndex);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (!drawer.isDrawerOpen(GravityCompat.START)) {
            leftRecycler.setVisibility(View.INVISIBLE);
            rightRecycler.setVisibility(View.INVISIBLE);
        }
        super.onBackPressed();
    }

    private List<String> formatList(List<String> list) {
        List<String> formattedList = new ArrayList<>();
        for (String s : list) {
            formattedList.add(formatString(s));
        }
        return formattedList;
    }

    private String formatString(String s) {
        try {
            return DateConverter.convertToString(DateConverter.convertToCalender(ConverterMode.DATE_FIRST, s), "hh:mm a");
        } catch (ParseException e) {
            return s;
        }
    }


}
