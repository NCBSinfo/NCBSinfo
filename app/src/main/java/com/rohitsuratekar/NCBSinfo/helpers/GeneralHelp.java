package com.rohitsuratekar.NCBSinfo.helpers;

import android.util.Log;
import android.widget.Switch;

import com.rohitsuratekar.NCBSinfo.models.CommonEventModel;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;
import com.rohitsuratekar.retro.google.gcm.reponse.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class GeneralHelp {

    public String timeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public int reverseTimestamp(String timestamp){
        Date dt = new Date();
        DateFormat currentformat = new SimpleDateFormat("hh:mm:ss a d MMM yy",Locale.getDefault());
        try {
            dt = currentformat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
        }
        return (int) dt.getTime();
    }

    public Date convertToDate(String Date, String Time){
        Date returnDate=new Date();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        try {
            returnDate = eventFormat.parse(Date+" "+Time);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
        }
        return returnDate;
    }

    public int getMiliseconds(String timestamp){
        Date dt = new Date();
        DateFormat currentformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss",Locale.getDefault());
        try {
            dt = currentformat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
        }
        return (int) dt.getTime();
    }

    public CommonEventModel convertToCommonEvent (DataModel d){

        CommonEventModel common = new CommonEventModel();
        common.setDataID(d.getDataID());
        common.setTimestamp(d.getTimestamp());
        common.setDate(d.getDate());
        common.setTime(d.getTime());
        common.setNotificationTitle(d.getNotificationTitle());
        common.setDatacode(d.getDatacode());
        common.setActioncode(d.getActioncode());
        common.setSpeaker(d.getSpeaker());
        common.setVenue(d.getVenue());
        common.setCommonItem1(d.getTalkabstract());
        common.setCommonItem2(d.getUrl());
        common.setCommonItem3(d.getNextspeaker());
        return common;

    }

    public List<CommonEventModel> convertToCommonEvent (List<DataModel> data){
        List<CommonEventModel> datalist = new ArrayList<>();
        for (DataModel d : data){
        CommonEventModel common = new CommonEventModel();
        common.setDataID(d.getDataID());
        common.setTimestamp(d.getTimestamp());
        common.setDate(d.getDate());
        common.setTime(d.getTime());
        common.setNotificationTitle(d.getNotificationTitle());
        common.setDatacode(d.getDatacode());
        common.setActioncode(d.getActioncode());
        common.setSpeaker(d.getSpeaker());
        common.setVenue(d.getVenue());
        common.setCommonItem1(d.getTalkabstract());
        common.setCommonItem2(d.getUrl());
        common.setCommonItem3(d.getNextspeaker());
            datalist.add(common);
        }
        return datalist;
    }

    public CommonEventModel convertToCommonEvent (TalkModel d){

        CommonEventModel common = new CommonEventModel();
        common.setDataID(d.getDataID());
        common.setTimestamp(d.getTimestamp());
        common.setDate(d.getDate());
        common.setTime(d.getTime());
        common.setNotificationTitle(d.getNotificationTitle());
        common.setDatacode(d.getDatacode());
        common.setActioncode(d.getActioncode());
        common.setSpeaker(d.getSpeaker());
        common.setVenue(d.getVenue());
        common.setCommonItem1(d.getAffilication());
        common.setCommonItem2(d.getTitle());
        common.setCommonItem3(d.getHost());
        return common;

    }

    public List<CommonEventModel> makeCommonList (List<DataModel> datalist ,List<TalkModel> talklist){
        List<CommonEventModel> commonList = new ArrayList<>();
        for (DataModel data : datalist){
            commonList.add(convertToCommonEvent(data));
        }
        for (TalkModel talk : talklist){
            commonList.add(convertToCommonEvent(talk));
        }
        return commonList;
    }

    public DataModel CommonEventToData (CommonEventModel d){

        DataModel common = new DataModel();
        common.setDataID(d.getDataID());
        common.setTimestamp(d.getTimestamp());
        common.setDate(d.getDate());
        common.setTime(d.getTime());
        common.setNotificationTitle(d.getNotificationTitle());
        common.setDatacode(d.getDatacode());
        common.setActioncode(d.getActioncode());
        common.setSpeaker(d.getSpeaker());
        common.setVenue(d.getVenue());
        common.setTalkabstract(d.getCommonItem1());
        common.setNextspeaker(d.getCommonItem3());
        common.setUrl(d.getCommonItem2());
        return common;

    }


    public TalkModel CommonEventToTalk (CommonEventModel d){

        TalkModel common = new TalkModel();
        common.setDataID(d.getDataID());
        common.setTimestamp(d.getTimestamp());
        common.setDate(d.getDate());
        common.setTime(d.getTime());
        common.setNotificationTitle(d.getNotificationTitle());
        common.setDatacode(d.getDatacode());
        common.setActioncode(d.getActioncode());
        common.setSpeaker(d.getSpeaker());
        common.setVenue(d.getVenue());
        common.setAffilication(d.getCommonItem1());
        common.setTitle(d.getCommonItem2());
        common.setHost(d.getCommonItem3());
        return common;

    }

    public String makeReadableTime (int timeInmilliseconds){
        Calendar cal = Calendar.getInstance();

        long diff = (cal.getTimeInMillis()-timeInmilliseconds);
        long seconds = diff / 1000 % 60; //Seconds
        long min = diff / (60 * 1000) % 60; //Minutes
        long hour = diff / (60 * 60 * 1000) % 24; //Hours
        long days = diff / (24 * 60 * 60 * 1000); //days
        cal.setTimeInMillis(timeInmilliseconds);
        String returnString;
        if(min<=1){ returnString = seconds + " seconds ago";}
        else if(min<=10){ returnString = "Few minutes ago";}
        else if(hour<=1 && min>10) {returnString = min+" minutes ago";}
        else if(hour>=1 && hour<5) {returnString = "Few hours ago";}
        else if(hour>=5 && hour<24) {returnString = hour+" hours ago";}
        else if(hour>=24 && days<2) {returnString = "Yesterday";}
        else if (days>=2 && days <5) {returnString = days + " days ago";}
        else{returnString = new SimpleDateFormat("d MMM", Locale.getDefault()).format(timeInmilliseconds);}
        return returnString;
    }


}
