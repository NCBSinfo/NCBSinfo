package com.rohitsuratekar.NCBSinfo.common.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utilities {

    public String timeStamp(){
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public Date convertToDate(String Date, String Time){
        Date returnDate=new Date();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        try {
            returnDate = eventFormat.parse(Date+" "+Time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return returnDate;
    }

    public String[] stringToarray(String input){
        List<String> output = new ArrayList<>();
        input = input.replace("{","");
        input = input.replace("}","");
        String[] split = input.split(",");
        for (String s: split){
            s = s.replace("\"","");
            output.add(s.replaceAll("\\s+",""));
        }
        return output.toArray(new String[output.size()]);
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

}
