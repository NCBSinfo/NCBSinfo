package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport);
        ButterKnife.bind(this);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.transport);
        List<String> list = new ArrayList<>();
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");
        list.add("ajsajs");

        TransportAdapter adapter = new TransportAdapter(list, 1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        changeDay(daysList.get(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 2));
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
        changeDay(textView);
    }

    @OnClick(R.id.tp_show_all)
    public void showBottomSheet() {
        BottomSheetDialogFragment bottomSheetDialogFragment = TransportFragment.newInstance(0, 0);
        bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }


    @Override
    protected int setNavigationMenu() {
        return R.id.nav_transport;
    }

    @Override
    public void selected(int routeID) {

    }
}
