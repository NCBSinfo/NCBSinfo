package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.graphics.Point;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.flipboard.bottomsheet.BottomSheetLayout;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.SelectRouteAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.TripAdapter;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.views.ScrollUpRecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Transport extends BaseActivity {

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.TRANSPORT;
    }

    @BindView(R.id.tp_week_list)
    ScrollUpRecyclerView weekList;
    @BindView(R.id.tp_sunday_list)
    ScrollUpRecyclerView sundayList;

    @BindView(R.id.transport_bottomsheet)
    BottomSheetLayout allRoutes;

    @BindView(R.id.tp_all_route_img)
    ImageView allRouteImg;

    @BindView(R.id.tp_bottom_note)
    TextView bottomNote;
    @BindView(R.id.tp_next_day_text)
    TextView nextDay;
    @BindView(R.id.tp_previous_day_text)
    TextView previousDay;
    @BindView(R.id.tp_next_day)
    ImageView nextDayImg;
    @BindView(R.id.tp_previous_day)
    ImageView previousDayImg;

    TripAdapter weekAdapter;
    TripAdapter sundayAdapter;
    TransportLocation currentRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        final List<String> items = new ArrayList<>();
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        items.add("11:30 PM");
        currentRoute = new TransportLocation("NCBS", "IISC", R.drawable.iisc, TransportType.SHUTTLE, false);
        // bottomNote.setVisibility(View.INVISIBLE);
        nextDay.setVisibility(View.INVISIBLE);
        nextDayImg.setVisibility(View.INVISIBLE);
        previousDay.setVisibility(View.INVISIBLE);
        previousDayImg.setVisibility(View.INVISIBLE);

        weekAdapter = new TripAdapter(items, 2);
        sundayAdapter = new TripAdapter(items, 10);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getBaseContext());
        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(getBaseContext());

        weekList.setLayoutManager(mLayoutManager1);
        weekList.setItemAnimator(new DefaultItemAnimator());
        weekList.setAdapter(weekAdapter);

        sundayList.setLayoutManager(mLayoutManager2);
        sundayList.setItemAnimator(new DefaultItemAnimator());
        sundayList.setAdapter(sundayAdapter);

        allRouteImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                int width = size.x;
                int height = size.y;
                allRoutes.setPeekSheetTranslation(height / 2);
                allRoutes.showWithSheetView(LayoutInflater.from(getBaseContext()).inflate(R.layout.transport_sheet, allRoutes, false));
                final List<String[]> items = new ArrayList<String[]>();
                items.add(new String[]{"NCBS", "Manadra"});
                items.add(new String[]{"NCBS", "Manadra"});
                items.add(new String[]{"NCBS", "Manadra"});
                items.add(new String[]{"NCBS", "Manadra"});
                items.add(new String[]{"NCBS", "Manadra"});
                items.add(new String[]{"NCBS", "Manadra"});
                items.add(new String[]{"NCBS", "Manadra"});
                RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(getBaseContext());
                SelectRouteAdapter ad = new SelectRouteAdapter(items);
                RecyclerView locationList = (RecyclerView) findViewById(R.id.tp_sheet_recycler);
                locationList.setLayoutManager(mLayoutManager3);
                locationList.setItemAnimator(new DefaultItemAnimator());
                locationList.setAdapter(ad);
                ad.setOnItemClickListener(new SelectRouteAdapter.ClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Log.i("Selected", position + "");
                        allRoutes.dismissSheet();
                        weekAdapter.notifyDataSetChanged();
                        sundayAdapter.notifyDataSetChanged();
                    }
                });

                final FloatingActionButton im = (FloatingActionButton) findViewById(R.id.tp_sheet_max);
                im.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        im.setVisibility(View.GONE);
                        allRoutes.expandSheet();
                    }
                });
            }
        });


    }


}
