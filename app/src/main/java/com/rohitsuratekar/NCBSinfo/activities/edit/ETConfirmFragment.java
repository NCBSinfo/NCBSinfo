package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;
import static android.R.string.ok;

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
    @BindView(R.id.et_progressBar)
    ProgressBar progressBar;
    @BindView(R.id.et_final_confirm_btn)
    Button confirmBtn;


    private ETViewModel viewModel;
    private ETDataHolder holder;
    private boolean isBackgroundBusy = false;
    private boolean finishEditing = false;
    private ConfirmModel confirmModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_confirm, container, false);
        ButterKnife.bind(this, rootView);

        confirmModel = new ConfirmModel();
        confirmModel.setForEdit(((EditTransport) getActivity()).isForEdit());
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

                    if (isBackgroundBusy) {
                        Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();
                    } else {
                        viewModel.getCurrentStep().postValue(3);
                    }
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        progressBar.setVisibility(View.GONE);
        subscribe();
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

    private void subscribe() {
        viewModel.getConfirmActions().observe(this, new Observer<ConfirmModel>() {
            @Override
            public void onChanged(@Nullable final ConfirmModel model) {
                if (model != null) {
                    toggleBackground();
                    if (model.isConfirmed()) {
                        viewModel.setFinalData(true);
                    } else {
                        new AlertDialog.Builder(getContext())
                                .setTitle(getString(R.string.are_you_sure))
                                .setMessage(getString(model.getErrorMessage()))
                                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        toggleBackground();
                                        confirmModel.setConfirmed(true);
                                        viewModel.validateInfo(getContext(), confirmModel);
                                    }
                                }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
                    }
                }
            }
        });
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
        if (isBackgroundBusy) {
            Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.getCurrentStep().postValue(1);
        }
    }

    @OnClick(R.id.et_final_route_name)
    public void changeInfo() {

        if (isBackgroundBusy) {
            Toast.makeText(getContext(), "Please wait...", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.getCurrentStep().postValue(0);
        }
    }

    @OnClick(R.id.et_final_confirm_btn)
    public void confirmEdit() {
        toggleBackground();
        confirmModel.setData(viewModel.getData().getValue());
        viewModel.validateInfo(getContext(), confirmModel);
    }

    private void toggleBackground() {
        isBackgroundBusy = !isBackgroundBusy;
        if (isBackgroundBusy) {
            progressBar.setVisibility(View.VISIBLE);
            confirmBtn.setEnabled(false);
            confirmBtn.setAlpha(0.6f);
        } else {
            progressBar.setVisibility(View.GONE);
            confirmBtn.setEnabled(true);
            confirmBtn.setAlpha(1f);
        }
    }

}
