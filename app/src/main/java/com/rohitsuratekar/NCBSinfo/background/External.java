package com.rohitsuratekar.NCBSinfo.background;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.Home;
import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.database.ExternalData;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.models.ExternalModel;

public class External {
    Bundle data;
    Context context;
    public External(Context context, Bundle data) {
        this.context = context;
        this.data = data;
        if (data.getString(ExternalConstants.GEN_EXTERNAL_CODE,"null").equals(ExternalConstants.CONFERENCE_CAMP2016)){
            CAMP2016();
        }
    }

    private void CAMP2016(){
        ExternalModel entry = new ExternalModel();
        entry.setId(1);
        entry.setCode(data.getString(ExternalConstants.GEN_EXTERNAL_CODE,"null"));
        entry.setTimestamp(new GeneralHelp().timeStamp());
        entry.setTitle(data.getString(ExternalConstants.CAMP2016_GCM_TITLE,"null"));
        entry.setMessage(data.getString(ExternalConstants.CAMP2016_GCM_MESSAGE,"null"));
        entry.setExtra(data.getString(ExternalConstants.CAMP2016_GCM_EXTRA,"null"));
        new ExternalData(context).add(entry);
        //Notify
        Intent notservice=new Intent(context,Notifications.class);
        notservice.putExtra(General.GEN_NOTIFICATION_INTENT,General.GEN_SENDNOTIFICATION);
        notservice.putExtra(General.GEN_NOTIFICATION_DATACODE,ExternalConstants.CONFERENCE_CAMP2016);
        notservice.putExtra(General.GEN_NOTIFY_TITLE,data.getString(ExternalConstants.CAMP2016_GCM_TITLE,"New notification"));
        notservice.putExtra(General.GEN_NOTIFY_MESSAGE,data.getString(ExternalConstants.CAMP2016_GCM_MESSAGE,"null"));
        context.sendBroadcast(notservice);
    }
}
