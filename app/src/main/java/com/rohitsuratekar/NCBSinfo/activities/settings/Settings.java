package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.rohitsuratekar.NCBSinfo.BuildConfig;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.home.Home;
import com.rohitsuratekar.NCBSinfo.activities.intro.Intro;
import com.rohitsuratekar.NCBSinfo.background.services.CommonTasks;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.string.cancel;
import static android.R.string.ok;

public class Settings extends BaseActivity implements SettingsActions, SettingsAdapter.OnSelect {

    @BindView(R.id.st_recycler)
    RecyclerView recyclerView;

    private SettingsAdapter adapter;
    private List<SettingsModel> modelList = new ArrayList<>();
    private AppPrefs prefs;
    private SettingsViewModel viewModel;
    private List<RouteData> routeDataList;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.settings);
        ButterKnife.bind(this);

        //New test for custom events for analytics
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle params = new Bundle();
        params.putString("settings_accessed", General.timeStamp());
        mFirebaseAnalytics.logEvent("settings", params);

        viewModel = ViewModelProviders.of(this).get(SettingsViewModel.class);
        prefs = new AppPrefs(getApplicationContext());
        adapter = new SettingsAdapter(modelList, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        setUpItems();
        getRoutes();
        viewModel.loadRoute(getApplicationContext());
    }

    private void getRoutes() {
        viewModel.getAllRoutes().observe(this, new Observer<List<RouteData>>() {
            @Override
            public void onChanged(@Nullable List<RouteData> routeData) {
                if (routeData != null) {
                    routeDataList = routeData;
                    setUpItems();
                }
            }
        });

        viewModel.getResetDone().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean != null) {
                    if (aBoolean) {
                        General.makeLongToast(getApplicationContext(), "All routes reset!");
                        Intent intent = new Intent(Settings.this, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        animateTransition();
                    }
                }
            }
        });
    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_settings;
    }

    private void setUpItems() {
        modelList.clear();

        for (Object[] obj : items) {
            if ((int) obj[0] == VIEW_HEADER) {
                if ((int) obj[1] != R.string.settings_egg) {
                    modelList.add(new SettingsModel(getString((int) obj[1])));
                } else {
                    if (prefs.isEggActive()) {
                        modelList.add(new SettingsModel(getString((int) obj[1])));
                    }
                }
            } else if ((int) obj[0] == VIEW_ITEM) {
                SettingsModel mod = new SettingsModel(VIEW_ITEM);
                mod.setTitle(getString((int) obj[1]));
                if ((int) obj[2] != 0) {
                    mod.setDescription(getString((int) obj[2]));
                }
                mod.setIcon((int) obj[3]);
                mod.setAction((int) obj[4]);
                switch (mod.getAction()) {
                    case ACTION_TRANSPORT_UPDATE:
                        mod.setDisabled(true);
                        break;
                    case ACTION_APP_DETAILS:
                        int versionCode = BuildConfig.VERSION_CODE;
                        String versionName = BuildConfig.VERSION_NAME;
                        mod.setDescription(getString(R.string.settings_app_details_text, versionCode, versionName));
                        mod.setDisabled(true);
                        break;
                    case ACTION_NOTIFICATIONS:
                        String notifications;
                        if (prefs.areNotificationsAllowed()) {
                            notifications = "Allowed";
                            mod.setIcon(R.drawable.icon_notifications_active);
                        } else {
                            notifications = "Disabled";
                            mod.setIcon(R.drawable.icon_notifications_disabled);
                        }
                        mod.setDescription(getString(R.string.settings_notification_details, notifications));
                        break;
                    case ACTION_DEFAULT_ROUTE:
                        if (routeDataList == null) {
                            mod.setDisabled(true);
                        } else {
                            if (prefs.isDefaultRouteSet()) {
                                mod.setDescription(getString(R.string.settings_default_route_name,
                                        prefs.getFavoriteOrigin().toUpperCase(),
                                        prefs.getFavoriteDestination().toUpperCase(),
                                        prefs.getFavoriteType()));
                            }
                            mod.setDisabled(false);
                        }
                        break;


                }
                if (mod.getAction() == ACTION_EGG1 || mod.getAction() == ACTION_EGG2) {
                    if (prefs.isEggActive()) {
                        modelList.add(mod);
                    }
                } else if (mod.getAction() == ACTION_REMOVE_EGG) {
                    if (prefs.isEggActive()) {
                        modelList.add(mod);
                        modelList.add(new SettingsModel(VIEW_LINE));
                    }
                } else {
                    modelList.add(mod);
                }
            } else {
                modelList.add(new SettingsModel(VIEW_LINE));
            }
        }

        adapter.notifyDataSetChanged();
    }

    private void showTransport() {
        //TODO : Do this properly
        Builder b = new Builder(this);
        b.setTitle("Select default route");

        String[] types = new String[routeDataList.size()];

        for (RouteData s : routeDataList) {
            types[routeDataList.indexOf(s)] = s.getOrigin().toUpperCase() + "-" + s.getDestination().toUpperCase() + " " + s.getType();
            if (s.getFavorite().equals("yes")) {
                types[routeDataList.indexOf(s)] = types[routeDataList.indexOf(s)] + " (default)";
            }
        }
        b.setPositiveButton(cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        b.setItems(types, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                prefs.setFavoriteOrigin(routeDataList.get(which).getOrigin());
                prefs.setFavoriteDestination(routeDataList.get(which).getDestination());
                prefs.setFavoriteType(routeDataList.get(which).getType());
                CommonTasks.sendFavoriteRoute(getApplicationContext(), routeDataList.get(which).getRouteID());
                prefs.defaultRouteSet();
                for (int i = 0; i < routeDataList.size(); i++) {
                    if (i == which) {
                        routeDataList.get(i).setFavorite("yes");
                    } else {
                        routeDataList.get(i).setFavorite("no");
                    }
                }
                dialog.dismiss();
                setUpItems();
                Snackbar snackbar = Snackbar.make(recyclerView, "Default route changed!", BaseTransientBottomBar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
                snackbar.show();
            }

        });
        b.show();
    }

    Object[][] items = {
            {VIEW_HEADER, R.string.settings_header_general},
            {VIEW_ITEM, R.string.settings_notification, 0, R.drawable.icon_notifications_active, ACTION_NOTIFICATIONS},
            {VIEW_ITEM, R.string.settings_see_intro, R.string.settings_see_intro_details, 0, ACTION_INTRO},
            {VIEW_LINE},
            {VIEW_HEADER, R.string.settings_header_transport},
            {VIEW_ITEM, R.string.settings_default_route, R.string.settings_default_transport_details, R.drawable.icon_transport, ACTION_DEFAULT_ROUTE},
            {VIEW_ITEM, R.string.settings_reset_transport, R.string.settings_reset_transport_details, R.drawable.icon_restore, ACTION_RESET_ROUTE},
            {VIEW_ITEM, R.string.settings_transport_last, R.string.settings_transport_last_update, 0, ACTION_TRANSPORT_UPDATE},
            {VIEW_LINE},
            {VIEW_HEADER, R.string.settings_header_legal},
            {VIEW_ITEM, R.string.settings_terms, R.string.settings_terms_details, R.drawable.icon_copyright, ACTION_TERMS},
            {VIEW_ITEM, R.string.settings_privacy, R.string.settings_privacy_details, 0, ACTION_PRIVACY},
            {VIEW_ITEM, R.string.settings_acknow, R.string.settings_acknow_details, R.drawable.icon_favorite, ACTION_ACK},
            {VIEW_LINE},
            {VIEW_HEADER, R.string.settings_egg},
            {VIEW_ITEM, R.string.settings_ron, R.string.settings_ron_details, 0, ACTION_EGG1},
            {VIEW_ITEM, R.string.settings_hermione, R.string.settings_hermione_details, 0, ACTION_EGG2},
            {VIEW_ITEM, R.string.settings_remove_egg, R.string.settings_remove_egg_details, 0, ACTION_REMOVE_EGG},
            {VIEW_HEADER, R.string.settings_header_about},
            {VIEW_ITEM, R.string.settings_about, R.string.settings_about_details, R.drawable.icon_star, ACTION_ABOUT_US},
            {VIEW_ITEM, R.string.settings_github, R.string.settings_github_details, R.drawable.icon_github, ACTION_GITHUB},
            {VIEW_ITEM, R.string.settings_feedback, R.string.settings_feedback_details, R.drawable.icon_feedback, ACTION_FEEDBACK},
            {VIEW_ITEM, R.string.settings_app_details, 0, R.mipmap.ic_launcher_round, ACTION_APP_DETAILS}
    };

    private void showInfo(String action) {
        Bundle params = new Bundle();
        params.putString("settings_info_accessed", General.timeStamp());
        params.putString("settings_info_type", action);
        mFirebaseAnalytics.logEvent("settings_info", params);
        Intent infoIntent = new Intent(this, SettingsInfo.class);
        infoIntent.setAction(action);
        startActivity(infoIntent);
        animateTransition();
    }


    @Override
    public void clicked(int position) {
        if (!modelList.get(position).isDisabled()) {

            switch (modelList.get(position).getAction()) {
                case ACTION_DEFAULT_ROUTE:
                    showTransport();
                    break;
                case ACTION_NOTIFICATIONS:
                    prefs.toggleNotifications();
                    setUpItems();
                    break;
                case ACTION_TERMS:
                    showInfo(SettingsInfo.TERMS);
                    break;
                case ACTION_PRIVACY:
                    showInfo(SettingsInfo.PRIVACY);
                    break;
                case ACTION_ACK:
                    showInfo(SettingsInfo.ACK);
                    break;
                case ACTION_ABOUT_US:
                    showInfo(SettingsInfo.ABOUT);
                    break;
                case ACTION_GITHUB:
                    String currentUrl = "https://github.com/NCBSinfo/NCBSinfo";
                    Intent i2 = new Intent(Intent.ACTION_VIEW);
                    i2.setData(Uri.parse(currentUrl));
                    startActivity(i2);
                    break;
                case ACTION_RESET_ROUTE:
                    new Builder(Settings.this)
                            .setTitle(getString(R.string.are_you_sure))
                            .setMessage(getString(R.string.settings_reset_warning))
                            .setPositiveButton(ok, new OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    viewModel.startRest(getApplicationContext());
                                }
                            }).setNegativeButton(cancel, new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    }).show();
                    break;
                case ACTION_FEEDBACK:
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/html");
                    intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"contact@secretbiology.com", "ncbs.mod@gmail.com"});
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback on NCBSinfo v" + BuildConfig.VERSION_CODE);
                    startActivity(Intent.createChooser(intent, "Send Email"));
                    break;
                case ACTION_INTRO:
                    startActivity(new Intent(this, Intro.class));
                    animateTransition();
                    break;
                case ACTION_EGG1:
                    showInfo(SettingsInfo.EGG_ASSAY1);
                    break;
                case ACTION_EGG2:
                    showInfo(SettingsInfo.EGG_ASSAY2);
                    break;
                case ACTION_REMOVE_EGG:
                    prefs.removeEggs();
                    General.makeLongToast(getApplicationContext(), "As you wish! Apparate...");
                    finish();
                    break;

            }
        }
    }
}
