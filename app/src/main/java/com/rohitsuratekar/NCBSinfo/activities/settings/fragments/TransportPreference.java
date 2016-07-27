package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;

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
    int currentHurryUpValue;

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

        if (pref.settings().isHurryUpUsed()) {
            hurryUp.setSummary(pref.user().getHurryUpTime() + " min");
        } else {
            hurryUp.setSummary(R.string.settings_hurryup_summary);
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

        hurryUp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                showNumberPicker();
                return false;
            }
        });

    }

    private void showNumberPicker() {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        final NumberPicker picker = new NumberPicker(getActivity());
        picker.setMaxValue(60);
        picker.setMinValue(5);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(50, 50);
        RelativeLayout.LayoutParams numPicker = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        numPicker.addRule(RelativeLayout.CENTER_HORIZONTAL);

        relativeLayout.setLayoutParams(params);
        relativeLayout.addView(picker, numPicker);
        picker.setValue(pref.user().getHurryUpTime());
        currentHurryUpValue = pref.user().getHurryUpTime();
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                currentHurryUpValue = i1;
            }
        });
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.settings_hurryup))
                .setMessage(Html.fromHtml(getString(R.string.settings_hurryup_summary) + " \n (in min)"))
                .setView(relativeLayout)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        pref.user().setHurryUpTime(currentHurryUpValue);
                        hurryUp.setSummary(currentHurryUpValue + " min");
                        pref.settings().setHurryUpUsed(true);
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.cancel();
                            }
                        })
                .show();
    }

}