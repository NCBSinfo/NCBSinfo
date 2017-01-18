package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
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
import com.rohitsuratekar.NCBSinfo.activities.background.TripHolder;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TransportRouteListAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TransportTripAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Trips;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.SetUpActivity;
import com.secretbiology.helpers.general.ConverterMode;
import com.secretbiology.helpers.general.DateConverter;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.views.ScrollUpRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.rohitsuratekar.NCBSinfo.activities.Helper.getType;

public class Transport extends BaseActivity {

    public static final String INTENT = "intent";
    public static final String ORIGIN = "origin";
    public static final String DESTINATION = "destination";
    public static final String TYPE = "type";

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

    private Calendar currentCalendar = Calendar.getInstance();
    private TransportTripAdapter leftAdapter;
    private TransportTripAdapter rightAdapter;
    private Trips currentTrip;
    private List<Trips> allTrips = new ArrayList<>();
    private List<String> leftTrips = new ArrayList<>();
    private List<String> rightTrips = new ArrayList<>();
    private int tempRoute;
    private TransportUI ui;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SetUpActivity(this, R.layout.transport, "Transport", false);
        ButterKnife.bind(this);

        tempRoute = 0;
        if (getIntent().getExtras() != null) {
            tempRoute = getIntent().getExtras().getInt(INTENT);
            currentPlace.setText(getString(R.string.home_current_place, getIntent().getExtras().getString(ORIGIN),
                    getIntent().getExtras().getString(DESTINATION)));
            currentDate.setText(DateConverter.convertToString(currentCalendar, "EE, dd MMM"));
            type.setText(getIntent().getExtras().getString(TYPE));
        }


        leftAdapter = new TransportTripAdapter(leftTrips, 0);
        rightAdapter = new TransportTripAdapter(rightTrips, 0);
        leftRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        rightRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        leftRecycler.setAdapter(leftAdapter);
        rightRecycler.setAdapter(rightAdapter);
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.DATE, -1);
                new LoadTransport().execute(getBaseContext());
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCalendar.add(Calendar.DATE, 2);
                new LoadTransport().execute(getBaseContext());
            }
        });

        new InitialTransport().execute(getBaseContext());
    }


    @OnClick(R.id.tp_bt_show_all)
    public void showAllRoutes() {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int height = size.y;
        bottomSheet.setPeekSheetTranslation(height / 2);
        bottomSheet.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.transport_route_sheet, bottomSheet, false));

        final TransportRouteListAdapter listAdapter = new TransportRouteListAdapter(allTrips);

        RecyclerView listRecycler = (RecyclerView) findViewById(R.id.tp_sheet_recycler);
        listRecycler.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        listRecycler.setAdapter(listAdapter);
        listAdapter.setOnItemClickListener(new TransportRouteListAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                bottomSheet.dismissSheet();
                currentCalendar = Calendar.getInstance();
                currentTrip = allTrips.get(position);
                new LoadTransport().execute(getBaseContext());
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAllTrips() {
        List<Trips> allTrips = new ArrayList<>();
        List<String[]> routeList = new RouteData(getBaseContext()).getRouteNames();
        for (String[] s : routeList) {
            List<RouteModel> models = new RouteData(getBaseContext()).getAllDays(s[0], s[1], getType(s[2]));
            allTrips.add(new Trips(models, currentCalendar));
        }
        this.allTrips = new ArrayList<>(allTrips);
        // setUpLayout();
    }

    private Trips getCurrentTrip(int i) {
        RouteModel model = new RouteData(getBaseContext()).getRouteByNumber(i);
        List<RouteModel> modelList = new RouteData(getBaseContext())
                .getAllDays(model.getOrigin(), model.getDestination(), model.getType());
        return new Trips(modelList, currentCalendar);
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

    class InitialTransport extends AsyncTask<Object, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Transport.this);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Object[] params) {
            Log.inform("Background activity started");
            currentTrip = getCurrentTrip(tempRoute);
            allTrips = TripHolder.getInstance().getAllTrips();
            ui = new TransportUI(currentTrip, getBaseContext());

            leftTrips.clear();
            leftTrips.addAll(formatList(ui.leftTrips()));
            leftAdapter.setCurrentItem(ui.leftIndex());

            rightTrips.clear();
            rightTrips.addAll(formatList(ui.rightTrips()));
            rightAdapter.setCurrentItem(ui.rightIndex());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            updateUI();

        }
    }

    class LoadTransport extends AsyncTask<Object, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Transport.this);
            dialog.setMessage("Loading...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Object[] params) {
            ui = new TransportUI(currentTrip, getBaseContext());
            leftTrips.clear();
            leftTrips.addAll(formatList(ui.leftTrips()));
            leftAdapter.setCurrentItem(ui.leftIndex());
            rightTrips.clear();
            rightTrips.addAll(formatList(ui.rightTrips()));
            rightAdapter.setCurrentItem(ui.rightIndex());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            updateUI();
        }
    }

    private void updateUI() {
        currentPlace.setText(getString(R.string.home_current_place, currentTrip.getOrigin().toUpperCase(),
                currentTrip.getDestination().toUpperCase()));
        currentDate.setText(DateConverter.convertToString(currentCalendar, "EE, dd MMM"));
        type.setText(ui.type());

        rightListTitle.setText(ui.rightTitle());
        leftListTitle.setText(ui.leftTitle());
        footNote.setText(ui.footnote());
        ui.leftButton(leftBtn, leftButtonText);
        ui.rightButton(rightBtn, rightButtonText);

        leftAdapter.notifyDataSetChanged();
        rightAdapter.notifyDataSetChanged();

        leftRecycler.scrollToPosition(ui.leftIndex());
        rightRecycler.scrollToPosition(ui.rightIndex());
    }

}
