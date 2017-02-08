package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.settings.log.LogActivity;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.rohitsuratekar.NCBSinfo.background.tasks.CreateDefaultRoutes;
import com.rohitsuratekar.NCBSinfo.background.tasks.OnTaskCompleted;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings extends BaseActivity implements SettingIDs {

    @BindView(R.id.settings_recycler)
    RecyclerView recyclerView;

    private List<SettingsModel> allList = new ArrayList<>();
    private AppPrefs prefs;
    private SettingsAdapter adapter;
    private int developClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getBaseContext());
        allList = new SettingsItems(getBaseContext()).getList();

        adapter = new SettingsAdapter(allList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SettingsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position) {
                if (allList.get(position).getAction() != ACTION_NONE) {
                    doAction(allList.get(position).getAction());
                }
            }
        });


    }

    private void doAction(int position) {
        switch (position) {
            case ACTION_LOG:
                startActivity(new Intent(this, LogActivity.class));
                animateTransition();
                break;
            case ACTION_CONTRIBUTE:
                String currenturl = "https://github.com/NCBSinfo/NCBSinfo";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(currenturl));
                startActivity(i2);
                break;
            case ACTION_SHARE:
                sendShare();
                break;
            case ACTION_TRANSPORT:
                showTransport();
                break;
            case ACTION_NOTIFICATIONS:
                prefs.notificationAllowed(!prefs.isNotificationAllowed());
                resetList();
                break;
            case ACTION_BUILD_CLICK:
                developClick++;
                if (developClick > 5) {
                    General.makeShortToast(getBaseContext(), "You have enabled developers options");
                    prefs.setDeveloper(true);
                    resetList();
                }
                break;
            case ACTION_TOGGLE_DEV:
                prefs.setDeveloper(false);
                resetList();
                break;
            case ACTION_RESET_ROUTES:
                showResetRoute();
                break;
            case ACTION_SYNC:
                if (!prefs.isUserLoggedIn()) {
                    startActivity(new Intent(this, Login.class));
                    animateTransition();
                }
                break;
        }
    }

    private void resetList() {
        allList.clear();
        allList.addAll(new SettingsItems(getBaseContext()).getList());
        adapter.notifyDataSetChanged();
    }

    private void sendShare() {
        try {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "NCBSinfo");
            String sAux = "\nHey, check this awesome app about NCBS\n\n";
            sAux = sAux + "http://bit.ly/ncbsinfo\n\n";
            i.putExtra(Intent.EXTRA_TEXT, sAux);
            startActivity(Intent.createChooser(i, "choose one"));
        } catch (Exception e) {
            General.makeLongToast(getBaseContext(), e.getLocalizedMessage());
            Log.error(e.getLocalizedMessage());
        }
    }

    @Override
    protected CurrentActivity setUpActivity() {
        return CurrentActivity.SETTINGS;
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
                resetList();
                dialog.dismiss();

            }

        });
        b.show();
    }

    private void showResetRoute() {
        final ProgressDialog progressDialog = new ProgressDialog(Settings.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please wait...");
        new AlertDialog.Builder(this)
                .setTitle("Are you sure?")
                .setMessage(getString(R.string.settings_reset_warning))
                .setIcon(R.drawable.icon_reset)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int which) {
                        progressDialog.show();
                        new CreateDefaultRoutes(new OnTaskCompleted() {
                            @Override
                            public void onTaskCompleted() {
                                dialog.dismiss();
                                progressDialog.dismiss();
                                General.makeLongToast(getBaseContext(), "All routes reset to their default values");
                            }
                        }).execute(getBaseContext());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
