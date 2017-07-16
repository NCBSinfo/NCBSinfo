package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Rohit Suratekar on 15-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETConfirmFragment extends Fragment {


    @BindView(R.id.et_final_route_name)
    TextView routeName;
    @BindView(R.id.et_final_type)
    TextView routeType;
    @BindView(R.id.et_final_recycler)
    RecyclerView recyclerView;
    private ETViewModel viewModel;
    private ETDataHolder holder;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_confirm, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        holder = viewModel.getData().getValue();
        if (holder != null) {
            routeName.setText(getString(R.string.tp_route_name, holder.getOrigin(), holder.getDestination()));
            routeType.setText(getString(R.string.et_confirm_type, getType(holder.getType()), getFrequency(holder.getFrequency())));
            ETSimpleAdapter adapter = new ETSimpleAdapter(holder.getItemList(), true);
            recyclerView.setAdapter(adapter);
            adapter.setOnSelect(new ETSimpleAdapter.OnSelect() {
                @Override
                public void selected(int position) {
                    viewModel.getCurrentStep().postValue(3);
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        return rootView;
    }

    private String getType(int option) {
        switch (option) {
            case 0:
                return getString(R.string.shuttle);
            case 1:
                return getString(R.string.ttc);
            case 2:
                return getString(R.string.buggy);
            default:
                return getString(R.string.other);
        }
    }

    private String getFrequency(int option) {
        switch (option) {
            case R.id.et_fq_mon_sat:
                return getString(R.string.et_confirm_fq_mon_sat);
            case R.id.et_fq_sat_sun:
                return getString(R.string.weekend);
            case R.id.et_fq_select_specific:
                return getDetailFrequency();
            default:
                return getString(R.string.everyday);
        }
    }

    private String getDetailFrequency() {
        StringBuilder s = new StringBuilder();
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < holder.getFrequencyDetails().length; i++) {
            if (holder.getFrequencyDetails()[i] == 1) {
                calendar.set(Calendar.DAY_OF_WEEK, i + 1);
                s.append(calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())).append(",");
            }
        }
        return s.toString().replaceAll(",$", "");
    }

    @OnClick(R.id.et_final_type)
    public void changeType() {
        viewModel.getCurrentStep().postValue(1);
    }

    @OnClick(R.id.et_final_route_name)
    public void changeInfo() {
        viewModel.getCurrentStep().postValue(0);
    }

}
