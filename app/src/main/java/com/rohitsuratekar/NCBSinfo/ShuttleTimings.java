package com.rohitsuratekar.NCBSinfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dexter on 1/17/2016.
 */
public class ShuttleTimings {

    public String[] newTrips(String from, String to, String date, int isBuggy){
        int day = 0;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        try {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(format.parse(date));
            day = d1.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] ncbs_iisc = {"07:30", "08:15", "09:00", "11:30", "14:00", "16:15", "17:45", "18:15", "19:00", "20:30", "22:00", "23:00"};
        String[] ncbs_iisc_sunday = {"8:00", "10:00", "12:00", "14:00", "16:15", "17:45", "19:00", "19:30", "22:00", "23:30"};
        String[] iisc_ncbs = {"00:30", "07:00", "08:30", "09:15", "09:45", "12:30", "15:15", "17:00", "18:30", "19:15", "19:45", "21:30", "22:30"};
        String[] iisc_ncbs_sunday = {"00:30", "09:00", "11:00", "12:30", "15:15", "17:00", "18:30", "21:15", "21:30", "22:30"};

        String[] ncbs_mandara_monday = {"02:00", "07:45", "09:00", "18:00", "20:30", "21:30", "23:00"};
        String[] ncbs_mandara = {"00:30", "02:00", "07:45", "9:00", "18:00", "20:30", "21:30", "23:00"};
        String[] ncbs_mandara_sunday = {"00:30", "02:00", "09:30", "10:30", "11:30", "15:00", "17:30", "22:00", "23:00"};

        String[] mandara_ncbs_monday = {"00:00","07:00", "07:45", "08:30", "09:30", "18:30", "21:30", "23:30"};
        String[] mandara_ncbs = {"01:00", "07:00", "07:45", "08:30", "09:30", "18:30", "21:30", "23:30"};
        String[] mandara_ncbs_sunday = {"01:00", "08:30", "09:45", "11:00", "12:15", "16:00", "18:30", "22:30"};

        String[] buggy_ncbs_mandara = {"08:00", "08:30", "08:45", "09:00", "09:30", "09:45", "10:00", "10:30", "11:30", "12:15", "12:45", "13:00", "14:15", "15:30", "16:00", "16:30", "17:30", "17:45", "18:15", "19:00", "19:30", "19:45", "20:00"};
        String[] buggy_mandara_ncbs = {"08:15", "08:45", "09:00", "09:15", "09:45", "10:00", "10:15", "10:45", "11:45", "12:30", "13:45", "14:00", "14:45", "15:45", "16:45", "17:45", "18:15", "18:30", "19:15", "19:45", "20:00", "20:15"};

        String[] ncbs_icts = {"08:15", "09:00","10:30","15:00","18:00","18:30"};
        String[] icts_ncbs = {"09:45","14:00","17:05","17:35","19:25"};

        String[] ncbs_cbl = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};
        String[] ncbs_cbl_sunday = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};

        String[] currentRoute = {};
        if (isBuggy==0) {
            if (from.equals("ncbs") && to.equals("iisc") && day != 1) {
                currentRoute = ncbs_iisc;
            } else if (from.equals("ncbs") && to.equals("iisc") && day == 1) {
                currentRoute = ncbs_iisc_sunday;
            } else if (from.equals("iisc") && to.equals("ncbs") && day != 1) {
                currentRoute = iisc_ncbs;
            } else if (from.equals("iisc") && to.equals("ncbs") && day == 1) {
                currentRoute = iisc_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("mandara") && day != 1) {
                if (day == 2){currentRoute=ncbs_mandara_monday;}
                else{
                    currentRoute = ncbs_mandara;
                }
            } else if (from.equals("ncbs") && to.equals("mandara") && day == 1) {
                currentRoute = ncbs_mandara_sunday;
            } else if (from.equals("mandara") && to.equals("ncbs") && day != 1) {
                if (day == 2){currentRoute=mandara_ncbs_monday;}
                else{
                    currentRoute = mandara_ncbs;
                }
            } else if (from.equals("mandara") && to.equals("ncbs") && day == 1) {
                currentRoute = mandara_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("icts")) {
                currentRoute = ncbs_icts;
            } else if (from.equals("icts") && to.equals("ncbs")) {
                currentRoute = icts_ncbs;
            } else if (from.equals("ncbs") && to.equals("cbl") && day==1) {
                currentRoute = ncbs_cbl_sunday;
            } else if (from.equals("ncbs") && to.equals("cbl") && day != 1){
                currentRoute = ncbs_cbl;
            }
        }
        else {
            if (from.equals("ncbs") && to.equals("mandara")) {
                currentRoute = buggy_ncbs_mandara;
            }
            else{
                currentRoute = buggy_mandara_ncbs;
            }

        }
        return currentRoute;
    }

    public Calendar newNextShuttle (String from, String to, String date, int isbuggy){
        String[] trips = newTrips(from, to, date, isbuggy);
        List<String> convertedList = new ArrayList<String>();
        int sec = 0;
        Calendar returnString = Calendar.getInstance();
        String dateString = "";
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        SimpleDateFormat shortformat = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());

        try {
            Date ak = format.parse(date);
            dateString = shortformat.format(ak);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (int i =0;i<trips.length;i++){
            String temp2 = String.format("%s %s:%d", dateString, trips[i],sec);
            convertedList.add(temp2);
        }
        int getbreak = 0;
        for (int j =0; j<convertedList.size();j++){
            Date d1 = null;
            Date d2 = null;
            try {
                d1= format.parse(convertedList.get(j));
                d2 = format.parse(date);
                Calendar re = Calendar.getInstance();
                if ((d2.getTime() - d1.getTime())<0){
                    re.setTime(format.parse(convertedList.get(j)));
                    returnString = re;
                    getbreak =1;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (getbreak==1){break;}
        }
        if (getbreak ==0){
            Calendar d2_new = Calendar.getInstance();
            String dateString2 = "";
            try {
                d2_new.setTime(format.parse(date));
                d2_new.add(Calendar.DATE, 1);
                String TempString = shortformat.format(d2_new.getTime());

                dateString2 = String.format("%s 00:00:00",TempString);
                String[] nexttrips = newTrips(from, to, dateString2, isbuggy);

                String temp2 = String.format("%s %s:%s", TempString, nexttrips[0], 0);
                Calendar finalTime = Calendar.getInstance();
                finalTime.setTime(format.parse(temp2));
                returnString = finalTime;
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return returnString;
    }

    public String[] OnlyTrips(String from, String to, String date, int isBuggy){
        int day = 0;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        try {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(format.parse(date));
            day = d1.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] ncbs_iisc = {"07:30", "08:15", "09:00", "11:30", "14:00", "16:15", "17:45", "18:15", "19:00", "20:30", "22:00", "23:00"};
        String[] ncbs_iisc_sunday = {"8:00", "10:00", "12:00", "14:00", "16:15", "17:45", "19:00", "19:30", "22:00", "23:30"};
        String[] iisc_ncbs = {"07:00", "08:30", "09:15", "09:45", "12:30", "15:15", "17:00", "18:30", "19:15", "19:45", "21:30", "22:30","00:30"};
        String[] iisc_ncbs_sunday = {"09:00", "11:00", "12:30", "15:15", "17:00", "18:30", "21:15", "21:30", "22:30","00:30"};

        String[] ncbs_mandara = {"07:45", "9:00", "18:00", "20:30", "21:30", "23:00","00:30","02:00"};
        String[] ncbs_mandara_sunday = {"09:30", "10:30", "11:30", "15:00", "17:30", "22:00", "23:00","02:00"};

        String[] mandara_ncbs = {"07:00", "07:45", "08:30", "09:30", "18:30", "21:30", "23:30","01:00"};
        String[] mandara_ncbs_sunday = {"08:30", "09:45", "11:00", "12:15", "16:00", "18:30", "22:30","00:00"};

        String[] buggy_ncbs_mandara = {"08:00", "08:30", "08:45", "09:00", "09:30", "09:45", "10:00", "10:30", "11:30", "12:15", "12:45", "13:00", "14:15", "15:30", "16:00", "16:30", "17:30", "17:45", "18:15", "19:00", "19:30", "19:45", "20:00"};
        String[] buggy_mandara_ncbs = {"08:15", "08:45", "09:00", "09:15", "09:45", "10:00", "10:15", "10:45", "11:45", "12:30", "13:45", "14:00", "14:45", "15:45", "16:45", "17:45", "18:15", "18:30", "19:15", "19:45", "20:00", "20:15"};

        String[] ncbs_icts = {"08:15", "09:00","10:30","15:00","18:00","18:30"};
        String[] icts_ncbs = {"09:45","14:00","17:05","17:35","19:25"};


        String[] ncbs_cbl = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};
        String[] ncbs_cbl_sunday = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};

        String[] currentRoute = {};
        if (isBuggy==0) {
            if (from.equals("ncbs") && to.equals("iisc") && day != 1) {
                currentRoute = ncbs_iisc;
            } else if (from.equals("ncbs") && to.equals("iisc") && day == 1) {
                currentRoute = ncbs_iisc_sunday;
            } else if (from.equals("iisc") && to.equals("ncbs") && day != 1) {
                currentRoute = iisc_ncbs;
            } else if (from.equals("iisc") && to.equals("ncbs") && day == 1) {
                currentRoute = iisc_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("mandara") && day != 1) {
                currentRoute = ncbs_mandara;
            } else if (from.equals("ncbs") && to.equals("mandara") && day == 1) {
                currentRoute = ncbs_mandara_sunday;
            } else if (from.equals("mandara") && to.equals("ncbs") && day != 1) {
                currentRoute = mandara_ncbs;
            } else if (from.equals("mandara") && to.equals("ncbs") && day == 1) {
                currentRoute = mandara_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("icts")) {
                currentRoute = ncbs_icts;
            } else if (from.equals("icts") && to.equals("ncbs")) {
                currentRoute = icts_ncbs;
            } else if (from.equals("ncbs") && to.equals("cbl") && day==1) {
                currentRoute = ncbs_cbl_sunday;
            } else if (from.equals("ncbs") && to.equals("cbl") && day != 1){
                currentRoute = ncbs_cbl;
            }
        }
        else {
            if (from.equals("ncbs") && to.equals("mandara")) {
                currentRoute = buggy_ncbs_mandara;
            }
            else{
                currentRoute = buggy_mandara_ncbs;
            }

        }
        return currentRoute;
    }
}
