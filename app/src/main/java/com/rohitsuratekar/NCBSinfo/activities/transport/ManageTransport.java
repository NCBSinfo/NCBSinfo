package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.EditTransport;
import com.rohitsuratekar.NCBSinfo.background.services.CommonTasks;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.string.cancel;
import static android.R.string.yes;

public class ManageTransport extends BaseActivity {

    @BindView(R.id.mt_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.ma_progressBar)
    ProgressBar progressBar;

    private ManageTransportAdapter adapter;
    private List<RouteData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_transport);
        ButterKnife.bind(this);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.manage_routes);
        progressBar.setVisibility(View.VISIBLE);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("transport_manage_access", Helper.timestamp());
        mFirebaseAnalytics.logEvent("transport_manage", params);

        TransportViewModel viewModel = ViewModelProviders.of(this).get(TransportViewModel.class);
        dataList = new ArrayList<>();
        adapter = new ManageTransportAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        viewModel.getAllRoutes().observe(this, new Observer<List<RouteData>>() {
            @Override
            public void onChanged(@Nullable List<RouteData> routeDataList) {
                if (routeDataList != null) {
                    progressBar.setVisibility(View.GONE);
                    dataList.clear();
                    dataList.addAll(routeDataList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        viewModel.loadRoute(getApplicationContext(), 0);
        adapter.setOnOptionClick(new ManageTransportAdapter.OnOptionClick() {
            @Override
            public void edit(int position) {
                RouteData d = dataList.get(position);
                Intent intent = new Intent(ManageTransport.this, EditTransport.class);
                intent.putExtra(EditTransport.ORIGIN, d.getOrigin());
                intent.putExtra(EditTransport.DESTINATION, d.getDestination());
                intent.putExtra(EditTransport.TYPE, d.getType());
                startActivity(intent);
                animateTransition();
            }

            @Override
            public void delete(final int position) {

                final String origin = dataList.get(position).getOrigin();
                final String destination = dataList.get(position).getDestination();
                final String type = dataList.get(position).getType();
                new AlertDialog.Builder(ManageTransport.this)
                        .setTitle(getString(R.string.are_you_sure))
                        .setMessage(getString(R.string.mt_delete_confirm,
                                origin.toUpperCase(), destination.toUpperCase(), type))
                        .setPositiveButton(yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                CommonTasks.deleteRoute(getApplicationContext(), origin, destination, type);
                                dataList.remove(position);
                                adapter.notifyItemRemoved(position);
                                adapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
            }

            @Override
            public void report(int position) {
                Log.inform("Reporting Route");
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@secretbiology.com", "ncbs.mod@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on Transport Route " + getString(R.string.route_name,
                        dataList.get(position).getOrigin().toUpperCase(),
                        dataList.get(position).getDestination().toUpperCase(),
                        dataList.get(position).getType()));
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_manage_transport;
    }

    @OnClick(R.id.mt_add_new)
    public void addNew() {
        startActivity(new Intent(this, EditTransport.class));
        animateTransition();
    }
}
