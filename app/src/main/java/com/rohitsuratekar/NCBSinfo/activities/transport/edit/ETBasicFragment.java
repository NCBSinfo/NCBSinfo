package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rohitsuratekar.NCBSinfo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rohit Suratekar on 13-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETBasicFragment extends Fragment {

    @BindView(R.id.et_origin_input)
    TextInputLayout origin;
    @BindView(R.id.et_destination_input)
    TextInputLayout destination;
    @BindView(R.id.et_origin_helper)
    ImageView originHelper;
    @BindView(R.id.et_destination_helper)
    ImageView destinationHelper;

    private ETViewModel viewModel;
    private ETDataHolder holder;

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_basic, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        holder = viewModel.getData().getValue();
        if (holder != null) {
            if (holder.getOrigin() != null) {
                origin.getEditText().setText(holder.getOrigin());
                if (holder.getOrigin().length() > 0) {
                    originHelper.setImageResource(R.drawable.icon_done_circle);
                    originHelper.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
                }
            }
            if (holder.getDestination() != null) {
                destination.getEditText().setText(holder.getDestination());
                if (holder.getDestination().length() > 0) {
                    destinationHelper.setImageResource(R.drawable.icon_done_circle);
                    destinationHelper.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
                }
            }
        }
        origin.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    originHelper.setImageResource(R.drawable.icon_error);
                    originHelper.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
                } else if (charSequence.length() > 0) {
                    originHelper.setImageResource(R.drawable.icon_done_circle);
                    originHelper.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        destination.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    destinationHelper.setImageResource(R.drawable.icon_error);
                    destinationHelper.setColorFilter(ContextCompat.getColor(getContext(), R.color.red));
                } else if (charSequence.length() > 0) {
                    destinationHelper.setImageResource(R.drawable.icon_done_circle);
                    destinationHelper.setColorFilter(ContextCompat.getColor(getContext(), R.color.green));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        return rootView;
    }

    @OnClick(R.id.et_basic_next)
    public void goNext() {
        holder.setOrigin(origin.getEditText().getText().toString());
        holder.setDestination(destination.getEditText().getText().toString());
        viewModel.getData().postValue(holder);
        viewModel.getCurrentStep().postValue(1);
    }

    @OnClick(R.id.et_basic_cancel)
    public void cancelSteps() {
        //todo
    }
}
