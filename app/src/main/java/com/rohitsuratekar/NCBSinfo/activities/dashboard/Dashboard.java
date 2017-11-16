package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.background.services.CommonTasks;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Dashboard extends BaseActivity implements DashboardAdapter.OnItemClick {

    static final int ACTION_NAME = 1;
    static final int ACTION_ROUTE = 2;
    static final int ACTION_EMAIL = 3;
    static final int ACTION_SYNC = 4;

    @BindView(R.id.db_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.db_button)
    Button btn;
    @BindView(R.id.db_warning)
    TextView warningText;


    private AppPrefs prefs;
    private DashboardAdapter adapter;
    private List<DashboardItem> itemList;
    private boolean userLoggedIn = false;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.dashboard);
        ButterKnife.bind(this);
        itemList = new ArrayList<>();
        prefs = new AppPrefs(getApplicationContext());
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            userLoggedIn = true;
            btn.setText(getString(R.string.log_out));
            warningText.setVisibility(View.INVISIBLE);
        } else {
            btn.setText(getString(R.string.login));
            warningText.setVisibility(View.VISIBLE);
        }

        setUpList();
        adapter = new DashboardAdapter(itemList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

        //New test for custom events for analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("dashboard_accessed", General.timeStamp());
        mFirebaseAnalytics.logEvent("dashboard", params);


    }

    private void setUpList() {

        if (userLoggedIn) {
            DashboardItem name = new DashboardItem();
            name.setAction(ACTION_NAME);
            name.setEditable(true);
            name.setSubtitle(getString(R.string.name));
            name.setTitle(prefs.getUserName());
            name.setIcon(R.drawable.icon_contact);
            itemList.add(name);
        }

        DashboardItem route = new DashboardItem();
        route.setAction(ACTION_ROUTE);
        route.setEditable(true);
        route.setTitle(getString(R.string.settings_default_route_name,
                prefs.getFavoriteOrigin().toUpperCase(), prefs.getFavoriteDestination().toUpperCase(), prefs.getFavoriteType()));
        route.setSubtitle(getString(R.string.settings_default_route));
        route.setIcon(R.drawable.icon_transport);
        itemList.add(route);

        if (userLoggedIn) {
            DashboardItem email = new DashboardItem();
            email.setTitle(prefs.getUserEmail());
            email.setSubtitle(getString(R.string.email));
            email.setAction(ACTION_EMAIL);
            email.setIcon(R.drawable.icon_email);
            itemList.add(email);
        }


        DashboardItem sync = new DashboardItem();
        sync.setIcon(R.drawable.icon_sync);
        sync.setAction(ACTION_SYNC);
        sync.setSubtitle(getString(R.string.last_network_sync));
        try {
            sync.setTitle(DateConverter.changeFormat(ConverterMode.MONTH_FIRST, prefs.getLastSync(), "dd MMM yy, hh:mm a"));
        } catch (Exception e) {
            sync.setTitle("N/A");
            sync.setIcon(R.drawable.icon_sync_disabled);
        }
        itemList.add(sync);

    }

    @OnClick(R.id.db_button)
    public void dashboardButton() {
        if (userLoggedIn) {
            auth.signOut();
            prefs.clearPersonal();
            finish();
        } else {
            startActivity(new Intent(this, Login.class));
            animateTransition();
        }
    }


    @Override
    protected int setNavigationMenu() {
        return R.id.nav_dash;
    }

    public void changeName() {
        if (General.isNetworkAvailable(getBaseContext())) {
            AlertDialog.Builder alert = new AlertDialog.Builder(Dashboard.this);
            final EditText edittext = new EditText(getBaseContext());
            edittext.setText(prefs.getUserName());
            alert.setTitle(getString(R.string.change_name));
            alert.setView(edittext);
            alert.setPositiveButton(getString(R.string.change), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (edittext.getText().toString().length() > 0) {
                        edittext.clearFocus();
                        sendNameChangeRequest(edittext.getText().toString());

                    } else {
                        General.makeShortToast(getBaseContext(), getString(R.string.empty_error));
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // what ever you want to do with No option.
                }
            });

            alert.show();
        } else {
            General.makeShortToast(getBaseContext(), getString(R.string.network_issue));
        }

    }

    private void sendNameChangeRequest(String name) {
        prefs.setUserName(name);
        CommonTasks.syncUserDetails(getApplicationContext());
        itemList.clear();
        setUpList();
        adapter.notifyDataSetChanged();
    }


    @Override
    public void itemClicked(int position) {
        switch (itemList.get(position).getAction()) {
            case ACTION_ROUTE:
                startActivity(new Intent(this, Settings.class));
                animateTransition();
                break;
            case ACTION_NAME:
                changeName();
                break;

        }
    }
}
