package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

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

import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.ok;

/**
 * Created by Rohit Suratekar on 14-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ETFirstTripFragment extends Fragment {

    @BindView(R.id.et_confirm_recycler)
    RecyclerView recyclerView;

    private ETViewModel viewModel;
    private ETDataHolder holder;
    private ETSimpleAdapter adapter;
    private List<String> itemList;


    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_first_trip, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        itemList = new ArrayList<>();
        adapter = new ETSimpleAdapter(itemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        holder = viewModel.getData().getValue();
        if (holder != null) {
            if (holder.getItemList().size() > 0) {
                itemList.addAll(holder.getItemList());
                adapter.notifyDataSetChanged();
            }
        }
        adapter.setOnSelect(new ETSimpleAdapter.OnSelect() {
            @Override
            public void selected(int position) {
                selectFirst(position);
            }
        });
        return rootView;
    }

    private void selectFirst(int position) {
        String first = null;
        List<String> afterTrips = new ArrayList<>();
        List<String> regularTrips = new ArrayList<>();
        for (String s : itemList) {
            if (itemList.indexOf(s) < position) {
                afterTrips.add(s);
            } else if (itemList.indexOf(s) == position) {
                first = s;
            } else {
                regularTrips.add(s);
            }
        }
        itemList.clear();
        itemList.add(first);
        itemList.addAll(regularTrips);
        itemList.addAll(afterTrips);
        adapter.notifyDataSetChanged();
        holder.setItemList(itemList);
    }

    @OnClick(R.id.et_confirm_previous)
    public void goPrevious() {
        viewModel.getCurrentStep().postValue(3);
    }

    @OnClick(R.id.et_confirm_next)
    public void validate() {
        if (holder.getOrigin() == null | holder.getDestination() == null) {
            showError(getString(R.string.et_confirm_basic_info_warning), 0);
        } else if (holder.getOrigin().length() == 0 | holder.getDestination().length() == 0) {
            showError(getString(R.string.et_confirm_basic_info_warning), 0);
        } else if (holder.getType() == -1) {
            showError(getString(R.string.et_confirm_type_warning), 1);
        } else if (holder.getItemList().size() == 0) {
            showError(getString(R.string.et_confirm_trip_warning), 3);
        } else {
            viewModel.getCurrentStep().postValue(5);
        }
    }

    void showError(String message, final int action) {
        new AlertDialog.Builder(getContext(), R.style.ErrorDialog)
                .setTitle(R.string.oops)
                .setMessage(message)
                .setCancelable(false)
                .setIcon(R.drawable.icon_error)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        viewModel.getCurrentStep().postValue(action);
                    }
                }).show();


    }
}
