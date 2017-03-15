package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;

import java.util.ArrayList;
import java.util.List;


class SettingsItems implements SettingsIDs {

    private Context context;
    private List<SettingsModel> list;
    private AppPrefs prefs;

    SettingsItems(Context context) {
        this.context = context;
        prefs = new AppPrefs(context);
    }

    public List<SettingsModel> getList() {
        list = new ArrayList<>();

        general();
        addDivider();
        transport();
        addDivider();
        if (prefs.isDeveloper()) {
            developersOptions();
            addDivider();
        }
        addAboutNCBSinfo();


        return list;

    }

    private void transport() {
        addSectionHeader(context.getString(R.string.transport));
        SettingsModel transport = new SettingsModel();
        transport.setTitle("Default route");
        transport.setSubtitle(context.getString(R.string.dashboard_transport,
                prefs.getFavoriteOrigin().toUpperCase(), prefs.getFavoriteDestination().toUpperCase(), prefs.getFavoriteType().toLowerCase()));
        transport.setAction(ACTION_TRANSPORT);
        transport.setIcon(R.drawable.icon_transport);
        list.add(transport);

        SettingsModel addNew = new SettingsModel();
        addNew.setTitle("Add new route");
        addNew.setSubtitle("You can add your own custom route");
        addNew.setAction(ACTION_ADD_NEW_ROUTE);
        addNew.setIcon(R.drawable.icon_add);
        list.add(addNew);

        SettingsModel reset = new SettingsModel();
        reset.setTitle("Reset all routes");
        reset.setSubtitle("Reset all routes to default app routes");
        reset.setAction(ACTION_RESET_ROUTES);
        reset.setIcon(R.drawable.icon_reset);
        list.add(reset);

    }

    private void general() {
        addSectionHeader("General");

        SettingsModel notifications = new SettingsModel();
        String tempNote = "OFF";
        notifications.setIcon(R.drawable.icon_notifications_off);
        if (prefs.isNotificationAllowed()) {
            tempNote = "ON";
            notifications.setIcon(R.drawable.icon_notifications);
        }
        notifications.setTitle("Notifications");
        notifications.setSubtitle(context.getString(R.string.settings_notifications, tempNote));
        notifications.setAction(ACTION_NOTIFICATIONS);
        list.add(notifications);

        SettingsModel network = new SettingsModel();
        network.setTitle(context.getString(R.string.last_sync));

        network.setAction(ACTION_SYNC);
        if (prefs.isUserLoggedIn()) {
            network.setIcon(R.drawable.icon_sync);
            network.setSubtitle(prefs.getLastSync());

        } else {
            network.setIcon(R.drawable.icon_no_sync);
            network.setSubtitle(context.getString(R.string.last_sync_warning));
        }
        list.add(network);
    }

    private void addAboutNCBSinfo() {
        addSectionHeader("Important information");

        SettingsModel terms = new SettingsModel();
        terms.setTitle(context.getString(R.string.terms_and_conditions));
        terms.setSubtitle(context.getString(R.string.settings_terms_summary));
        terms.setAction(ACTION_TERMS_AND_CONDITIONS);
        terms.setIcon(R.drawable.icon_alphabetically);
        list.add(terms);

        SettingsModel privacy = new SettingsModel();
        privacy.setTitle(context.getString(R.string.privacy));
        privacy.setSubtitle(context.getString(R.string.settings_privacy_summary));
        privacy.setAction(ACTION_PRIVACY);
        privacy.setIcon(R.drawable.icon_authenticated);
        list.add(privacy);

        SettingsModel aboutUs = new SettingsModel();
        aboutUs.setTitle(context.getString(R.string.settings_about_us));
        aboutUs.setSubtitle(context.getString(R.string.settings_about_us_summary));
        aboutUs.setAction(ACTION_ABOUT_US);
        aboutUs.setIcon(R.drawable.icon_favorite);
        list.add(aboutUs);

        addDivider();
        addSectionHeader("Other information");

        SettingsModel contribute = new SettingsModel();
        contribute.setTitle(context.getString(R.string.settings_contribute));
        contribute.setSubtitle(context.getString(R.string.settings_contribute_summary));
        contribute.setAction(ACTION_CONTRIBUTE);
        contribute.setIcon(R.drawable.github);
        list.add(contribute);

        SettingsModel share = new SettingsModel();
        share.setTitle("Share");
        share.setSubtitle("Spread the word");
        share.setAction(ACTION_SHARE);
        share.setIcon(R.drawable.icon_share);
        list.add(share);

        SettingsModel copyrights = new SettingsModel();
        copyrights.setTitle(context.getString(R.string.acknowledgments));
        copyrights.setSubtitle(context.getString(R.string.acknowledgments_summary));
        copyrights.setAction(ACTION_COPYRIGHT);
        copyrights.setIcon(R.drawable.icon_copy);
        list.add(copyrights);

        SettingsModel appInfo = new SettingsModel();
        appInfo.setTitle(context.getString(R.string.app_name));
        appInfo.setSubtitle("version :" + prefs.getCurrentVersionName());
        appInfo.setAction(ACTION_BUILD_CLICK);
        appInfo.setIcon(R.drawable.notification_icon);
        list.add(appInfo);
    }

    private void developersOptions() {
        addSectionHeader("Developers Options");

        SettingsModel log = new SettingsModel();
        log.setTitle("Application Log");
        log.setSubtitle("check logs");
        log.setIcon(R.drawable.icon_edit);
        log.setAction(ACTION_LOG);
        list.add(log);

        SettingsModel dev = new SettingsModel();
        dev.setTitle("Developer's options");
        dev.setSubtitle("Turn off developers options");
        dev.setIcon(R.drawable.icon_developer);
        dev.setAction(ACTION_TOGGLE_DEV);
        list.add(dev);
    }

    private void addSectionHeader(String title) {
        SettingsModel section1 = new SettingsModel();
        section1.setTitle(title);
        section1.setViewType(VIEW_SECTION_HEADER);
        section1.setAction(ACTION_NONE);
        list.add(section1);
    }

    private void addDivider() {
        SettingsModel divider = new SettingsModel();
        divider.setViewType(VIEW_DIVIDER);
        divider.setAction(ACTION_NONE);
        list.add(divider);
    }
}
