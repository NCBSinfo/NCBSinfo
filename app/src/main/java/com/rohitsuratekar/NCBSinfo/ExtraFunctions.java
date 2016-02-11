package com.rohitsuratekar.NCBSinfo;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Dexter on 1/17/2016.
 */
public class ExtraFunctions {

    public int RouteNo (String from, String to, int isBuggy){
        int Route = 0;
        if (isBuggy==0){
            if (from.equals("ncbs") && to.equals("iisc") ){ Route = 0;}
            else if (from.equals("iisc") && to.equals("ncbs") ){ Route = 1;}
            else if (from.equals("ncbs") && to.equals("mandara") ){ Route = 2;}
            else if (from.equals("mandara") && to.equals("ncbs") ){ Route = 3;}
            else if (from.equals("ncbs")&&to.equals("icts")){Route=6;}
            else if (from.equals("icts")&&to.equals("ncbs")){Route=7;}
            else if (from.equals("ncbs")&&to.equals("cbl")){Route=8;}
        }
        else
        {
            if (from.equals("ncbs") && to.equals("mandara") ){ Route = 4;}
            else
            {Route = 5;}
        }
        return Route;
    }

    public String[] getRouteName (int RouteNo){
        String[] place = new String[3];
        if (RouteNo==0){place[0]="ncbs";place[1]="iisc";place[2]="0";}
        else if (RouteNo==1){place[0]="iisc";place[1]="ncbs";place[2]="0";}
        else if (RouteNo==2){place[0]="ncbs";place[1]="mandara";place[2]="0";}
        else if (RouteNo==3){place[0]="mandara";place[1]="ncbs";place[2]="0";}
        else if (RouteNo==4){place[0]="ncbs";place[1]="mandara";place[2]="1";}
        else if (RouteNo==5){place[0]="mandara";place[1]="ncbs";place[2]="1";}
        else if (RouteNo==6){place[0]="ncbs";place[1]="icts";place[2]="0";}
        else if (RouteNo==7){place[0]="icts";place[1]="ncbs";place[2]="0";}
        else if (RouteNo==8){place[0]="ncbs";place[1]="cbl";place[2]="0";}
        return place;
    }

    public float[] DateTimeDifferentExample (String currentTime, String DestinationTime) {

        float[] timeLeft = new float[4];
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(currentTime);
            d2 = format.parse(DestinationTime);

            //in milliseconds
            long diff = Math.abs(d2.getTime() - d1.getTime());

            timeLeft[0] = diff / 1000 % 60; //Seconds
            timeLeft[1] = diff / (60 * 1000) % 60; //Minutes
            timeLeft[2] = diff / (60 * 60 * 1000) % 24; //Hours
            timeLeft[3] = diff / (24 * 60 * 60 * 1000); //days

        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeLeft;
    }

    public String currentLang(String code){
        String setLan ="en";
        switch (code) {
            case "0":
                setLan = "en";
                break;
            case "1":
                setLan = "mr";
                break;
            case "2":
                setLan = "bn";
                break;
        }

        return setLan;
    }


}
