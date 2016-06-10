package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.constants.Network;
import com.rohitsuratekar.NCBSinfo.constants.Preferences;
import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.database.ConferenceData;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.helpers.LogEntry;
import com.rohitsuratekar.NCBSinfo.models.ConferenceModel;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;
import com.rohitsuratekar.retro.google.fusiontable.Commands;
import com.rohitsuratekar.retro.google.fusiontable.Service;
import com.rohitsuratekar.retro.google.fusiontable.reponse.FusionTableRow;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataFetch extends IntentService {

    public DataFetch() {
        super(DataFetch.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        new LogEntry(this, StatusCodes.STATUS_SERVICESTART);
        String serviceswitch = intent.getStringExtra(General.GEN_SERIVICE_SWITCH);
        //Switch Activities
        if (serviceswitch.equals(Network.NET_START_FETCHING)){ loadData(this);}
    }

    public void loadData(final Context context) {
        //TODO make specific shared preferences
        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_SUB_JC,false)){
            loadJCData(context, Network.NET_CBJC_TABLEID);}
        if (PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_SUB_JC,false)){
            loadJCData(context, Network.NET_DBJC_TABLEID);
        }

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(Preferences.PREF_SUB_RESEARCHTALK,false)){
        loadTalkData(context);}

        if(PreferenceManager.getDefaultSharedPreferences(getBaseContext()).getBoolean(ExternalConstants.CAMP2016_REGISTERED, false)){
            loadConference(context);
        }

        //TODO change this forced notification strategy later on
        Intent myIntent = new Intent(context , Notifications.class);
        myIntent.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
        context.sendBroadcast(myIntent);

    }

    public void loadJCData(final Context context, String TableID) {
        String sql_query = "SELECT * FROM " + TableID;
        Commands FusionTable = Service.createService(Commands.class);
        Call<FusionTableRow> call = FusionTable.getAllRows(sql_query, Network.NET_KEY);
        call.enqueue(new Callback<FusionTableRow>() {
            @Override
            public void onResponse(Call<FusionTableRow> call, Response<FusionTableRow> response) {

                if (response.isSuccess()) {
                    Database db = new Database(context);
                    int numberOfentries = 0;
                    String datacode = "JC";
                    for (int i = 0; i < response.body().getRows().size(); i++) {
                        String timestamp = response.body().getRows().get(i).get(0);
                        String title = response.body().getRows().get(i).get(1);
                        String date = response.body().getRows().get(i).get(2);
                        String time = response.body().getRows().get(i).get(3);
                        String venue = response.body().getRows().get(i).get(4);
                        String speaker = response.body().getRows().get(i).get(5);
                        String talkabstract = response.body().getRows().get(i).get(6);
                        String url = response.body().getRows().get(i).get(7);
                        String nextspeaker = response.body().getRows().get(i).get(8);
                        datacode = response.body().getRows().get(i).get(9);
                        int actioncode = SQL.ACTION_RETRIVED;
                        if(title.isEmpty()){
                            title = "Talk by "+speaker;
                        }
                        if (!db.isAlreadyThere(SQL.TABLE_DATABASE, SQL.DATA_TIMESTAMP, timestamp)) {

                            if (timestamp.length() != 0) {
                                db.addDataEntry(new DataModel(0, timestamp, title, date, time, venue, speaker, talkabstract, url, nextspeaker, datacode, actioncode));  //0 is pseudo increment
                                numberOfentries++;
                            }
                         }
                    }

                    String detailchange = "Request to server is successful and number of entries added to "+datacode+ " : "  +numberOfentries;
                    if(numberOfentries==0){ new LogEntry(getBaseContext(),StatusCodes.STATUS_DATARETRIVED);}
                    else {
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_DATARETRIVED,detailchange);
                        Intent notservice=new Intent(context,Notifications.class);
                        notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
                        context.sendBroadcast(notservice);}
                    db.close();
                } else {
                    Database db = new Database(context);
                    new LogEntry(getBaseContext(),response.code());
                    db.close();
                }
            }
            @Override
            public void onFailure(Call<FusionTableRow> call, Throwable t) {
            }
        });


    }

    public void loadTalkData(final Context context) {
        String sql_query = "SELECT * FROM " + Network.NET_RESEARCH_TALK_TABLEID;
        Commands FusionTable = Service.createService(Commands.class);
        Call<FusionTableRow> call = FusionTable.getAllRows(sql_query, Network.NET_KEY);
        call.enqueue(new Callback<FusionTableRow>() {
            @Override
            public void onResponse(Call<FusionTableRow> call, Response<FusionTableRow> response) {
                if (response.isSuccess()) {
                    Database db = new Database(context);
                    int numberOfentries = 0;
                    for (int i = 0; i < response.body().getRows().size(); i++) {
                        String timestamp = response.body().getRows().get(i).get(0);
                        String notificationtitle = response.body().getRows().get(i).get(1);
                        String date = response.body().getRows().get(i).get(2);
                        String time = response.body().getRows().get(i).get(3);
                        String venue = response.body().getRows().get(i).get(4);
                        String speaker = response.body().getRows().get(i).get(5);
                        String affliations = response.body().getRows().get(i).get(6);
                        String title = response.body().getRows().get(i).get(7);
                        String host = response.body().getRows().get(i).get(8);
                        String datacode = response.body().getRows().get(i).get(9);
                        int actioncode = SQL.ACTION_RETRIVED;
                        if(notificationtitle.isEmpty()){
                            notificationtitle = "Talk by "+speaker;
                        }
                        if (!db.isAlreadyThere(SQL.TABLE_TALK, SQL.TALK_TIMESTAMP, timestamp)) {

                            if (timestamp.length() != 0) {
                                db.addTalkEntry(new TalkModel(0, timestamp, notificationtitle, date, time, venue, speaker, affliations,title, host, datacode, actioncode));  //0 is pseudo increment
                                numberOfentries++;
                            }
                        }
                    }

                    String detailchange = "Request to server is successful and number of entries added to talks : "+numberOfentries;
                    if(numberOfentries==0){
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_DATARETRIVED);}
                    else {
                        new LogEntry(getBaseContext(),StatusCodes.STATUS_DATARETRIVED,detailchange);
                        Intent notservice=new Intent(context,Notifications.class);
                        notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_DAILYNOTIFICATION);
                        context.sendBroadcast(notservice);}
                    db.close();
                } else {
                    Database db = new Database(context);
                    new LogEntry(getBaseContext(),response.code());
                    db.close();
                }
            }
            @Override
            public void onFailure(Call<FusionTableRow> call, Throwable t) {
            }
        });

    }

    public void loadConference(final Context context) {
        String sql_query = "SELECT * FROM " + Network.CONFERENCE_TABLE;
        Commands FusionTable = Service.createService(Commands.class);
        Call<FusionTableRow> call = FusionTable.getAllRows(sql_query, Network.NET_KEY);
        call.enqueue(new Callback<FusionTableRow>() {
            @Override
            public void onResponse(Call<FusionTableRow> call, Response<FusionTableRow> response) {
                if (response.isSuccess()) {

                    Database db = new Database(context);

                    for (int i = 0; i < response.body().getRows().size(); i++) {
                        String conferenceCode = response.body().getRows().get(i).get(0);
                        String eventID = response.body().getRows().get(i).get(1);
                        String eventTitle = response.body().getRows().get(i).get(2);
                        String eventSpeaker = response.body().getRows().get(i).get(3);
                        String eventHost = response.body().getRows().get(i).get(4);
                        String startTime = response.body().getRows().get(i).get(5);
                        String endTime = response.body().getRows().get(i).get(6);
                        String date = response.body().getRows().get(i).get(7);
                        String venue = response.body().getRows().get(i).get(8);
                        String message = response.body().getRows().get(i).get(9);
                        String eventCode = response.body().getRows().get(i).get(10);
                        String updateCounter = response.body().getRows().get(i).get(11);

                        ConferenceModel entry = new ConferenceModel(0, new GeneralHelp().timeStamp(),conferenceCode,
                                eventID,eventTitle,eventSpeaker,eventHost,startTime,endTime,date,
                                venue,message,eventCode,Integer.valueOf(updateCounter));

                        if (db.isAlreadyThere(SQL.TABLE_CONFERENCE, SQL.CONFERENCE_CODE, conferenceCode)) {
                            List<ConferenceModel> allData = new ConferenceData(getBaseContext()).getAll();
                            List<ConferenceModel> refine1 = new ArrayList<ConferenceModel>();
                            for (ConferenceModel a : allData){
                                if(a.getCode().equals(ExternalConstants.CONFERENCE_CAMP2016)){
                                    refine1.add(a);
                                }
                            }
                           boolean newdataFound = true;
                            for (ConferenceModel b : refine1){

                                if(b.getEventID().equals(eventID)){
                                    if(b.getUpdateCounter()!=Integer.valueOf(updateCounter)){
                                        entry.setId(b.getId());
                                        new ConferenceData(getBaseContext()).update(entry);
                                        new LogEntry(getBaseContext(), StatusCodes.STATUS_CONFERENCE_DATA_ADDED, entry.getEventTitle());
                                        newdataFound=false;
                                    }
                                    else {
                                        newdataFound=false;
                                    }
                                }
                            }

                            if(newdataFound){
                                new ConferenceData(getBaseContext()).add(entry);
                                new LogEntry(getBaseContext(), StatusCodes.STATUS_CONFERENCE_DATA_ADDED, entry.getEventTitle());
                            }

                        }
                        else
                        {
                            new ConferenceData(getBaseContext()).add(entry);
                            new LogEntry(getBaseContext(), StatusCodes.STATUS_CONFERENCE_DATA_ADDED, entry.getEventTitle());
                        }

                    }

                } else {
                    Database db = new Database(context);
                    new LogEntry(getBaseContext(),response.code());
                    db.close();
                }
            }
            @Override
            public void onFailure(Call<FusionTableRow> call, Throwable t) {
            }
        });

    }



}
