package com.rohitsuratekar.NCBSinfo.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.rohitsuratekar.NCBSinfo.constants.Network;

/**
 * Created by Rohit Suratekar on 10-04-16.
 */
public class NetworkRelated {

    public boolean isConnected (Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public String getTopicCode(boolean[] values){

        String code="";
        for (int i = 0; i<values.length;i++){
            if(values[i]){
                code=code+"1";
            }
            else{
                code=code+"0";;
            }
        }
        return code;
    }

    public String getTopicStrings(String topicoce){
        String topic = "";
        if(topicoce.equals(Network.GCM_TOPIC_RESEARCHTALK)){topic = "NCBS Research Talks";}
        if (topicoce.equals(Network.GCM_TOPIC_JC)){topic = "NCBS Journal Clubs";}
        if (topicoce.equals(Network.GCM_TOPIC_STUDENT_ACTIVITY)){topic="Students Activities";}
        if (topicoce.equals(Network.GCM_TOPIC_PUBLIC)) {topic = "Public";}
        return topic;
    }
}
