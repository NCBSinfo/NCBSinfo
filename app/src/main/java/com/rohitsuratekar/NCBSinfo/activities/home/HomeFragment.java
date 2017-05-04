package com.rohitsuratekar.NCBSinfo.activities.home;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.common.AppState;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dexter for NCBSinfo .
 * Code is released under MIT license
 */

public class HomeFragment extends Fragment {

    private static final String ROUTE = "route";
    private AppState state = AppState.getInstance();

    public static HomeFragment newInstance(int route) {
        HomeFragment myFragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ROUTE, route);
        myFragment.setArguments(args);
        return myFragment;
    }

    @BindViews({R.id.hm_icon_back, R.id.hm_icon_loc, R.id.hm_icon_type, R.id.hm_icon_cal, R.id.hm_icon_seats})
    List<ImageView> icons;

    @BindView(R.id.hm_txt_next_time)
    TextView timeText;
    @BindView(R.id.hm_txt_next_route)
    TextView routeText;
    @BindView(R.id.hm_txt_next_date)
    TextView dateText;
    @BindView(R.id.hm_txt_next_type)
    TextView typeText;
    @BindView(R.id.hm_txt_next_seats)
    TextView seatText;
    @BindView(R.id.hm_icon_type)
    ImageView typeIcon;

    private OnFragmentActions fragmentActions;
    private Route currentRoute;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_fragment, container, false);
        ButterKnife.bind(this, rootView);
        currentRoute = state.getRouteList().get(getArguments().getInt(ROUTE));
        changeIconColors();
        setText();
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            fragmentActions = (OnFragmentActions) context;
        } catch (ClassCastException castException) {
            /* The activity does not implement the listener. */
            General.makeLongToast(context, "Unable to start activity!");
        }
    }

    @OnClick(R.id.hm_btn_all_trips)
    public void showAll() {
        fragmentActions.changeActivity();
    }

    private void changeIconColors() {
        for (ImageView view : icons) {
            view.setColorFilter(General.getColor(getContext(), currentRoute.getColor()));
        }
        typeIcon.setImageResource(currentRoute.getType().getIcon());
    }

    private void setText() {
        routeText.setText(getString(R.string.home_card_route_name, currentRoute.getOrigin(), currentRoute.getDestination()));
        typeText.setText(currentRoute.getType().getName());
        seatText.setText(getString(R.string.home_card_seats, currentRoute.getType().getSeats()));
        dateText.setText(DateConverter.convertToString(Calendar.getInstance(), "EEEE, d MMM"));
        try {
            timeText.setText(DateConverter.changeFormat(ConverterMode.DATE_FIRST, currentRoute.nextTrip(Calendar.getInstance()), "hh:mm a"));
        } catch (ParseException e) {
            timeText.setText("--:--");
            e.printStackTrace();
        }
    }

    interface OnFragmentActions {
        void changeActivity();
    }
}
