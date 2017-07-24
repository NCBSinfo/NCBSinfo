package com.rohitsuratekar.NCBSinfo.activities.transport.edit;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.TripData;
import com.secretbiology.helpers.general.General;

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

    private ETViewModel viewModel;
    private ETDataHolder holder;
    private ProgressDialog dialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_transport_confirm, container, false);
        ButterKnife.bind(this, rootView);
        viewModel = ViewModelProviders.of(getActivity()).get(ETViewModel.class);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.et_check_conflict));
        dialog.setCancelable(false);
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

    @OnClick(R.id.et_final_recycler)
    public void changeTrip() {
        viewModel.getCurrentStep().postValue(3);
    }

    @OnClick(R.id.et_final_confirm)
    public void confirmChanges() {
        dialog.show();
        startVerification(false);
    }

    private void startVerification(boolean dataOverride) {
        new CheckData(getContext(), new OnDataQuery() {
            @Override
            public void showError(String string, int actionCode) {
                dialog.dismiss();
                showErrorDialog(string);
            }

            @Override
            public void success() {
                dialog.dismiss();
                showErrorDialog("Success!");
            }
        }).execute(false, holder);
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(getString(R.string.oops))
                .setMessage(message)
                .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        }).show();
    }

    static class CheckData extends AsyncTask<Object, Void, Void> {

        private AppData db;
        private OnDataQuery query;
        private AppPrefs prefs;
        private int routeNo;
        private boolean success = false;
        private int actionCode;

        CheckData(Context context, OnDataQuery query) {
            prefs = new AppPrefs(context);
            db = AppData.getDatabase(context);
            this.query = query;
        }

        @Override
        protected Void doInBackground(Object... objects) {
            boolean dataOverride = (boolean) objects[0];
            ETDataHolder holder = (ETDataHolder) objects[1];
            RouteData routeData = new RouteData();
            routeData.setCreatedOn(General.timeStamp());
            routeData.setModifiedOn(General.timeStamp());
            routeData.setAuthor(prefs.getUserName());
            routeData.setOrigin(holder.getOrigin().toLowerCase().trim());
            routeData.setDestination(holder.getDestination().toLowerCase().trim());
            routeData.setType(holder.getConvertedType().toLowerCase().trim());
            routeNo = db.routes().getRouteNo(routeData.getOrigin(), routeData.getDestination(), routeData.getType());
            if (routeNo == 0) {
                int newRoute = (int) db.routes().insertRoute(routeData);
                insert(holder, newRoute);
                success = true;
            } else {
                if (dataOverride) {
                    insert(holder, routeNo);
                    success = true;
                } else {
                    //todo specific errors
                }

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (routeNo != 0) {
                if (success) {
                    query.success();
                } else {
                    query.showError("Old Route issue", actionCode);
                }

            } else if (success) {
                query.success();
            } else {
                query.showError("Something Else Happened", actionCode);
            }
        }

        private void insert(ETDataHolder holder, int route) {
            switch (holder.getFrequency()) {
                case R.id.et_fq_all_days:
                case R.id.et_fq_mon_sat:
                    TripData tripData = new TripData();
                    tripData.setRouteID(route);
                    tripData.setDay(Calendar.MONDAY);
                    tripData.setTrips(holder.getItemList());
                    db.trips().insertTrips(tripData);
                    break;
                default:
                    for (int i = 0; i < holder.getFrequencyDetails().length; i++) {
                        if (holder.getFrequencyDetails()[i] == 1) {
                            TripData tripData2 = new TripData();
                            tripData2.setRouteID(route);
                            tripData2.setDay(i + 1);
                            tripData2.setTrips(holder.getItemList());
                            db.trips().insertTrips(tripData2);
                        }
                    }
            }
        }
    }

    interface OnDataQuery {
        void showError(String string, int actionCode);

        void success();
    }

}
