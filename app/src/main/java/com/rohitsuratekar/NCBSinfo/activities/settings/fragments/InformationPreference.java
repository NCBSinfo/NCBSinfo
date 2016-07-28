package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.PrivacyNotice;
import com.rohitsuratekar.NCBSinfo.activities.settings.SettingsCommon;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.ui.BaseParameters;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 11-07-16.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class InformationPreference extends PreferenceFragment {

    Preference github, termsAndConditions, aboutUs, version, privacy;

    int hits;
    Preferences pref;
    BaseParameters baseParameters;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_about_us);
        setHasOptionsMenu(true);
        hits = 0;
        pref = new Preferences(getActivity().getBaseContext());
        baseParameters = new BaseParameters(getActivity().getBaseContext());
        getActivity().setTitle(R.string.information);

        github = findPreference("settings_github");
        termsAndConditions = findPreference("settings_terms_conditions");
        aboutUs = findPreference("settings_aboutus");
        version = findPreference("settings_version");
        privacy = findPreference("settings_privacy");

        version.setTitle(getResources().getString(R.string.app_name));
        version.setSummary("Version Code: " + pref.app().getAppVersionName() + "\nBuild No: " + pref.app().getAppVesion());


        github.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String currenturl = "https://github.com/NCBSinfo/NCBSinfo";
                Intent i2 = new Intent(Intent.ACTION_VIEW);
                i2.setData(Uri.parse(currenturl));
                startActivity(i2);
                return true;
            }
        });

        termsAndConditions.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SettingsCommon.class);
                intent.putExtra(SettingsCommon.INTENT, SettingsCommon.TERMS);
                startActivity(intent);
                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                return true;
            }
        });

        aboutUs.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), SettingsCommon.class);
                intent.putExtra(SettingsCommon.INTENT, SettingsCommon.ABOUT_US);
                startActivity(intent);
                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                return true;
            }
        });

        privacy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), PrivacyNotice.class);
                startActivity(intent);
                getActivity().overridePendingTransition(baseParameters.startTransition(), baseParameters.stopTransition());
                return true;
            }
        });

        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                hits++;
                if (hits > 10) {
                    if (!pref.settings().isDevelopersOptionON()) {
                        Toast.makeText(getActivity(), "Developers Options are switched On", Toast.LENGTH_LONG).show();
                        pref.settings().setDevelopersOptions(true);
                    }
                }
                return false;
            }
        });


    }

}
