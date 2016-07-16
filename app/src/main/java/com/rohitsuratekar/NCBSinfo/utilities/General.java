package com.rohitsuratekar.NCBSinfo.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class General {

    /**
     * Checks if network is available.
     * Warning: it won't check if internet is available or not.
     *
     * @param context : application context
     * @return : true if network is available
     */
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Checks if user is using proxy
     *
     * @return true if there is any proxy on the network
     */
    public boolean isOnProxy() {
        List<Proxy> proxyList = ProxySelector.getDefault().select(URI.create("http://www.google.com"));
        return proxyList.size() <= 0 || proxyList.get(0).address() != null;
    }

    /**
     * Get current proxy address
     *
     * @return : returns proxy address of network if it is on proxy
     */
    public String getProxyAddress() {
        List<Proxy> proxyList = ProxySelector.getDefault().select(URI.create("http://www.google.com"));
        if (proxyList.size() <= 0 || proxyList.get(0).address() != null) {
            return proxyList.get(0).address().toString();
        } else return "null";
    }

    /**
     * Generates random integer within given range
     *
     * @param min : minimum value
     * @param max : maximum value
     * @return : random integer between min and max
     */
    public int randInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }

    /**
     * @return : Current timestamp
     */
    public String timeStamp() {
        return new DateConverters().convertToString(new Date(), DateFormats.TIMESTAMP_STANDARD);
    }

    public List<TalkModel> getUpcomigTalks(Context context, Date targetDate) {
        List<TalkModel> allList = new TalkData(context).getAll();
        List<TalkModel> returnList = new ArrayList<>();
        for (TalkModel talk : allList) {
            Date tempdate = new DateConverters().convertToDate(talk.getDate() + " " + talk.getTime());
            //Upcoming events will be before target date and after current date
            if (tempdate.before(targetDate) && tempdate.after(new Date())) {
                returnList.add(talk);
            }
        }
        return returnList;
    }

    /**
     * Sets color to imageview considering compatibility issues
     *
     * @param context   : to get resources
     * @param imageView : Imageview
     * @param color     : Color resources
     */
    public void setColorToIcon(Context context, ImageView imageView, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            imageView.setColorFilter(context.getResources().getColor(color, context.getTheme()));
        } else {
            imageView.setColorFilter(context.getResources().getColor(color));
        }
    }

    /**
     * Sets color to textview considering compatibility issues
     *
     * @param context  : to get resources
     * @param textView : TextView
     * @param color    : Color resources
     */
    public void setColorToText(Context context, TextView textView, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(context.getResources().getColor(color, context.getTheme()));
        } else {
            textView.setTextColor(context.getResources().getColor(color));
        }
    }

    /**
     * Converts simple time into readable format
     *
     * @param date : Date
     * @return : Readable time in String
     */
    public String makeReadableTime(Date date) {
        DateTime timestamp = new DateTime(date);
        DateTime currentTime = new DateTime(new Date());
        Interval interval = new Interval(timestamp, currentTime);
        int Minute = interval.toPeriod().getMinutes();
        int Hours = interval.toPeriod().getHours();
        int Days = interval.toPeriod().getDays();
        if (Days == 0) {
            if (Hours == 0) {
                if (Minute == 0) {
                    return "few seconds ago";
                } else if (Minute == 1) {
                    return "a minute ago";
                } else return Minute + " minutes ago";
            } else if (Hours == 1) {
                return "an hour ago";
            } else return Hours + " hours ago";
        } else if (Days == 1) {
            return "yesterday";
        } else if (Days > 1 && Days < 6) {
            return " days ago";
        } else return new DateConverters().convertToString(date, DateFormats.READABLE_DATE);


    }

}
