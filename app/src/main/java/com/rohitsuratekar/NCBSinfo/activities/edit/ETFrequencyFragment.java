package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rohit Suratekar on 14-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETFrequencyFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.et_fq_group)
    RadioGroup group;

    @BindViews({R.id.et_fq_sunday, R.id.et_fq_monday, R.id.et_fq_tuesday, R.id.et_fq_wednesday, R.id.et_fq_thursday, R.id.et_fq_friday, R.id.et_fq_saturday})
    List<CheckBox> checkBoxList;

    private ETViewModel viewModel;
    private ETDataHolder holder;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_frequency, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        holder = viewModel.getData().getValue();
        if (holder != null) {
            if (holder.getFrequency() != -1) {
                switchView(holder.getFrequency());
                ((RadioButton) rootView.findViewById(holder.getFrequency())).setChecked(true);
                for (int i = 0; i < holder.getFrequencyDetails().length; i++) {
                    if (holder.getFrequencyDetails()[i] == 0) {
                        checkBoxList.get(i).setChecked(false);
                    } else {
                        checkBoxList.get(i).setChecked(true);
                    }
                }
            }
        }
        group.setOnCheckedChangeListener(this);
        return rootView;
    }

    @OnClick(R.id.et_fq_next)
    public void goNext() {
        setFrequencyDetails();
        viewModel.getData().postValue(holder);
        viewModel.getCurrentStep().postValue(3);
    }

    @OnClick(R.id.et_fq_previous)
    public void goPrevious() {
        setFrequencyDetails();
        viewModel.getData().postValue(holder);
        viewModel.getCurrentStep().postValue(1);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        holder.setFrequency(i);
        switchView(i);
    }

    private void setFrequencyDetails() {
        int[] details = new int[7];
        for (int j = 0; j < checkBoxList.size(); j++) {
            if (checkBoxList.get(j).isChecked()) {
                details[j] = 1;
            } else {
                details[j] = 0;
            }
        }
        holder.setFrequencyDetails(details);
    }

    private void switchView(int i) {
        switch (i) {
            case R.id.et_fq_all_days:
                for (CheckBox box : checkBoxList) {
                    box.setChecked(true);
                    box.setEnabled(false);
                }
                break;
            case R.id.et_fq_select_specific:
                for (CheckBox box : checkBoxList) {
                    box.setChecked(false);
                    box.setEnabled(true);
                }
                break;
            case R.id.et_fq_mon_sat:
                for (CheckBox box : checkBoxList) {
                    box.setChecked(true);
                    box.setEnabled(false);
                }
                checkBoxList.get(0).setChecked(false);
                break;
            case R.id.et_fq_sat_sun:
                for (CheckBox box : checkBoxList) {
                    box.setChecked(false);
                    box.setEnabled(false);
                }
                checkBoxList.get(0).setChecked(true);
                checkBoxList.get(6).setChecked(true);
                break;
        }
    }


}
