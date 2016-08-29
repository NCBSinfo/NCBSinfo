package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.SettingsCommon;
import com.rohitsuratekar.NCBSinfo.background.NetworkOperations;
import com.rohitsuratekar.NCBSinfo.background.ServiceCentre;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;
import com.rohitsuratekar.NCBSinfo.utilities.CurrentMode;
import com.secretbiology.helpers.general.General;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 11-07-16.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class GeneralPreference extends PreferenceFragment {

    Preference changeMode, clearPref, faq, sync;
    Context context;
    BaseParameters baseParameters;
    Preferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);
        setHasOptionsMenu(true);

        baseParameters = new BaseParameters(getActivity().getBaseContext());

        context = getActivity();
        pref = new Preferences(context);
        getActivity().setTitle(R.string.settings_general);

        changeMode = findPreference("change_mode_settings");
        clearPref = findPreference("clear_settings");
        faq = findPreference("settings_knownBugs");
        sync = findPreference("sync_events");

        sync.setSummary(context.getResources().getString(R.string.settings_sync_events_summary) + pref.app().getLastEventSync());

        if (pref.app().getMode() != AppConstants.modes.ONLINE) {
            sync.setEnabled(false);
            sync.setSummary("This function is only available in Online Mode.");
        }


        changeMode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new AlertDialog.Builder(context)
                        .setTitle(getString(R.string.warning_mode_change))
                        .setMessage(new CurrentMode(context).getWarningMessage())
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent service = new Intent(context, ServiceCentre.class);
                                service.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_APP_DATA);
                                getActivity().startService(service);
                                Intent intent = new Intent(context, Home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                            }
                        })
                        .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Do nothing
                            }
                        })
                        .setIcon(new CurrentMode(context).getIcon())
                        .show();
                return false;
            }
        });

        clearPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Warning!")
                        .setMessage("Want to clear all customizations and preferences ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent service = new Intent(context, ServiceCentre.class);
                                service.putExtra(ServiceCentre.INTENT, ServiceCentre.RESET_PREFERENCES);
                                getActivity().startService(service);
                                Toast.makeText(getActivity(), "User Preferences Cleared", Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(R.drawable.icon_warning)
                        .show();
                return false;
            }
        });

        faq.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SettingsCommon.class);
                intent.putExtra(SettingsCommon.INTENT, SettingsCommon.FAQ);
                startActivity(intent);
                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                return true;
            }
        });

        sync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (General.isNetworkAvailable(getActivity())) {
                    Intent intent = new Intent(getActivity(), NetworkOperations.class);
                    intent.putExtra(NetworkOperations.INTENT, NetworkOperations.RESEARCH_TALKS);
                    getActivity().startService(intent);
                    Toast.makeText(getActivity(), "Sync Started, Events will be updated if there is any new event and your internet is on", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "You need internet to perform this action.", Toast.LENGTH_LONG).show();
                }
                return true;
            }
        });

    }

}