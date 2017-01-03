package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Home;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.SelectFirstAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.StepAdapter;
import com.rohitsuratekar.NCBSinfo.activities.transport.adapters.StepModel;
import com.rohitsuratekar.NCBSinfo.activities.transport.fragments.AddLocationFragment;
import com.rohitsuratekar.NCBSinfo.activities.transport.fragments.AddTripsFragment;
import com.rohitsuratekar.NCBSinfo.activities.transport.fragments.ConfirmDetailsFragment;
import com.rohitsuratekar.NCBSinfo.activities.transport.fragments.SelectFirstTripFragment;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TransportEdit extends BaseActivity implements AddLocationFragment.getData,
        AddTripsFragment.sendDetails, SelectFirstTripFragment.sendFirstItem, ConfirmDetailsFragment.getConfirmation {

    @BindView(R.id.transport_viewpager)
    ViewPager viewPager;

    @BindView(R.id.edit_transport_step_indicator)
    RecyclerView stepIndicator;

    @BindView(R.id.edit_transport_next)
    ImageView nextButton;
    @BindView(R.id.edit_transport_text)
    TextView buttonText;
    @BindView(R.id.edit_tranport_main_layout)
    View ActivityRootView;

    private StepAdapter stepAdapter;
    private int currentPage;
    private List<StepModel> stepItems;
    private List<String> currentTrips;
    private int currentFirstTrip;
    private String currentOrigin;
    private String currentDestination;
    private boolean[] allSteps = new boolean[]{false, false, false, false};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        currentTrips = new ArrayList<>();
        currentFirstTrip = -1;
        currentOrigin = "";
        currentDestination = "";
        final ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AddLocationFragment(), "Add Locations");
        adapter.addFragment(new AddTripsFragment(), "Add Strips");
        adapter.addFragment(new SelectFirstTripFragment(), "Select Trip");
        adapter.addFragment(new ConfirmDetailsFragment(), "Confirm Details");
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                stepAdapter.updateLocation(position);
                if (currentPage == stepItems.size() - 1) {
                    buttonText.setText("DONE");
                } else {
                    buttonText.setText("NEXT");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        stepItems = new ArrayList<>();
        stepItems.add(new StepModel(R.drawable.icon_unchecked, R.color.colorPrimary));
        stepItems.add(new StepModel(R.drawable.icon_unchecked, R.color.colorPrimary));
        stepItems.add(new StepModel(R.drawable.icon_unchecked, R.color.colorPrimary));
        stepItems.add(new StepModel(R.drawable.icon_unchecked, R.color.colorPrimary));
        stepAdapter = new StepAdapter(stepItems);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false);
        stepIndicator.setLayoutManager(mLayoutManager);
        stepIndicator.setItemAnimator(new DefaultItemAnimator());
        stepIndicator.setAdapter(stepAdapter);
        stepAdapter.setOnItemClickListener(new StepAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                stepAdapter.updateLocation(position);
                viewPager.setCurrentItem(position);
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPage < stepItems.size() - 1) {
                    stepAdapter.isSkipped(currentPage, true);
                    currentPage++;
                    stepAdapter.updateLocation(currentPage);
                    viewPager.setCurrentItem(currentPage);

                } else {
                    if(allSteps[0]&&allSteps[1]&&allSteps[2]&&allSteps[3]){
                        startActivity(new Intent(TransportEdit.this, Home.class));
                    }else {
                        Toast.makeText(getBaseContext(), "Complete all steps to add new route", Toast.LENGTH_SHORT).show();
                    }
                }
                if (currentPage == stepItems.size() - 1) {
                    buttonText.setText("DONE");
                } else {
                    buttonText.setText("NEXT");
                }
            }
        });

        ActivityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                //r will be populated with the coordinates of your view that area still visible.
                ActivityRootView.getWindowVisibleDisplayFrame(r);

                int heightDiff = ActivityRootView.getRootView().getHeight() - (r.bottom - r.top);
                if (heightDiff > ActivityRootView.getRootView().getHeight() / 4) {
                    stepIndicator.setVisibility(View.GONE);
                    nextButton.setVisibility(View.GONE);
                } else {
                    stepIndicator.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public List<String> getCurrentTrips() {
        return currentTrips;
    }

    public int getCurrentFirstTrip() {
        return currentFirstTrip;
    }

    public String getCurrentOrigin() {
        return currentOrigin;
    }

    public String getCurrentDestination() {
        return currentDestination;
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.TRANSPORT_EDIT;
    }

    @Override
    public boolean isOriginSet(boolean isIt) {
        stepAdapter.isDone(0, isIt);
        allSteps[0] = isIt;
        return false;
    }

    @Override
    public String addedLocation(String org, String des) {
        this.currentOrigin = org;
        this.currentDestination = des;
        return null;
    }

    @Override
    public List<String> setTripDetails(List<String> trips) {
        SelectFirstTripFragment frag1 = (SelectFirstTripFragment) viewPager.getAdapter().instantiateItem(viewPager, 2);
        frag1.setItems(trips);
        frag1.setFirstTrip(-1);
        this.currentFirstTrip = -1;
        this.currentTrips = trips;
        return null;
    }

    @Override
    public boolean areTripsDone(boolean isIt) {
        stepAdapter.isDone(1, isIt);
        allSteps[1] = isIt;
        return false;
    }

    @Override
    public int getFirstItem(int f) {
        ConfirmDetailsFragment frag1 = (ConfirmDetailsFragment) viewPager.getAdapter().instantiateItem(viewPager, 3);
        frag1.setFirstItem(f);
        this.currentFirstTrip = f;
        return 0;
    }

    @Override
    public boolean isFirstTripSelected(boolean isIt, int f) {
        if(isIt && f != -1) {
            stepAdapter.isDone(2, true);
            allSteps[2] = true;
        } else {
            stepAdapter.isDone(2, false);
            allSteps[2] = false;
        }
        return false;
    }

    @Override
    public boolean isConfirmed(boolean isIt) {
        stepAdapter.isDone(3, true);
        allSteps[3] = true;
        return false;
    }
}
