package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ETTypeFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {

    @BindView(R.id.et_type_group)
    RadioGroup group;

    @BindViews({R.id.et_type_option1, R.id.et_type_option2, R.id.et_type_option3, R.id.et_type_option4})
    List<RadioButton> buttonList;

    private ETViewModel viewModel;
    private ETDataHolder holder;
    private View rootView;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.edit_transport_type, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        holder = viewModel.getData().getValue();
        if (holder != null) {
            if (holder.getType() != -1) {
                buttonList.get(holder.getType()).setChecked(true);
            }
        }
        group.setOnCheckedChangeListener(this);
        return rootView;
    }

    @OnClick(R.id.et_type_next)
    public void goNext() {
        viewModel.getCurrentStep().postValue(2);
    }

    @OnClick(R.id.et_type_previous)
    public void goPrevious() {
        viewModel.getCurrentStep().postValue(0);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        holder.setType(buttonList.indexOf((RadioButton) rootView.findViewById(i)));
        viewModel.getData().postValue(holder);
    }
}
