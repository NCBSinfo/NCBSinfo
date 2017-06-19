package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.RouteInfo;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TripWeek;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 12-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class TransportFragment extends Fragment {

    private static String INDEX = "index";

    public static TransportFragment newInstance(int index) {
        TransportFragment myFragment = new TransportFragment();
        Bundle args = new Bundle();
        args.putInt(INDEX, index);
        myFragment.setArguments(args);
        return myFragment;
    }

    @BindView(R.id.card_expand)
    ImageView cardExpand;
    @BindView(R.id.transport_card)
    CardView optionCard;
    @BindView(R.id.transport_trips)
    CardView transportTrips;
    @BindView(R.id.transport_option_recycler)
    RecyclerView optionRecycler;
    @BindView(R.id.transport_options)
    LinearLayout options;
    @BindView(R.id.transport_card_text)
    TextView cardText;
    @BindViews({R.id.left_recycler, R.id.right_recycler})
    List<RecyclerView> tripRecyclers;

    private RouteInfo info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.transport_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (getActivity() instanceof Transport) {
            info = ((Transport) getActivity()).getInfoList().get(getArguments().getInt(INDEX));
        }
        optionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (options.getVisibility() == View.VISIBLE) {
                    toggleOptions(false);
                    changeExpandIcon(false);
                    cardText.setText(getString(R.string.transport_card_text,
                            info.getRoute().getOrigin().toUpperCase(),
                            info.getRoute().getDestination().toUpperCase(),
                            info.getRoute().getType()));
                } else {
                    changeExpandIcon(true);
                    cardText.setText(R.string.transport_options);
                    toggleOptions(true);

                }

            }
        });
        setOptionList();
        setUpTrips();


        return rootView;
    }

    private void toggleOptions(boolean isExpanding) {
        ViewGroup.LayoutParams params = optionCard.getLayoutParams();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        optionCard.setLayoutParams(params);
        int height = optionCard.getHeight();
        if (isExpanding) {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(height, height + 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    optionCard.setMinimumHeight((Integer) animation.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    options.setVisibility(View.VISIBLE);
                    ViewGroup.LayoutParams params = optionCard.getLayoutParams();
                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    optionCard.setLayoutParams(params);
                }
            });
            valueAnimator.setDuration(300);
            valueAnimator.start();
        } else {
            ValueAnimator valueAnimator = ValueAnimator.ofInt(height, height - 100);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    optionCard.setMinimumHeight((Integer) animation.getAnimatedValue());
                }
            });
            valueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    options.setVisibility(View.GONE);
                }
            });
            valueAnimator.setDuration(300);
            valueAnimator.start();
        }


    }

    private void changeExpandIcon(boolean isExpanded) {
        int id = R.drawable.icon_expand;
        if (isExpanded) {
            id = R.drawable.icon_collapse;
        }
        final int finalId = id;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardExpand.setImageResource(finalId);

            }
        }, 300);
    }

    private void setOptionList() {
        List<TransportOption> optionList = new ArrayList<>();
        optionList.add(new TransportOption(0, R.drawable.icon_favorite_empty, R.string.transport_fav));
        optionList.add(new TransportOption(1, R.drawable.icon_delete, R.string.transport_delete));
        optionList.add(new TransportOption(2, R.drawable.icon_edit, R.string.transport_edit));
        optionList.add(new TransportOption(3, R.drawable.icon_feedback, R.string.transport_report));
        TransportOptionAdapter optionAdapter = new TransportOptionAdapter(optionList);
        optionRecycler.setAdapter(optionAdapter);
        optionRecycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        optionAdapter.setOnOptionSelect(new TransportOptionAdapter.OnOptionSelect() {
            @Override
            public void select(int action) {
                switch (action) {
                    case 0:
                        Log.inform("fav");
                        break;
                    case 1:
                        Log.inform("delete");
                        break;
                }
            }
        });
    }

    private void setUpTrips() {
        if (info != null) {

            cardText.setText(getString(R.string.transport_card_text,
                    info.getRoute().getOrigin().toUpperCase(),
                    info.getRoute().getDestination().toUpperCase(),
                    info.getRoute().getType()));

            List<String> aList = info.getTrips().get(0).getTrips(); //todo

            //Load all trips first and start async task to get their index
            TripAdapter adapter1 = new TripAdapter(aList, -1);
            TripAdapter adapter2 = new TripAdapter(aList, -1);

            tripRecyclers.get(0).setAdapter(adapter1);
            tripRecyclers.get(1).setAdapter(adapter2);
            tripRecyclers.get(1).setLayoutManager(new LinearLayoutManager(getContext()));
            tripRecyclers.get(0).setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            General.makeLongToast(getContext(), "Unable to retrieve trips data :(");
        }

    }

    private int getLeftIndex() {
        TripWeek week = new TripWeek(info.getMap());
        if (info.getTrips().size() == 1) {
            try {
                return info.getTrips().get(0).getTrips().indexOf(week.nextTrip(Calendar.getInstance())[0]);
            } catch (ParseException e) {
                Log.error(e.getMessage());
                return 0;
            }
        }

        return 0;
    }

    private int getRightIndex() {
        return 0;
    }
}
