package com.rohitsuratekar.NCBSinfo.background.services;


import android.content.Intent;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;
import com.secretbiology.helpers.general.Log;

import java.util.concurrent.TimeUnit;

public class SyncJobs extends JobService {

    public static final String RESET_ALL_JOBS = "startAllJobs";
    public static final String SINGLE_ROUTE_SYNC = "singleRouteSync";
    public static final String SYNC_PREFERENCES = "syncPreferences";
    private static final String SYNC_ROUTE_DATA = "syncRouteData";

    private FirebaseJobDispatcher dispatcher;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.inform("Sync Job request received");
        dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getBaseContext()));
        switch (params.getTag()) {
            case RESET_ALL_JOBS:
                resetAllJobs();
                break;
            case SYNC_PREFERENCES:
                syncPreferences();
                break;
            case SYNC_ROUTE_DATA:
                syncRouteData();
                break;
            case SINGLE_ROUTE_SYNC:
                syncRouteData();
                break;
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private void syncPreferences() {
        Log.inform("PreferenceSync started");
        Intent intent = new Intent(getBaseContext(), UserPreferenceService.class);
        intent.setAction(UserPreferenceService.SYNC_USER_PREFERENCES);
        startService(intent);
    }

    private void syncRouteData() {
        Log.inform("RouteSync started");
        Intent intent = new Intent(getBaseContext(), RouteSyncService.class);
        intent.setAction(RouteSyncService.SYNC_ALL);
        startService(intent);
    }

    private void resetAllJobs() {
        dispatcher.cancelAll();
        Log.inform("Cancelling all jobs");
        startPrefSync();
        startRouteSync();
    }

    private void startPrefSync() {
        int startTime = (int) TimeUnit.SECONDS.convert(12, TimeUnit.HOURS);
        int tolerance = startTime + (int) TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);
        Job myJob = dispatcher.newJobBuilder()
                .setService(SyncJobs.class)
                .setRecurring(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(startTime, tolerance))
                .setTag(SYNC_PREFERENCES)
                .build();
        dispatcher.mustSchedule(myJob);
    }

    private void startRouteSync() {
        int startTime = (int) TimeUnit.SECONDS.convert(1, TimeUnit.DAYS);
        int tolerance = startTime + (int) TimeUnit.SECONDS.convert(1, TimeUnit.HOURS);
        Job myJob = dispatcher.newJobBuilder()
                .setService(SyncJobs.class)
                .setRecurring(true)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setReplaceCurrent(true)
                .setLifetime(Lifetime.FOREVER)
                .setTrigger(Trigger.executionWindow(startTime, tolerance))
                .setTag(SYNC_ROUTE_DATA)
                .build();
        dispatcher.mustSchedule(myJob);
    }

}
