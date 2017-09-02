package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

class DashboardItems {

    static final int TRANSPORT = 1;
    static final int NOTIFICATIONS = 2;
    static final int SYNC = 3;
    static final int PASSWORD = 4;
    static final int SETTINGS = 5;

    List<DashboardModel> getAll(Context context) {
        List<DashboardModel> all = new ArrayList<>();
        AppPrefs prefs = new AppPrefs(context);

        DashboardModel transport = new DashboardModel();
        transport.setTitle(context.getString(R.string.dashboard_transport,
                prefs.getFavoriteOrigin().toUpperCase(), prefs.getFavoriteDestination().toUpperCase(), prefs.getFavoriteType().toLowerCase()));
        transport.setSubtitle(context.getString(R.string.dashboard_transport_details));
        transport.setIcon(R.drawable.icon_transport);
        transport.setActionCode(TRANSPORT);
        all.add(transport);

        DashboardModel notifications = new DashboardModel();
        String tempNote = "OFF";
        notifications.setIcon(R.drawable.icon_notifications_off);
        if (prefs.isNotificationAllowed()) {
            tempNote = "ON";
            notifications.setIcon(R.drawable.icon_notifications);
        }
        notifications.setTitle(context.getString(R.string.dashboard_notifications, tempNote));
        notifications.setSubtitle(context.getString(R.string.dashboard_notifications_details));

        notifications.setActionCode(NOTIFICATIONS);
        all.add(notifications);

        if (prefs.isUserLoggedIn()) {
            DashboardModel sync = new DashboardModel();
            sync.setTitle(context.getString(R.string.dashboard_sync));
            try {
                String formatted = DateConverter.convertToString(
                        DateConverter.convertToCalender(ConverterMode.MONTH_FIRST, prefs.getLastSync()),
                        "EEE dd MMM, hh:mm a"
                );
                sync.setSubtitle(context.getString(R.string.dashboard_sync_details, formatted));
            } catch (ParseException e) {
                sync.setSubtitle(context.getString(R.string.dashboard_sync_details, prefs.getLastSync()));
            }
            sync.setIcon(R.drawable.icon_sync);
            sync.setActionCode(SYNC);
            all.add(sync);

            DashboardModel password = new DashboardModel();
            password.setTitle(context.getString(R.string.dashboard_password));
            password.setSubtitle(context.getString(R.string.dashboard_password_details));
            password.setIcon(R.drawable.icon_password);
            password.setActionCode(PASSWORD);
            all.add(password);
        }

        DashboardModel settings = new DashboardModel();
        settings.setTitle(context.getString(R.string.settings));
        settings.setSubtitle(context.getString(R.string.dashboard_settings_details));
        settings.setIcon(R.drawable.icon_settings);
        settings.setActionCode(SETTINGS);
        all.add(settings);


        return all;

    }
}
