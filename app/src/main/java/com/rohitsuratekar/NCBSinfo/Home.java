package com.rohitsuratekar.NCBSinfo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.rohitsuratekar.NCBSinfo.background.Alarms;
import com.rohitsuratekar.NCBSinfo.common.contacts.ContactList;
import com.rohitsuratekar.NCBSinfo.common.contacts.Contacts;
import com.rohitsuratekar.NCBSinfo.common.transport.TransportConstants;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.ContactsData;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;
import com.rohitsuratekar.NCBSinfo.offline.OfflineHome;
import com.rohitsuratekar.NCBSinfo.online.OnlineHome;
import com.rohitsuratekar.NCBSinfo.online.login.Registration;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements UserInformation {


    //UI elements
    Button onlineButton, offlineButton;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        //App name in middle
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);


        pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        switch (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getString(MODE, "none")) {
            case ONLINE:
                startActivity(new Intent(Home.this, OnlineHome.class));
                overridePendingTransition(0, 0);
                break;

            case OFFLINE:
                startActivity(new Intent(Home.this, OfflineHome.class));
                overridePendingTransition(0, 0);
                break;

            case registration.camp16.CAMP_MODE:
                pref.edit().putString(MODE, ONLINE).apply();
                startActivity(new Intent(Home.this, OnlineHome.class));
                overridePendingTransition(0, 0);
                break;
        }

        onlineButton = (Button) findViewById(R.id.button_home_online);
        offlineButton = (Button) findViewById(R.id.button_home_offline);

        onlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_online));
                alertDialog.setMessage(getResources().getString(R.string.warning_online_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Home.this, Registration.class);
                        startActivity(intent);
                        alertDialog.dismiss();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();

            }
        });

        offlineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog alertDialog = new AlertDialog.Builder(Home.this).create();
                alertDialog.setTitle(getResources().getString(R.string.warning_offline));
                alertDialog.setMessage(getResources().getString(R.string.warning_offline_details));
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        pref.edit().putString(Home.MODE, Home.OFFLINE).apply();
                        setTransportValue(); //Reset transport values
                        alertDialog.dismiss();
                        Intent intent = new Intent(Home.this, OfflineHome.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "GO BACK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.show();
            }
        });

        //Set transport timings first time app is open
        if (pref.getBoolean(firstTime.APP_OPEN, true)) {
            setTransportValue();
            pref.edit().putBoolean(firstTime.APP_OPEN, false).apply();
            //Start Alarms
            Intent i = new Intent(getBaseContext(), Alarms.class);
            i.putExtra(Alarms.INTENT, Alarms.RESET_ALL);
            startService(i);
        }

        //Data migration from old table
        if (!pref.getBoolean(firstTime.DATA_MIGRATED, false)) {
            Database database = new Database(getBaseContext());
            SQLiteDatabase db = database.getWritableDatabase();
            boolean migrate = false;
            try {
                String selectQuery = "SELECT  * FROM " + TalkData.TABLE_OLD_TALK;
                Cursor cursor = db.rawQuery(selectQuery, null);
                cursor.close();
                migrate = true;
            } catch (Exception e) {
                Log.i("Database", " : null");
            }
            if (migrate) {
                List<TalkModel> oldList = getOldTalks(db);
                for (TalkModel t : oldList) {
                    new TalkData(getBaseContext()).addEntry(t);
                }
                new TalkData(getBaseContext()).dropOldtable();
                db.close();
            }

            pref.edit().putBoolean(firstTime.DATA_MIGRATED, true).apply();
        }

        //Fill contacts
        if (pref.getBoolean(Contacts.FIRST_TIME_CONTACT, true)) {
            String[][] clist = new ContactList().allContacts();
            for (int i = 0; i < clist.length; i++) {
                new ContactsData(getBaseContext()).add(new ContactModel(1, clist[i][0], clist[i][1], clist[i][2], clist[i][3], "0"));
            }
            pref.edit().putBoolean(Contacts.FIRST_TIME_CONTACT, false).apply();
        }

    }

    //Sets default transport timings. Later on this will be overwritten by remote config values
    public void setTransportValue() {

        pref.edit().putString(TransportConstants.NCBS_IISC_WEEK, getResources().getString(R.string.def_ncbs_iisc_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_IISC_SUNDAY, getResources().getString(R.string.def_ncbs_iisc_sunday)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_WEEK, getResources().getString(R.string.def_iisc_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.IISC_NCBS_SUNDAY, getResources().getString(R.string.def_iisc_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_WEEK, getResources().getString(R.string.def_ncbs_mandara_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_MANDARA_SUNDAY, getResources().getString(R.string.def_ncbs_mandara_sunday)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_WEEK, getResources().getString(R.string.def_mandara_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.MANDARA_NCBS_SUNDAY, getResources().getString(R.string.def_mandara_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_WEEK, getResources().getString(R.string.def_ncbs_icts_week)).apply();
        pref.edit().putString(TransportConstants.NCBS_ICTS_SUNDAY, getResources().getString(R.string.def_ncbs_icts_sunday)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_WEEK, getResources().getString(R.string.def_icts_ncbs_week)).apply();
        pref.edit().putString(TransportConstants.ICTS_NCBS_SUNDAY, getResources().getString(R.string.def_icts_ncbs_sunday)).apply();
        pref.edit().putString(TransportConstants.NCBS_CBL, getResources().getString(R.string.def_ncbs_cbl)).apply();
        pref.edit().putString(TransportConstants.BUGGY_NCBS, getResources().getString(R.string.def_buggy_from_ncbs)).apply();
        pref.edit().putString(TransportConstants.BUGGY_MANDARA, getResources().getString(R.string.def_buggy_from_mandara)).apply();

        pref.edit().putString(TransportConstants.CAMP_BUGGY_NCBS, getResources().getString(R.string.def_camp_buggy_ncbs)).apply();
        pref.edit().putString(TransportConstants.CAMP_BUGGY_MANDARA, getResources().getString(R.string.def_camp_buggy_mandara)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_MANDARA, getResources().getString(R.string.def_camp_shuttle_mandara)).apply();
        pref.edit().putString(TransportConstants.CAMP_SHUTTLE_NCBS, getResources().getString(R.string.def_camp_shuttle_ncbs)).apply();
        pref.edit().putString(netwrok.LAST_REFRESH_REMOTE_CONFIG, new Utilities().timeStamp()).apply();
    }


    private List<TalkModel> getOldTalks(SQLiteDatabase db) {
        List<TalkModel> entryList = new ArrayList<TalkModel>();
        String selectQuery = "SELECT  * FROM " + TalkData.TABLE_OLD_TALK;
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TalkModel model = new TalkModel();
                model.setDataID(Integer.parseInt(cursor.getString(0)));
                model.setTimestamp(cursor.getString(1));
                model.setNotificationTitle(cursor.getString(2));
                model.setDate(cursor.getString(3));
                model.setTime(cursor.getString(4));
                model.setVenue(cursor.getString(5));
                model.setSpeaker(cursor.getString(6));
                model.setAffilication(cursor.getString(7));
                model.setTitle(cursor.getString(8));
                model.setHost(cursor.getString(9));
                model.setDataCode(cursor.getString(10));
                model.setActionCode(cursor.getInt(11));
                // Adding database to list
                model.setDataAction("send");
                entryList.add(model);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return entryList;
    }
}
