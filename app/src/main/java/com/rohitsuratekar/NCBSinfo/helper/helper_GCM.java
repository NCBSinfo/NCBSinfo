package com.rohitsuratekar.NCBSinfo.helper;

import com.rohitsuratekar.NCBSinfo.constants.GCMConstants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dexter on 19-03-16.
 */
public class helper_GCM {

    public List<String> getTopics(String topicCode){
        List<String> topicList = new ArrayList<>();
        topicCode = topicCode.trim();
        String[] seperatedCode = topicCode.split("(?!^)");
        if(seperatedCode[0].equals("1")){topicList.add(GCMConstants.GCM_TOPIC_TALK);}
        if (seperatedCode[1].equals("1")){topicList.add(GCMConstants.GCM_TOPIC_jc);}
        if(seperatedCode[2].equals("1")){topicList.add(GCMConstants.GCM_TOPIC_student);}

        return topicList;
    }
    public String getTopicStrings(String topicoce){
        String topic = "";
        if(topicoce.equals(GCMConstants.GCM_TOPIC_TALK)){topic = "NCBS Research Talks";}
        if (topicoce.equals(GCMConstants.GCM_TOPIC_jc)){topic = "NCBS Journal Clubs";}
        if (topicoce.equals(GCMConstants.GCM_TOPIC_student)){topic="Students Activities";}
        return topic;
    }

    public int converToSeconds(int Hours, int Minutes){

        int finalSeconds;
        Calendar c = Calendar.getInstance();
        int currentHours = c.get(Calendar.HOUR_OF_DAY);
        int currentMinute = c.get(Calendar.MINUTE);
        finalSeconds = (Hours*60*60  + Minutes*60) - (currentHours*60*60 + currentMinute*60) ;
        if (finalSeconds<0){
            finalSeconds=1;
        }
        return finalSeconds;
    }

    public String timeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("h:m a d MMM yy", Locale.getDefault());
        String time = formatter.format(new Date());
        return time;
    }

    public String ModDailyStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("dMMyyyy", Locale.getDefault());
        String time = formatter.format(new Date());
        return time;
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
}
