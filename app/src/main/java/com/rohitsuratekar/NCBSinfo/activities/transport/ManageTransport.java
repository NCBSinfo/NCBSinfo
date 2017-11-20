package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.EditTransport;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManageTransport extends BaseActivity {

    @BindView(R.id.mt_recycler)
    RecyclerView recyclerView;

    private ManageTransportAdapter adapter;
    private List<RouteData> dataList;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manage_transport);
        ButterKnife.bind(this);
        ButterKnife.findById(this, R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.manage_routes);
        dialog = new ProgressDialog(ManageTransport.this);
        dialog.setCancelable(false);
        dialog.setMessage(getString(R.string.loading));
        dialog.show();

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
                    dialog.dismiss();
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
            public void delete(int position) {
                Log.inform("delete");
            }

            @Override
            public void report(int position) {
                Log.inform("Report");
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
