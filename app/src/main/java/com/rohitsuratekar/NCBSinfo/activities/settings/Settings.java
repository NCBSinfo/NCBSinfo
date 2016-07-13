package com.rohitsuratekar.NCBSinfo.activities.settings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.DevelopersPreference;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.GeneralPreference;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.InformationPreference;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.NotificationPreference;
import com.rohitsuratekar.NCBSinfo.activities.settings.fragments.TransportPreference;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 11-07-16.
 */
public class Settings extends SettingsBase {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
        setTitle(R.string.settings);


    }


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
        Preferences pref = new Preferences(getBaseContext());
        for (int i = 0; i < target.size(); i++) {

            if (target.get(i).fragment.equals(NotificationPreference.class.getName())) {
                if (pref.app().getMode().equals(AppConstants.modes.OFFLINE)) {
                    target.remove(i);
                }
            }

            if (target.get(i).fragment.equals(DevelopersPreference.class.getName())) {
                if (!pref.settings().isDevelopersOptionON()) {
                    target.remove(i);
                }
            }
        }
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreference.class.getName().equals(fragmentName)
                || TransportPreference.class.getName().equals(fragmentName)
                || InformationPreference.class.getName().equals(fragmentName)
                || DevelopersPreference.class.getName().equals(fragmentName)
                || NotificationPreference.class.getName().equals(fragmentName);
    }


    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (!super.onMenuItemSelected(featureId, item)) {
                this.finish();
                this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

}
