package com.rohitsuratekar.NCBSinfo.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.SQL;
import com.rohitsuratekar.NCBSinfo.constants.SettingsRelated;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.LogModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;

import java.io.File;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    Context mContext;
    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public Database(Context context) {
        super(context, SQL.DATABASE_NAME, null, SQL.DATABASE_VERSION);
        this.mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CAT_TABLE = "CREATE TABLE " + SQL.TABLE_LOG + "("
                + SQL.LOG_ID + " INTEGER PRIMARY KEY,"
                + SQL.LOG_TIMESTAMP + " TEXT,"
                + SQL.LOG_MESSAGE + " TEXT,"
                + SQL.LOG_DETAILS + " TEXT,"
                + SQL.LOG_CATEGORY + " TEXT,"
                + SQL.LOG_STATUSCODE + " INTEGER,"
                + SQL.LOG_STATUS + " INTEGER )";
        String CREATE_DATA_TABLE = "CREATE TABLE " + SQL.TABLE_DATABASE + "("
                + SQL.DATA_KEY_ID + " INTEGER PRIMARY KEY,"
                + SQL.DATA_TIMESTAMP + " TEXT,"
                + SQL.DATA_TITLE + " TEXT,"
                + SQL.DATA_DATE + " TEXT,"
                + SQL.DATA_TIME + " TEXT,"
                + SQL.DATA_VENUE +" TEXT,"
                + SQL.DATA_SPEAKER +" TEXT,"
                + SQL.DATA_ABSTRACT +" TEXT,"
                + SQL.DATA_URL +" TEXT,"
                + SQL.DATA_NEXTSPEAKER + " TEXT,"
                + SQL.DATA_DATACODE + " TEXT,"
                + SQL.DATA_ACTIONCODE + " INTEGER )";
        String CREATE_TALK_TABLE = "CREATE TABLE " + SQL.TABLE_TALK + "("
                + SQL.TALK_KEY_ID + " INTEGER PRIMARY KEY,"
                + SQL.TALK_TIMESTAMP + " TEXT,"
                + SQL.TALK_NOTIFICATION_TITLE + " TEXT,"
                + SQL.TALK_DATE + " TEXT,"
                + SQL.TALK_TIME + " TEXT,"
                + SQL.TALK_VENUE +" TEXT,"
                + SQL.TALK_SPEAKER +" TEXT,"
                + SQL.TALK_AFFILICATION +" TEXT,"
                + SQL.TALK_TITLE +" TEXT,"
                + SQL.TALK_HOST + " TEXT,"
                + SQL.TALK_DATACODE + " TEXT,"
                + SQL.TALK_ACTIONCODE + " INTEGER )";

        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_DATA_TABLE);
        db.execSQL(CREATE_TALK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SQL.TABLE_LOG);
        db.execSQL("DROP TABLE IF EXISTS " + SQL.TABLE_DATABASE);
        db.execSQL("DROP TABLE IF EXISTS " + SQL.TABLE_TALK);
        onCreate(db);
    }

    //All log commands
    public void addLogEntry (LogModel log){
        SQLiteDatabase db = this.getWritableDatabase();
        long dataCount  = DatabaseUtils.queryNumEntries(db, SQL.TABLE_LOG);
        int currentLimit =  PreferenceManager.getDefaultSharedPreferences(mContext).getInt(SettingsRelated.SETTINGS_DEVELOPERS_LOG_ITEMS, mContext.getResources().getInteger(R.integer.settings_log_entries_default));
        currentLimit = currentLimit-1;
        if (dataCount < currentLimit) {
            new LogData().addLogEntry(db, log);
        }
        else {
            int deleteLast = (int) (dataCount - currentLimit);
            deleteOldEntries(SQL.TABLE_LOG,SQL.LOG_ID,deleteLast);
            new LogData().addLogEntry(db,log);
        }
        db.close();
        Log.i("HERE","");

    }
    public List<LogModel> getAllLogEntries (){ SQLiteDatabase db = this.getWritableDatabase(); return new LogData().getAllLogEntries(db);}
    public void deleteLogEntry (LogModel log){ SQLiteDatabase db = this.getWritableDatabase(); new LogData().deleteLogEntry(db,log);}
    public void clearLogs (){ SQLiteDatabase db = this.getWritableDatabase(); new LogData().clearLogs(db); }


    //All database commands
    public void addDataEntry (DataModel data){ SQLiteDatabase db = this.getWritableDatabase(); new FetchedData().addDataEntry(db, data);}
    public List<DataModel> getFullDatabase (){ SQLiteDatabase db = this.getWritableDatabase(); return new FetchedData().getFullDatabase(db);}
    public  DataModel getDatabaseEntry(int id){SQLiteDatabase db = this.getWritableDatabase(); return new FetchedData().getDatabaseEntry(db,id);}
    public  int getIDbyTimeStamp(String timestamp){SQLiteDatabase db = this.getWritableDatabase(); return new FetchedData().getIDbyTimeStamp(db,timestamp);}
    public void updateDataEntry (DataModel data){ SQLiteDatabase db = this.getWritableDatabase(); new FetchedData().updateDataEntry(db, data);}
    public void clearDatabase (){ SQLiteDatabase db = this.getWritableDatabase(); new FetchedData().clearDatabase(db); }
    public void deleteDataEntry (DataModel data){ SQLiteDatabase db = this.getWritableDatabase(); new FetchedData().deleteDataEntry(db,data);}

    //Talk database commands
    public void addTalkEntry (TalkModel data){ SQLiteDatabase db = this.getWritableDatabase(); new TalkData().addTalkEntry(db, data);}
    public List<TalkModel> getTalkDatabase (){ SQLiteDatabase db = this.getWritableDatabase(); return new TalkData().getTalkDatabase(db);}
    public  TalkModel getTalkDataEntry(int id){SQLiteDatabase db = this.getWritableDatabase(); return new TalkData().getTalkDataEntry(db,id);}
    public void updateTalkEntry (TalkModel data){ SQLiteDatabase db = this.getWritableDatabase(); new TalkData().updateTalkEntry(db, data);}
    public void clearTalkDatabase (){ SQLiteDatabase db = this.getWritableDatabase(); new TalkData().clearTalkDatabase(db); }


    //Check if record is already ther
    public boolean isAlreadyThere(String TableName, String dbfield, String fieldValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        String Query = "SELECT * FROM " + TableName + " WHERE " + dbfield + " = '" + fieldValue+"'";
        Cursor cursor = db.rawQuery(Query, null);
        if(cursor.getCount() <= 0){ cursor.close(); return false;
        } cursor.close(); return true; }

    //Delete old entries by keep specific number of entries
    public void deleteOldEntries(String Tablename,String UID, int numberofEntriesToKeep ){
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "DELETE FROM "+Tablename+" WHERE "+UID+" IN (SELECT "+UID+" FROM "+Tablename+" ORDER BY "+UID+" ASC LIMIT "+numberofEntriesToKeep+")";
        db.execSQL(Query);
    }

    //Delete Database
    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
}