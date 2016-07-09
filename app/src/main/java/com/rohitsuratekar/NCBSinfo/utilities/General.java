package com.rohitsuratekar.NCBSinfo.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class General {

    private final String TAG = getClass().getSimpleName();

    //Check network condition
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public int randInt(int min, int max) {
        return min + (int) (Math.random() * ((max - min) + 1));
    }


    public String timeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        return formatter.format(new Date());
    }

    public int getMilliseconds(String timestamp) {
        Date dt = new Date();
        DateFormat currentformat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());
        try {
            dt = currentformat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "Date parsing failed in getMilliseconds");
        }
        return (int) dt.getTime();
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


}
