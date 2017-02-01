package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.activities.login.AccountSecurity;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dashboard extends BaseActivity {


    @BindView(R.id.dash_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.dash_txt_name)
    TextView name;
    @BindView(R.id.dash_txt_email)
    TextView email;
    @BindView(R.id.dash_btn_notifications)
    Button showBtn;

    private DashboardAdapter adapter;
    private List<DashboardModel> modelList = new ArrayList<>();
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getBaseContext());

        modelList = new DashboardItems().getAll(getBaseContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        //recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL));
        adapter = new DashboardAdapter(modelList);
        recyclerView.setAdapter(adapter);

        name.setText(prefs.getUsername());
        email.setText(prefs.getUserEmail());
        if (!prefs.isUserLoggedIn()) {
            showBtn.setText(getString(R.string.log_in));
            name.setText(getString(R.string.app_name));
            email.setText(getString(R.string.dashboard_note));
        }

        adapter.setOnItemClickListener(new DashboardAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                performAction(modelList.get(position).getActionCode());
            }
        });
    }

    @OnClick(R.id.dash_btn_notifications)
    public void showNotifications() {
        if (prefs.isUserLoggedIn()) {
            startActivity(new Intent(this, Notifications.class));
            animateTransition();
        } else {
            startActivity(new Intent(this, Login.class));
            animateTransition();
        }
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.DASHBOARD;
    }

    private void performAction(int code) {
        switch (code) {
            case DashboardItems.TRANSPORT:
                showTransport();
                break;
            case DashboardItems.NOTIFICATIONS:
                prefs.notificationAllowed(!prefs.isNotificationAllowed());
                modelList.clear();
                modelList.addAll(new DashboardItems().getAll(getBaseContext()));
                adapter.notifyDataSetChanged();
                break;
            case DashboardItems.PASSWORD:
                startActivity(new Intent(this, AccountSecurity.class));
                animateTransition();
                break;
        }
    }

    private void showTransport() {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select default route");
        final List<String[]> allRoutes = new RouteData(getBaseContext()).getRouteNames();
        String[] types = new String[allRoutes.size()];

        for (String[] s : allRoutes) {
            if (s[2].toLowerCase().equals(TransportType.SHUTTLE.toString().toLowerCase())) {
                types[allRoutes.indexOf(s)] = s[0].toUpperCase() + " - " + s[1].toUpperCase();
            } else {
                types[allRoutes.indexOf(s)] = s[0].toUpperCase() + " - " + s[1].toUpperCase() + " (" + s[2].toLowerCase() + ")";
            }
        }
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedRoute = new RouteData(getBaseContext()).checkIfExistsRoute(
                        allRoutes.get(which)[0],
                        allRoutes.get(which)[1],
                        Helper.getType(allRoutes.get(which)[2])
                );
                prefs.setFavoriteRoute(selectedRoute);
                prefs.setFavoriteOrigin(allRoutes.get(which)[0]);
                prefs.setFavoriteDestination(allRoutes.get(which)[1]);
                prefs.setFavoriteType(Helper.getType(allRoutes.get(which)[2]));
                modelList.clear();
                modelList.addAll(new DashboardItems().getAll(getBaseContext()));
                adapter.notifyDataSetChanged();
                dialog.dismiss();

            }

        });
        b.show();
    }
}
