package com.rohitsuratekar.NCBSinfo;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.common.AppCompatPreferenceActivity;
import com.rohitsuratekar.NCBSinfo.common.CurrentMode;
import com.rohitsuratekar.NCBSinfo.common.Information;
import com.rohitsuratekar.NCBSinfo.common.transport.Transport;

import java.util.List;

public class Settings extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    public static int activitystart = 0; // To start activity
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {

            String prefkey = preference.getKey();
            if (preference instanceof EditTextPreference) {
                if (prefkey.equals("setting_hurryup")) {
                    EditTextPreference EditPreference = (EditTextPreference) preference;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    float hurryupMin;
                    if (EditPreference.getText() != null) {
                        hurryupMin = Float.parseFloat((String) value);
                    } else {
                        hurryupMin = 5;
                    }
                    editor.putFloat("setting_hurryup_minutes", hurryupMin).apply();
                    preference.setSummary(String.valueOf(hurryupMin));
                    ((EditTextPreference) preference).setText(String.valueOf(hurryupMin));
                    return true;
                }


            } else if (preference instanceof ListPreference) {

                if (prefkey.equals("settings_transport_routes")) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putInt(Transport.DEFAULT_ROUTE, Integer.parseInt(String.valueOf(value))).apply(); // value to store
                }
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(value.toString());

                // Set the summary to reflect the new value.
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null);

            } else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(String.valueOf(value));
            }
            return true;
        }
    };

    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Binds a preference's summary to its value. More specifically, when the
     * preference's value is changed, its summary (line of text below the
     * preference title) is updated to reflect the value. The summary is also
     * immediately updated upon calling this method. The exact display format is
     * dependent on the type of preference.
     *
     * @see #sBindPreferenceSummaryToValueListener
     */
    private static void bindPreferenceSummaryToValue(Preference preference) {
        // Set the listener to watch for value changes.
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        // Trigger the listener immediately with the preference's
        // current value.
        if (preference instanceof EditTextPreference) {

            if (preference.getKey() != null) {

                if (((EditTextPreference) preference).getEditText() != null) {
                    sBindPreferenceSummaryToValueListener.onPreferenceChange(
                            preference,
                            PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                    .getString(preference.getKey(), String.valueOf(preference.getSummary())));
                } else {
                    sBindPreferenceSummaryToValueListener.onPreferenceChange(
                            preference,
                            PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                    .getString(preference.getKey(), String.valueOf(preference.getSummary())));

                }

            }

        } else {
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
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
                NavUtils.navigateUpFromSameTask(this);
            }
            return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    /**
     * {@inheritDoc}
     */
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
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                || TransportPreferenceFragment.class.getName().equals(fragmentName)
                || AboutusPreferenceFragment.class.getName().equals(fragmentName);
    }

    /**
     * This fragment shows general preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            Preference myPref = (Preference) findPreference("clear_settings");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Warning!")
                            .setMessage("Want to clear preferences ?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
                                    Toast.makeText(getActivity(), "User Preferences Cleared", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    return false;
                }
            });

            final Preference myPref2 = (Preference) findPreference("settings_knownBugs");
            myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent i1 = new Intent(getActivity(), Information.class);
                    i1.putExtra(Information.INTENT, 2);
                    startActivity(i1);
                    return true;
                }
            });

            Preference myPref3 = (Preference) findPreference("change_mode_settings");
            myPref3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Change mode!")
                            .setMessage(new CurrentMode(getActivity(), Transport.MODE_CONSTANT).getSwitchModeMessage())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete

                                    Toast.makeText(getActivity(), "User Preferences Cleared", Toast.LENGTH_LONG).show();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

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


    //Shuttle Fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class TransportPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_transport);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.

            bindPreferenceSummaryToValue(findPreference(Transport.HURRY_UP));
            bindPreferenceSummaryToValue(findPreference("settings_transport_routes"));

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


    //About us fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class AboutusPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_about_us);
            setHasOptionsMenu(true);

            final Preference myPref = (Preference) findPreference("settings_aboutus");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent i1 = new Intent(getActivity(), Information.class);
                    i1.putExtra(Information.INTENT, 0);
                    startActivity(i1);
                    return true;
                }
            });

            final Preference myPref2 = (Preference) findPreference("settings_terms_conditions");
            myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent i1 = new Intent(getActivity(), Information.class);
                    i1.putExtra(Information.INTENT, 1);
                    startActivity(i1);
                    return true;
                }
            });

            final Preference myPref3 = (Preference) findPreference("settings_github");
            myPref3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    String currenturl = "https://github.com/NCBSinfo/NCBSinfo";
                    Intent i2 = new Intent(Intent.ACTION_VIEW);
                    i2.setData(Uri.parse(currenturl));
                    startActivity(i2);
                    return true;
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

}
