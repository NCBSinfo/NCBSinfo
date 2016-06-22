package com.rohitsuratekar.NCBSinfo.common.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rohitsuratekar.NCBSinfo.interfaces.NetworkConstants;
import com.rohitsuratekar.NCBSinfo.background.NotificationService;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.database.NotificationData;
import com.rohitsuratekar.NCBSinfo.database.models.NotificationModel;
import com.rohitsuratekar.NCBSinfo.online.dashboard.DashBoard;

public class AutoConfiguration implements NetworkConstants, UserInformation {
    Context context;

    public AutoConfiguration(Context context) {
        this.context = context;
    }

    public void nameNotifications() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        NotificationModel note = new NotificationModel();
        note.setTimestamp(new Utilities().timeStamp());
        note.setFrom(topics.PUBLIC);
        note.setTitle("General notifications");
        note.setExtraVariables("automatic");
        note.setMessage("Buggy breakdowns, shuttle timing change etc.");
        note.setId(0);
        new NotificationData(context).add(note);
        note.setTimestamp(new Utilities().timeStamp());
        note.setFrom(topics.EMERGENCY);
        note.setTitle("Important notifications");
        note.setExtraVariables("automatic");
        note.setMessage("Telephone lines or Internet link down, GOT next episode is leaked etc");
        note.setId(0);
        new NotificationData(context).add(note);
        String title = "Hey " + pref.getString(registration.USERNAME, "User")
                + ", check your new dashboard.";
        String message = "New functionality of NCBSinfo app";
        new NotificationService(context).sendNotification(title, message, DashBoard.class);
        pref.edit().putBoolean(firstTime.FIRST_NOTIFICATION_DASHBOARD, false).apply();
    }
}
