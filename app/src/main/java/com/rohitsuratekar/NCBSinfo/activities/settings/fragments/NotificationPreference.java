package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.view.MenuItem;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 12-07-16.
 */
public class NotificationPreference extends PreferenceFragment {

    Preferences pref;
    SwitchPreference allNotification, eventNotification, importantNotifications, emergencyNotifications;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_notifications);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.notifications);

        pref = new Preferences(getActivity().getBaseContext());
        allNotification = (SwitchPreference) findPreference("settings_notifications");
        eventNotification = (SwitchPreference) findPreference("settings_event_notifications");
        importantNotifications = (SwitchPreference) findPreference("settings_important_notifications");
        emergencyNotifications = (SwitchPreference) findPreference("settings_emergency_notifications");


        if (pref.user().isNotificationAllowed()) {
            allNotification.setChecked(true);
            allNotification.setSummary("All notifications are ON");
            setAll(true);
        } else {
            allNotification.setChecked(false);
            allNotification.setSummary("All notifications are OFF");
            setAll(false);
        }

        allNotification.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (allNotification.isChecked()) {
                    allNotification.setSummary("All notifications are ON");
                    pref.user().notificationAllowed(true);
                    setAll(true);
                } else {
                    allNotification.setSummary("All notifications are OFF");
                    pref.user().notificationAllowed(false);
                    setAll(false);
                }

                return false;

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            startActivity(new Intent(getActivity(), Settings.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setAll(boolean value) {
        eventNotification.setEnabled(value);
        importantNotifications.setEnabled(value);
        emergencyNotifications.setEnabled(value);
    }
}