package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rohit Suratekar on 21-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class StepBasicDetails extends Fragment {

    @BindView(R.id.basic_origin)
    TextInputEditText origin;
    @BindView(R.id.basic_origin_layout)
    TextInputLayout originLayout;
    @BindView(R.id.basic_destination)
    TextInputEditText destination;
    @BindView(R.id.basic_destination_layout)
    TextInputLayout destinationLayout;
    @BindViews({R.id.basic_shuttle, R.id.basic_buggy, R.id.basic_other})
    List<Button> typeList;
    @BindViews({R.id.basic_t1, R.id.basic_t2, R.id.basic_t3, R.id.basic_t4, R.id.basic_t5, R.id.basic_t6, R.id.basic_t7, R.id.basic_t8, R.id.basic_t9, R.id.basic_t10})
    List<TextView> frequencyList;
    private EditTransportViewModel viewModel;
    private int currentType = 0;
    private int currentFrequency = 1;
    private GoNext goNext;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.add_basic_details, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(EditTransportViewModel.class);
        ButterKnife.findById(rootView, R.id.basic_next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        viewModel.getRouteModel().observe((LifecycleOwner) getActivity(), new Observer<RouteInfoModel>() {
            @Override
            public void onChanged(@Nullable RouteInfoModel routeInfoModel) {
                if (routeInfoModel != null) {
                    changeType(typeList.get(routeInfoModel.getType()));
                    changeFrequency(frequencyList.get(routeInfoModel.getFrequency()));
                    origin.setText(routeInfoModel.getOrigin());
                    destination.setText(routeInfoModel.getDestination());
                }
            }
        });
        return rootView;
    }


    @OnClick({R.id.basic_shuttle, R.id.basic_buggy, R.id.basic_other})
    public void changeType(Button button) {
        currentType = typeList.indexOf(button);
        button.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        for (Button b : typeList) {
            if (b != button) {
                b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            }
        }
    }

    @OnClick({R.id.basic_t1, R.id.basic_t2, R.id.basic_t3, R.id.basic_t4, R.id.basic_t5, R.id.basic_t6, R.id.basic_t7, R.id.basic_t8, R.id.basic_t9, R.id.basic_t10})
    public void changeFrequency(TextView textView) {
        currentFrequency = frequencyList.indexOf(textView);
        textView.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
        for (TextView b : frequencyList) {
            if (b != textView) {
                b.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryLight));
            }
        }
    }

    private void validate() {
        originLayout.setErrorEnabled(false);
        destinationLayout.setErrorEnabled(false);
        if (origin.getText().toString().trim().length() > 0) {
            if (destination.getText().toString().trim().length() > 0) {
                if (viewModel.getStepperList().getValue() != null) {
                    List<StepperModel> m = viewModel.getStepperList().getValue();
                    m.get(0).setState(StepperModel.STATE_COMPLETED);
                    viewModel.getStepperList().postValue(m);
                    goNext.stepCompleted(1);
                }
                if (viewModel.getRouteModel().getValue() != null) {
                    RouteInfoModel model = viewModel.getRouteModel().getValue();
                    model.setOrigin(origin.getText().toString().trim());
                    model.setDestination(destination.getText().toString().trim());
                    model.setFrequency(currentFrequency);
                    model.setType(currentType);
                    viewModel.getRouteModel().postValue(model);
                }
            } else {
                destinationLayout.setError("This field can not be empty");
            }
        } else {
            originLayout.setError("This field can not be empty.");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof EditTransport) {
            goNext = (GoNext) context;
        } else {
            General.makeLongToast(getContext(), "Implement fragment interface!");
        }
    }
}
