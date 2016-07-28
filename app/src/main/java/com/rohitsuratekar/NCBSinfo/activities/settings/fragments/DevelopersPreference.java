package com.rohitsuratekar.NCBSinfo.activities.settings.fragments;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.NetworkConstants;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 12-07-16.
 */


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DevelopersPreference extends PreferenceFragment {

    Preference topic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_developers);
        setHasOptionsMenu(true);

        getActivity().setTitle(R.string.developers);

        topic = findPreference("settings_debug_topic");

        topic.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                FirebaseMessaging.getInstance().subscribeToTopic(NetworkConstants.fcmTopics.DEBUG);
                Toast.makeText(getActivity().getBaseContext(),"Subscription request sent", Toast.LENGTH_LONG).show();
                return false;
            }
        });

    }
}
