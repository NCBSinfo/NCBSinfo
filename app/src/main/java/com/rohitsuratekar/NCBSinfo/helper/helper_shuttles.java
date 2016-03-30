package com.rohitsuratekar.NCBSinfo.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class helper_shuttles {


    //Getting timings based on FROM, TO, DATA and isBuggy parameter
    public String[] TripsData(String from, String to, String date, int isBuggy){
        int day = 0;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        try {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(format.parse(date));
            day = d1.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] ncbs_iisc = {"07:15", "08:50", "11:30", "14:00","16:15", "18:00", "18:45", "20:30", "22:00" ,"23:00"};
        String[] ncbs_iisc_sunday = {"08:00", "10:00" ,"12:00", "14:00", "16:15", "17:45","19:00", "19:30", "22:00", "23:30"};

        String[] iisc_ncbs_monday = {"00:30","08:15", "09:30", "12:30", "15:15", "17:00","18:30","19:30","21:30","22:30"};
        String[] iisc_ncbs = {"00:00","08:15", "09:30", "12:30", "15:15", "17:00","18:30","19:30","21:30","22:30"};
        String[] iisc_ncbs_sunday = {"00:00","09:00", "11:00", "12:30","15:15","17:00","18:30", "20:00","21:30","22:30"};

        String[] ncbs_mandara_monday = {"02:00","08:00", "09:00","17:45","20:30","22:00","23:00"};
        String[] ncbs_mandara = {"00:30", "01:30","08:00", "09:00","17:45","20:30","22:00","23:00"};
        String[] ncbs_mandara_sunday = {"00:30", "01:30","09:00","10:30","20:30","22:00","23:00"};

        String[] mandara_ncbs_monday = {"00:00","07:30","08:30","09:30","18:15","21:30","22:30","23:30"};
        String[] mandara_ncbs = {"01:00","07:30","08:30","09:30","18:15","21:30","22:30","23:30"};
        String[] mandara_ncbs_sunday = {"01:00","08:30", "09:30", "18:00","21:30","22:30"};

        String[] buggy_ncbs_mandara = {"07:45", "08:30","08:45","09:00","09:30","09:45", "10:00", "10:30", "11:00", "11:30", "12:15", "12:45", "13:00", "14:30" ,"15:00", "15:30", "16:00", "16:30", "17:30", "18:00", "18:15", "18:30", "18:45" ,"19:00", "19:30" ,"20:00"};
        String[] buggy_mandara_ncbs = {"08:00","08:45","09:00","09:15","09:45","10:00","10:15","10:45","11:15","11:45","12:30","13:45","14:00","14:45","15:15","15:45","16:15","16:45","17:45","18:15","18:30","18:45","19:00","19:15","19:45","20:15"};

        String[] ncbs_icts = {"08:00", "09:00", "10:00", "13:00", "14:00", "15:30", "16:30", "17:40", "18:30", "20:00", "21:30"};
        String[] icts_ncbs = {"08:00", "09:00", "12:00", "13:00", "14:00", "15:30", "17:00", "17:40", "19:00", "20:00", "20:30", "22:30"};
        String[] ncbs_icts_sunday = {"08:30", "10:30", "12:30", "15:00", "17:00", "19:00", "21:00", "23:00"};
        String[] icts_ncbs_sunday = {"07:30", "09:30", "11:30", "14:00", "16:00", "18:00", "20:00", "22:00"};

        String[] ncbs_cbl = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};
        String[] ncbs_cbl_sunday = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};

        String[] currentRoute = {};
        if (isBuggy==0) {
            if (from.equals("ncbs") && to.equals("iisc") && day != Calendar.SUNDAY) {
                currentRoute = ncbs_iisc;
            } else if (from.equals("ncbs") && to.equals("iisc") && day == Calendar.SUNDAY) {
                currentRoute = ncbs_iisc_sunday;
            } else if (from.equals("iisc") && to.equals("ncbs") && day != Calendar.SUNDAY) {
                if(day==Calendar.MONDAY){currentRoute=iisc_ncbs_monday;}
                else {
                    currentRoute = iisc_ncbs;
                }
            } else if (from.equals("iisc") && to.equals("ncbs") && day == Calendar.SUNDAY) {
                currentRoute = iisc_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("mandara") && day != Calendar.SUNDAY) {
                if (day == Calendar.MONDAY){currentRoute=ncbs_mandara_monday;}
                else{
                    currentRoute = ncbs_mandara;
                }
            } else if (from.equals("ncbs") && to.equals("mandara") && day == Calendar.SUNDAY) {
                currentRoute = ncbs_mandara_sunday;
            } else if (from.equals("mandara") && to.equals("ncbs") && day != Calendar.SUNDAY) {
                if (day == Calendar.MONDAY){currentRoute=mandara_ncbs_monday;}
                else{
                    currentRoute = mandara_ncbs;
                }
            } else if (from.equals("mandara") && to.equals("ncbs") && day == Calendar.SUNDAY) {
                currentRoute = mandara_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("icts") && day == Calendar.SUNDAY) {
                currentRoute = ncbs_icts_sunday;
            }else if (from.equals("ncbs") && to.equals("icts") && day != Calendar.SUNDAY) {
                currentRoute = ncbs_icts;
            } else if (from.equals("icts") && to.equals("ncbs")&& day != Calendar.SUNDAY) {
                currentRoute = icts_ncbs;
            } else if (from.equals("icts") && to.equals("ncbs")&& day == Calendar.SUNDAY) {
                currentRoute = icts_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("cbl") && day== Calendar.SUNDAY) {
                currentRoute = ncbs_cbl_sunday;
            } else if (from.equals("ncbs") && to.equals("cbl") && day != Calendar.SUNDAY){
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

    //Regular shuttle/buggy timings should be broken down in Monday/Saturday/Sunday format considering timings after 12 will come on next day. This is important to calculate time left data
    public String[] ModifiedTransportList(String from, String to, String date, int isBuggy){
        int day = 0;
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault());
        try {
            Calendar d1 = Calendar.getInstance();
            d1.setTime(format.parse(date));
            day = d1.get(Calendar.DAY_OF_WEEK);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String[] ncbs_iisc = {"07:15", "08:50", "11:30", "14:00","16:15", "18:00", "18:45", "20:30", "22:00" ,"23:00"};
        String[] ncbs_iisc_sunday = {"08:00", "10:00" ,"12:00", "14:00", "16:15", "17:45","19:00", "19:30", "22:00", "23:30"};

        String[] iisc_ncbs = {"08:15", "09:30", "12:30", "15:15", "17:00","18:30","19:30","21:30","22:30","00:00"};
        String[] iisc_ncbs_sunday = {"09:00", "11:00", "12:30","15:15","17:00","18:30", "20:00","21:30","22:30","00:30"};

        String[] ncbs_mandara = {"08:00", "09:00","17:45","20:30","22:00","23:00","00:30", "01:30"};
        String[] ncbs_mandara_sunday = {"09:00","10:30","20:30","22:00","23:00","02:00"};

        String[] mandara_ncbs = {"07:30","08:30","09:30","18:15","21:30","22:30","23:30","01:00"};
        String[] mandara_ncbs_sunday = {"08:30", "09:30", "18:00","21:30","22:30","00:00"};

        String[] buggy_ncbs_mandara = {"07:45", "08:30","08:45","09:00","09:30","09:45", "10:00", "10:30", "11:00", "11:30", "12:15", "12:45", "13:00", "14:30" ,"15:00", "15:30", "16:00", "16:30", "17:30", "18:00", "18:15", "18:30", "18:45" ,"19:00", "19:30" ,"20:00"};
        String[] buggy_mandara_ncbs = {"08:00","08:45","09:00","09:15","09:45","10:00","10:15","10:45","11:15","11:45","12:30","13:45","14:00","14:45","15:15","15:45","16:15","16:45","17:45","18:15","18:30","18:45","19:00","19:15","19:45","20:15"};

        String[] ncbs_icts = {"08:00", "09:00", "10:00", "13:00", "14:00", "15:30", "16:30", "17:40", "18:30", "20:00", "21:30"};
        String[] icts_ncbs = {"08:00", "09:00", "12:00", "13:00", "14:00", "15:30", "17:00", "17:40", "19:00", "20:00", "20:30", "22:30"};
        String[] ncbs_icts_sunday = {"08:30", "10:30", "12:30", "15:00", "17:00", "19:00", "21:00", "23:00"};
        String[] icts_ncbs_sunday = {"07:30", "09:30", "11:30", "14:00", "16:00", "18:00", "20:00", "22:00"};


        String[] ncbs_cbl = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};
        String[] ncbs_cbl_sunday = {"00:00", "01:00","02:00","03:00","04:00","20:30","22:00","23:00"};

        String[] currentRoute = {};
        if (isBuggy==0) {
            if (from.equals("ncbs") && to.equals("iisc") && day != Calendar.SUNDAY) {
                currentRoute = ncbs_iisc;
            } else if (from.equals("ncbs") && to.equals("iisc") && day == Calendar.SUNDAY) {
                currentRoute = ncbs_iisc_sunday;
            } else if (from.equals("iisc") && to.equals("ncbs") && day != Calendar.SUNDAY) {
                currentRoute = iisc_ncbs;
            } else if (from.equals("iisc") && to.equals("ncbs") && day == Calendar.SUNDAY) {
                currentRoute = iisc_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("mandara") && day != Calendar.SUNDAY) {
                currentRoute = ncbs_mandara;
            } else if (from.equals("ncbs") && to.equals("mandara") && day == Calendar.SUNDAY) {
                currentRoute = ncbs_mandara_sunday;
            } else if (from.equals("mandara") && to.equals("ncbs") && day != Calendar.SUNDAY) {
                currentRoute = mandara_ncbs;
            } else if (from.equals("mandara") && to.equals("ncbs") && day == Calendar.SUNDAY) {
                currentRoute = mandara_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("icts") && day == Calendar.SUNDAY) {
                currentRoute = ncbs_icts_sunday;
            }else if (from.equals("ncbs") && to.equals("icts") && day != Calendar.SUNDAY) {
                currentRoute = ncbs_icts;
            } else if (from.equals("icts") && to.equals("ncbs")&& day != Calendar.SUNDAY) {
                currentRoute = icts_ncbs;
            } else if (from.equals("icts") && to.equals("ncbs")&& day == Calendar.SUNDAY) {
                currentRoute = icts_ncbs_sunday;
            } else if (from.equals("ncbs") && to.equals("cbl") && day==Calendar.SUNDAY) {
                currentRoute = ncbs_cbl_sunday;
            } else if (from.equals("ncbs") && to.equals("cbl") && day != Calendar.SUNDAY){
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

    public Calendar NextTransport (String from, String to, String date, int isbuggy){
        String[] trips = ModifiedTransportList(from, to, date, isbuggy);
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
                String[] nexttrips = ModifiedTransportList(from, to, dateString2, isbuggy);

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

    //Function will return time left [Seconds, Minutes, Hours, Days]
    public float[] TimeLeft (String currentTime, String DestinationTime) {

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
}