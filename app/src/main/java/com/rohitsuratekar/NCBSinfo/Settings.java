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
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.activity.DevelopersOptions;
import com.rohitsuratekar.NCBSinfo.activity.Information;
import com.rohitsuratekar.NCBSinfo.activity.Registration;
import com.rohitsuratekar.NCBSinfo.background.DataFetch;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.SettingsRelated;
import com.rohitsuratekar.NCBSinfo.helpers.AppCompatPreferenceActivity;
import com.rohitsuratekar.NCBSinfo.helpers.NumberPickerPreference;

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
                    editor.putFloat(SettingsRelated.SETTING_HURRY_UP_TIME, hurryupMin).apply();
                    preference.setSummary(String.valueOf(hurryupMin));
                    Log.i("New value",String.valueOf(hurryupMin));
                    return true;
                }

                if (prefkey.equals("setting_notification_time")) {
                    EditTextPreference EditPreference = (EditTextPreference) preference;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    int notificationOnset;
                    if (EditPreference.getText() != null) {
                        notificationOnset = Integer.parseInt((String) value);
                    } else {
                        notificationOnset = 10; //Default value
                    }
                    preference.setSummary(String.valueOf(notificationOnset));
                    notificationOnset = notificationOnset * 60;
                    editor.putInt(Preferences.PREF_NOTIFICATION_ONSET, notificationOnset).apply();
                    return true;
                }

                if (prefkey.equals("setting_dataFetchFrequency")) {
                    EditTextPreference EditPreference = (EditTextPreference) preference;
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    int currentFrequency;
                    if (EditPreference.getText() != null) {
                        currentFrequency = Integer.parseInt((String) value);
                    } else {
                        currentFrequency = 120; //Default value
                        ((EditTextPreference) preference).setText(String.valueOf(120));
                    }
                    currentFrequency = currentFrequency * 60;
                    editor.putInt(Preferences.PREF_ALARM_FREQUENCY, currentFrequency).apply();
                    preference.setSummary(String.valueOf(currentFrequency));
                    return true;
                }
            }


       else if (preference instanceof ListPreference) {

           if(prefkey.equals(SettingsRelated.SETTINGS_TRANSPORT_ROUTES)) {
               SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
               SharedPreferences.Editor editor = preferences.edit();
               editor.putInt(SettingsRelated.HOME_DEFAULT_ROUTE, Integer.parseInt(String.valueOf(value))).apply(); // value to store
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
         if(preference instanceof NumberPickerPreference){

            sBindPreferenceSummaryToValueListener.onPreferenceChange(
                    preference,
                    PreferenceManager.getDefaultSharedPreferences( preference.getContext())
                            .getInt(preference.getKey(),0));

        }
        else if (preference instanceof EditTextPreference){

             if (preference.getKey()!=null) {

                if (((EditTextPreference) preference).getEditText()!=null)
                {
                    sBindPreferenceSummaryToValueListener.onPreferenceChange(
                         preference,
                         PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                 .getString(preference.getKey(), String.valueOf(((EditTextPreference) preference).getEditText())));
                }
                 else
                {
                    sBindPreferenceSummaryToValueListener.onPreferenceChange(
                            preference,
                            PreferenceManager.getDefaultSharedPreferences(preference.getContext())
                                    .getString(preference.getKey(), String.valueOf(preference.getSummary())));

                }


                }

         }
        else{
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
    public void onBuildHeaders(List<PreferenceActivity.Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
                ||TransportPreferenceFragment.class.getName().equals(fragmentName)
                || DeveloperPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName)
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
                    i1.putExtra(General.GET_INFORMATION_INTENT,2);
                    startActivity(i1);
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

            bindPreferenceSummaryToValue(findPreference("setting_hurryup"));
            bindPreferenceSummaryToValue(findPreference(SettingsRelated.SETTINGS_TRANSPORT_ROUTES));

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


    //Developers fragment
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DeveloperPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_dev);
            setHasOptionsMenu(true);

            final Preference myPref = (Preference) findPreference("setting_gotoDevLog");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    startActivity(new Intent(getActivity().getBaseContext(), DevelopersOptions.class));

                    return false;
                }
            });

            final SwitchPreference DevOption = (SwitchPreference) findPreference("setings_onDev");
            final PreferenceCategory logcat = (PreferenceCategory) findPreference("settings_devoptions");
            final PreferenceCategory networkrelated = (PreferenceCategory) findPreference("setting_network_related");
            if (DevOption.isChecked()) {
                logcat.setEnabled(true);
                networkrelated.setEnabled(true);
            } else {
                logcat.setEnabled(false);
                networkrelated.setEnabled(false);
            }

            DevOption.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if (DevOption.isChecked()) {
                        logcat.setEnabled(true);
                        networkrelated.setEnabled(true);
                    } else {
                        logcat.setEnabled(false);
                        networkrelated.setEnabled(false);
                    }
                    return false;
                }
            });

            Preference syncPref = (Preference)findPreference("settings_sync_now");
            syncPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent service = new Intent(preference.getContext(), DataFetch.class);
                    service.putExtra(General.GEN_SERIVICE_SWITCH, Network.NET_START_FETCHING);
                    preference.getContext().startService(service);
                    Toast.makeText(preference.getContext(),"Data sync initiated",Toast.LENGTH_LONG).show();
                    return false;
                }
            });

            final EditTextPreference frequency = (EditTextPreference)findPreference("setting_dataFetchFrequency");
            final SwitchPreference autoOn = (SwitchPreference)findPreference(SettingsRelated.SETTINGS_DATA_FETCH_FREQUENCY);
            if(autoOn.isChecked()){
                frequency.setEnabled(true);
                autoOn.setSummary("Manual");
            }
            else
            {
                frequency.setEnabled(false);
                autoOn.setSummary("Optimized");
            }
            final SharedPreferences freq;
            freq = PreferenceManager.getDefaultSharedPreferences(getActivity());
            autoOn.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    if(autoOn.isChecked()){
                        frequency.setEnabled(true);
                        autoOn.setSummary("Manual");
                        freq.edit().putBoolean(SettingsRelated.SETTINGS_OPTIMIZED_DATA_SYNC,false).apply();
                    }
                    else
                    {
                        frequency.setEnabled(false);
                        autoOn.setSummary("Optimized");
                        freq.edit().putBoolean(SettingsRelated.SETTINGS_OPTIMIZED_DATA_SYNC,true).apply();
                    }

                    return true;
                }
            });

            bindPreferenceSummaryToValue(findPreference(SettingsRelated.SETTINGS_DEVELOPERS_LOG_ITEMS));
            bindPreferenceSummaryToValue(findPreference("setting_dataFetchFrequency"));


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
        //Developers fragment
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        public static class NotificationPreferenceFragment extends PreferenceFragment {
            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                addPreferencesFromResource(R.xml.pref_notification);
                setHasOptionsMenu(true);

                final Preference myPref = (Preference) findPreference("setting_notification_regisration");
                myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {

                        startActivity(new Intent(getActivity().getBaseContext(), Registration.class));
                        return true;
                    }
                });

                bindPreferenceSummaryToValue(findPreference("setting_notification_time"));


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
            addPreferencesFromResource(R.xml.pref_aboutus);
            setHasOptionsMenu(true);

            final Preference myPref = (Preference) findPreference("settings_aboutus");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent i1 = new Intent(getActivity(), Information.class);
                    i1.putExtra(General.GET_INFORMATION_INTENT,0);
                    startActivity(i1);
                    return true;
                }
            });

            final Preference myPref2 = (Preference) findPreference("settings_terms_conditions");
            myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {

                    Intent i1 = new Intent(getActivity(), Information.class);
                    i1.putExtra(General.GET_INFORMATION_INTENT,1);
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
