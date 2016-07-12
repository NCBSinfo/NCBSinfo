package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.MenuItem;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.Settings;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

/**
 * Settings related to transport.
 * Get all "key" values from respected preference xml
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class TransportPreference extends PreferenceFragment {

    Preferences pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_transport);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.transport);

        pref = new Preferences(getActivity().getBaseContext());

        ListPreference defaultTransport = (ListPreference) findPreference("settings_transport_routes");
        defaultTransport.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                Log.i("Tag ", o.toString());
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
}