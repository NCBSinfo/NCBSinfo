package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.routebuilder.TransportHelper;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * Settings related to transport.
 * Get all "key" values from respected preference xml
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TransportPreference extends PreferenceFragment {

    Preferences pref;
    ListPreference defaultTransport;
    Preference hurryUp;
    String[] routeList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_transport);
        setHasOptionsMenu(true);

        routeList = getResources().getStringArray(R.array.home_spinner_items);

        getActivity().setTitle(R.string.transport);

        pref = new Preferences(getActivity().getBaseContext());
        defaultTransport = (ListPreference) findPreference("settings_transport_routes");
        hurryUp = findPreference("setting_hurryup");


        if (pref.settings().isDefaultRouteUsed()) {
            defaultTransport.setSummary(routeList[pref.user().getDefaultRouteValue()]);
        } else {
            defaultTransport.setSummary(R.string.settings_default_view_summary);
        }

        defaultTransport.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                pref.user().setDefaultRoute(new TransportHelper()
                        .getRoute(Integer.valueOf(o.toString())));
                defaultTransport.setSummary(routeList[pref.user().getDefaultRouteValue()]);
                pref.settings().setDefaultRouteUsed(true);
                return false;
            }
        });

        //TODO: create custom number picker
        hurryUp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });

    }



}