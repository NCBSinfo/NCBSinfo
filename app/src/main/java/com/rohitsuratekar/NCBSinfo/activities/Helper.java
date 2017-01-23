package com.rohitsuratekar.NCBSinfo.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.WindowManager;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class Helper {

    public static List<String> convertStringToList(String s) {
        List<String> list = new ArrayList<>();
        String[] arr = s.replace("{", "").replace("}", "").split(",");
        for (String m : arr) {
            list.add(m.trim());
        }
        return list;
    }

    public static TransportType getType(String string) {
        for (TransportType type : TransportType.values()) {
            if (type.toString().equals(string.toUpperCase())) {
                return type;
            }
        }
        return TransportType.SHUTTLE;
    }

    public OkHttpClient getClient() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpClient.addInterceptor(interceptor).build();
    }

    public void legacyDefaultConverter(Context context, int route) {
        AppPrefs prefs = new AppPrefs(context);
        String origin = "ncbs";
        String destination = "iisc";
        TransportType type = TransportType.SHUTTLE;
        switch (route) {
            case 1:
                origin = "iisc";
                destination = "ncbs";
                break;
            case 2:
                origin = "ncbs";
                destination = "mandara";
                break;
            case 3:
                origin = "mandara";
                destination = "ncbs";
                break;
            case 4:
                origin = "ncbs";
                destination = "mandara";
                type = TransportType.BUGGY;
                break;
            case 5:
                origin = "mandara";
                destination = "ncbs";
                type = TransportType.BUGGY;
                break;
            case 6:
                origin = "ncbs";
                destination = "icts";
                break;
            case 7:
                origin = "icts";
                destination = "ncbs";
                break;
            case 8:
                origin = "ncbs";
                destination = "cbl";
                type = TransportType.TTC;
                break;
        }
        prefs.setFavoriteOrigin(origin);
        prefs.setFavoriteDestination(destination);
        prefs.setFavoriteType(type);
        int getRoute = new RouteData(context).checkIfExistsRoute(origin, destination, type);
        if (getRoute != -1) {
            prefs.setFavoriteRoute(getRoute);
        }
    }


    public static int[] getRandomColor() {
        List<int[]> colorList = new ArrayList<>();
        colorList.add(new int[]{R.color.md_green_400, R.color.md_green_700});
        colorList.add(new int[]{R.color.md_red_400, R.color.md_red_700});
        colorList.add(new int[]{R.color.md_purple_400, R.color.md_purple_700});
        colorList.add(new int[]{R.color.md_teal_400, R.color.md_teal_700});
        colorList.add(new int[]{R.color.md_light_green_400, R.color.md_light_green_700});
        colorList.add(new int[]{R.color.md_lime_400, R.color.md_lime_700});
        colorList.add(new int[]{R.color.md_amber_400, R.color.md_amber_700});
        colorList.add(new int[]{R.color.md_orange_400, R.color.md_orange_700});
        colorList.add(new int[]{R.color.md_brown_400, R.color.md_brown_700});

        return colorList.get(General.randInt(0, colorList.size() - 1));
    }


    public void changeDarkColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().setStatusBarColor(General.getColor(activity, color));
        }
    }

}
