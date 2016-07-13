package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 12-07-16.
 */
public class NotificationPreference extends PreferenceFragment implements Preference.OnPreferenceClickListener {

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
            setAll(true);
        } else {
            allNotification.setChecked(false);
            setAll(false);
        }

        allNotification.setOnPreferenceClickListener(this);
        eventNotification.setOnPreferenceClickListener(this);
        importantNotifications.setOnPreferenceClickListener(this);
        emergencyNotifications.setOnPreferenceClickListener(this);

        setOptions(allNotification, "All Notifications");
        setOptions(eventNotification, "Event Notifications");
        setOptions(importantNotifications, "Important Notifications");
        setOptions(emergencyNotifications, "Emergency Notifications");


    }


    private void setAll(boolean value) {
        eventNotification.setEnabled(value);
        importantNotifications.setEnabled(value);
        emergencyNotifications.setEnabled(value);
    }

    private void setOptions(SwitchPreference preference, String details) {
        if (preference.isChecked()) {
            preference.setSummary(details + " are ON");
        } else {
            preference.setSummary(details + " are OFF");
        }
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "settings_event_notifications":
                if (eventNotification.isChecked()) {
                    pref.settings().setEventNotifications(true);
                } else {
                    pref.settings().setEventNotifications(false);
                }
                setOptions(eventNotification, "Event Notifications");
                break;
            case "settings_important_notifications":
                if (importantNotifications.isChecked()) {
                    pref.settings().setImportantNotifications(true);
                } else {
                    pref.settings().setImportantNotifications(false);
                }
                setOptions(importantNotifications, "Important Notifications");
                break;
            case "settings_emergency_notifications":
                if (emergencyNotifications.isChecked()) {
                    pref.settings().setEmergencyNotifications(true);
                } else {
                    pref.settings().setEmergencyNotifications(false);
                }
                setOptions(emergencyNotifications, "Emergency Notifications");
                break;
            case "settings_notifications":
                if (allNotification.isChecked()) {
                    pref.user().notificationAllowed(true);
                    setAll(true);
                } else {
                    pref.user().notificationAllowed(false);
                    setAll(false);
                }
                setOptions(allNotification, "All Notifications");
                break;

        }
        return false;
    }
}