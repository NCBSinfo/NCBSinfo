package com.rohitsuratekar.NCBSinfo.background.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Trigger;
import com.secretbiology.helpers.general.Log;

/**
 * Created by Rohit Suratekar on 15-11-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class OnUpgrade extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.inform("App updated and sync service started");
        // restart SyncServices
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job myJob = dispatcher.newJobBuilder()
                .setService(SyncJobs.class)
                .setReplaceCurrent(true)
                .setTrigger(Trigger.executionWindow(0, 1))
                .setTag(SyncJobs.RESET_ALL_JOBS)
                .build();
        dispatcher.mustSchedule(myJob);
    }
}
