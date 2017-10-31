package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.rohitsuratekar.NCBSinfo.BuildConfig;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.AppPrefs;
import com.rohitsuratekar.NCBSinfo.common.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Settings extends BaseActivity implements SettingsActions {

    @BindView(R.id.st_recycler)
    RecyclerView recyclerView;

    private SettingsAdapter adapter;
    private List<SettingsModel> modelList;
    private AppPrefs prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        findViewById(R.id.tabs).setVisibility(View.GONE);
        setTitle(R.string.settings);
        ButterKnife.bind(this);
        prefs = new AppPrefs(getApplicationContext());
        setUpItems();
        adapter = new SettingsAdapter(modelList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected int setNavigationMenu() {
        return R.id.nav_settings;
    }

    private void setUpItems() {
        modelList = new ArrayList<>();

        for (Object[] obj : items) {
            if ((int) obj[0] == VIEW_HEADER) {
                modelList.add(new SettingsModel(getString((int) obj[1])));
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

                }
                modelList.add(mod);
            } else {
                modelList.add(new SettingsModel(VIEW_LINE));
            }
        }


    }

    Object[][] items = {
            {VIEW_HEADER, R.string.settings_header_general},
            {VIEW_ITEM, R.string.settings_notification, 0, R.drawable.icon_notifications_active, ACTION_NOTIFICATIONS},
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
            {VIEW_HEADER, R.string.settings_header_about},
            {VIEW_ITEM, R.string.settings_about, R.string.settings_about_details, R.drawable.icon_star, ACTION_ABOUT_US},
            {VIEW_ITEM, R.string.settings_github, R.string.settings_github_details, R.drawable.icon_github, ACTION_GITHUB},
            {VIEW_ITEM, R.string.settings_feedback, R.string.settings_feedback_details, R.drawable.icon_feedback, ACTION_FEEDBACK},
            {VIEW_ITEM, R.string.settings_app_details, 0, R.mipmap.ic_launcher_round, ACTION_APP_DETAILS}
    };


}
