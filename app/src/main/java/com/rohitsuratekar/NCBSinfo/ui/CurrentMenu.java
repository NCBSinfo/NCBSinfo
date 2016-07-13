package com.rohitsuratekar.NCBSinfo.ui;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.background.alarms.Alarms;
import com.rohitsuratekar.NCBSinfo.constants.AlarmConstants;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.TalkData;

public class CurrentMenu implements AlarmConstants {

    CurrentActivity currentActivity;
    Menu menu;

    public CurrentMenu(CurrentActivity currentActivity, Menu menu) {
        this.currentActivity = currentActivity;
        this.menu = menu;
    }

    public CurrentMenu() {
    }

    public boolean setItems() {
        clearAllMenu();

        switch (currentActivity) {
            case TRANSPORT:
                return switchON(R.id.action_settings);
            case EVENTS:
                return switchON(R.id.action_settings, R.id.action_clear_events);
            case CONTACTS:
                return switchON(R.id.action_settings, R.id.action_clear_contacts);
            case TRANSPORT_REMINDER:
                return switchON(R.id.action_settings, R.id.action_clear_reminders);

        }

        return true;
    }

    private void clearAllMenu() {

        int[] allIDs = {
                R.id.action_settings,
                R.id.action_clear_events,
                R.id.action_clear_contacts,
                R.id.action_clear_reminders
        };

        for (int item : allIDs) {
            menu.findItem(item).setVisible(false);
        }

    }

    private boolean switchON(int... resourceIDs) {
        for (int resourceID : resourceIDs) {
            menu.findItem(resourceID).setVisible(true);
        }
        return true;
    }

    public void getAction(int resourceID, final Activity activity) {

        switch (resourceID) {
            case R.id.action_clear_events:
                showDialog(activity, "events", R.drawable.icon_updates, resourceID);
                break;
            case R.id.action_clear_contacts:
                showDialog(activity, "contacts", R.drawable.icon_contacts, resourceID);
                break;
            case R.id.action_clear_reminders:
                showDialog(activity, "reminders", R.drawable.icon_set_reminder, resourceID);
                break;
        }
    }

    private void showDialog(final Activity activity, final String title, int iconID, final int resourceID) {
        new AlertDialog.Builder(activity)
                .setTitle("Are you sure?")
                .setMessage("You are about to delete all " + title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (resourceID) {
                            case R.id.action_clear_contacts:
                                new ContactsData(activity).clearAll();
                                break;
                            case R.id.action_clear_events:
                                new TalkData(activity).clearAll();
                                break;
                            case R.id.action_clear_reminders:
                                Intent intent = new Intent(activity, Alarms.class);
                                intent.putExtra(Alarms.INTENT, alarmTriggers.DELETE_REMINDERS.name());
                                activity.sendBroadcast(intent);
                                break;
                        }


                        Toast.makeText(activity, "All " + title + " cleared", Toast.LENGTH_SHORT).show();
                        activity.recreate();
                    }
                })
                .setNegativeButton("Go Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setIcon(iconID)
                .show();
    }

}
