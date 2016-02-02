package com.rohitsuratekar.NCBSinfo;


import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p/>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity {
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.*/
   public static final String EXTRA_SHOW_FRAGMENT = ":android:show_fragment";
     /**
     * When starting this activity, the invoking Intent can contain this extra
     * boolean that the header list should not be displayed.  This is most often
     * used in conjunction with {@link #EXTRA_SHOW_FRAGMENT} to launch
     * the activity to display a specific fragment that the user has navigated
     * to.
     */
   public static final String EXTRA_NO_HEADERS = ":android:no_headers";
    public static int activitystart = 0;

   private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();
            String prefkey = preference.getKey();
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(prefkey,stringValue).apply(); // value to store

            if (prefkey.equals("default_language")){

                if (stringValue.equals("0")){
                    Locale locale2 = new Locale("en");
                    Locale.setDefault(locale2);
                    Configuration config2 = new Configuration();
                    config2.locale = locale2;
                    preference.getContext().getResources().updateConfiguration(
                            config2, preference.getContext().getResources().getDisplayMetrics());

                    if (activitystart==0){activitystart++;}
                    else{
                        preference.getContext().startActivity(new Intent(preference.getContext(), HomeActivity.class));
                        activitystart=0;}

                   }
                else if (stringValue.equals("1")){

                    String languageToLoad  = "mr"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    preference.getContext().getResources().updateConfiguration(config,
                            preference.getContext().getResources().getDisplayMetrics());

                    if (activitystart==0){activitystart++;}
                    else{ preference.getContext().startActivity(new Intent(preference.getContext(), HomeActivity.class));
                        activitystart=0;}

                }

                else if (stringValue.equals("2")){

                    String languageToLoad  = "kn"; // your language
                    Locale locale = new Locale(languageToLoad);
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    preference.getContext().getResources().updateConfiguration(config,
                            preference.getContext().getResources().getDisplayMetrics());

                    if (activitystart==0){activitystart++;}
                    else{ preference.getContext().startActivity(new Intent(preference.getContext(), HomeActivity.class));
                        activitystart=0;}

                }

            }

            if (preference instanceof ListPreference) {
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list.
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);

                // Set the summary to reflect the new value.
                preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);


            }
            else {
                // For all other preferences, set the summary to the value's
                // simple string representation.
                preference.setSummary(stringValue);
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
        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
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
                ||DefaultShuttleFragment.class.getName().equals(fragmentName);

    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class DefaultShuttleFragment extends PreferenceFragment {
        @Override

        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_default_shuttle);
            setHasOptionsMenu(true);

            activitystart=0;
            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
           bindPreferenceSummaryToValue(findPreference("shuttle_route_list"));
           bindPreferenceSummaryToValue(findPreference("default_language"));

            Preference myPref = (Preference) findPreference("shuttle_clear_settings");
            myPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    //open browser or intent here
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getString(R.string.alert_prefchange_head))
                            .setMessage(getString(R.string.alert_prefchange_warning))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
                                    Toast.makeText(getActivity(), getString(R.string.toast_restore_value), Toast.LENGTH_LONG).show();
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

            Preference myPref2 = (Preference) findPreference("about_us_setting");
            myPref2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                    intent.putExtra("aboutswitch", String.valueOf(0));
                    startActivity(intent);
                    return false;
                }
            });

            Preference myPref3 = (Preference) findPreference("terms_setting");
            myPref3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                    intent.putExtra("aboutswitch",String.valueOf(1));
                    startActivity(intent);
                    return false;
                }
            });

            Preference myPref4 = (Preference) findPreference("guide_setting");
            myPref4.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putBoolean("guideSeen", true).apply();
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    return false;
                }
            });

            Preference myPref5 = (Preference) findPreference("feature_setting");
            myPref5.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                    intent.putExtra("aboutswitch",String.valueOf(2));
                    startActivity(intent);
                    return false;
                }
            });

        }


        @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            Intent intent = IntentCompat.makeRestartActivityTask(new ComponentName(getActivity(), SettingsActivity.class));
            startActivity(intent);
            /*int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            }*/

            return super.onOptionsItemSelected(item);

         }

    }


}
