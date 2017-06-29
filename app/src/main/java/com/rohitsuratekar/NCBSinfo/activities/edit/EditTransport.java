package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.secretbiology.helpers.general.views.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditTransport extends BaseActivity implements GoNext {

    @BindView(R.id.et_viewpager)
    ViewPager pager;
    @BindView(R.id.et_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.et_note)
    TextView note;

    private StepperAdapter stepperAdapter;
    private List<StepperModel> modelList = new ArrayList<>();
    private EditTransportViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_transport);
        ButterKnife.bind(this);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.add_route);
        viewModel = ViewModelProviders.of(this).get(EditTransportViewModel.class);
        ViewpagerAdapter adapter = new ViewpagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StepBasicDetails(), "temp");
        adapter.addFragment(new StepTripDetails(), "temp");
        adapter.addFragment(new StepTripDetails(), "temp");
        pager.setAdapter(adapter);
        stepperAdapter = new StepperAdapter(modelList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(stepperAdapter);
        subscribe();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTitle(getString(R.string.add_route_title, (position + 1)));
                note.setText(modelList.get(position).getNote());
                for (StepperModel m : modelList) {
                    if (modelList.indexOf(m) == position) {
                        m.setSelected(true);
                    } else {
                        m.setSelected(false);
                    }
                }
                stateChanged(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void subscribe() {
        viewModel.initializeProcess();
        viewModel.getStepperList().observe(this, new Observer<List<StepperModel>>() {
            @Override
            public void onChanged(@Nullable List<StepperModel> stepperModels) {
                if (stepperModels != null) {
                    modelList.clear();
                    modelList.addAll(stepperModels);
                    stepperAdapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.getRouteModel().observe(this, new Observer<RouteInfoModel>() {
            @Override
            public void onChanged(@Nullable RouteInfoModel routeInfoModel) {
                //// TODO: 21-06-17
            }
        });
    }

    private void stateChanged(int location) {
        for (int i = 0; i < modelList.size(); i++) {
            if (i < location) {
                if (modelList.get(i).getState() != StepperModel.STATE_COMPLETED) {
                    modelList.get(i).setState(StepperModel.STATE_INCOMPLETE);
                }
            } else if (i == location) {
                if (modelList.get(i).getState() == StepperModel.STATE_INITIATED) {
                    modelList.get(i).setState(StepperModel.STATE_SELECTED);
                }
            }
        }
        if (viewModel.getStepperList().getValue() != null) {
            List<StepperModel> m = viewModel.getStepperList().getValue();
            for (int i = 0; i < m.size(); i++) {
                m.get(i).setState(modelList.get(i).getState());
                m.get(i).setSelected(modelList.get(i).isSelected());
            }
            viewModel.getStepperList().postValue(m);
        }
    }

    @Override
    protected int setNavigationMenu() {
        return 0;
    }

    @Override
    public void stepCompleted(int stepNo) {
        if (stepNo == 1) {
            pager.setCurrentItem(stepNo, true);
        }
    }
}
