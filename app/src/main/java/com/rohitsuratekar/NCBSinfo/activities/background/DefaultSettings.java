package com.rohitsuratekar.NCBSinfo.activities.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.Helper;
import com.rohitsuratekar.NCBSinfo.activities.background.events.SplashLoadingEvent;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportType;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.secretbiology.helpers.general.General;
import com.secretbiology.helpers.general.Log;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Default service for all resets and first time use
 */
public class DefaultSettings extends IntentService {

    public static final String RESET = "reset";
    public static final String RESET_TRANSPORT = "resetTransport";

    public DefaultSettings() {
        super("DefaultSettings");
    }

    private Context context;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            context = getApplicationContext();
            final String action = intent.getAction();
            if (RESET.equals(action)) {
                Log.inform("DefaultSettings invoked for reset");
                doReset();
            } else if (RESET_TRANSPORT.equals(action)) {
                Log.inform("DefaultSettings invoked for reset transport");
                resetTransport();
            }
        }
    }


    private void doReset() {

    }

    private void resetTransport() {
        new RouteData(context).clearAll();

        //Use default Routes

        List<RouteModel> allRoutes = new ArrayList<>();

        allRoutes.add(make("ncbs", "iisc", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_iisc_week))));

        allRoutes.add(make("ncbs", "iisc", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_iisc_sunday))));

        allRoutes.add(make("iisc", "ncbs", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_iisc_ncbs_week))));

        allRoutes.add(make("iisc", "ncbs", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_iisc_ncbs_sunday))));

        allRoutes.add(make("ncbs", "mandara", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_mandara_week))));

        allRoutes.add(make("ncbs", "mandara", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_mandara_sunday))));

        allRoutes.add(make("mandara", "ncbs", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_mandara_ncbs_week))));

        allRoutes.add(make("mandara", "ncbs", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_mandara_ncbs_sunday))));

        allRoutes.add(make("ncbs", "mandara", Calendar.MONDAY, TransportType.BUGGY,
                Helper.convertStringToList(context.getString(R.string.def_buggy_from_ncbs))));

        allRoutes.add(make("mandara", "ncbs", Calendar.MONDAY, TransportType.BUGGY,
                Helper.convertStringToList(context.getString(R.string.def_buggy_from_mandara))));

        allRoutes.add(make("ncbs", "icts", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_icts_week))));

        allRoutes.add(make("ncbs", "icts", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_icts_sunday))));

        allRoutes.add(make("icts", "ncbs", Calendar.MONDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_icts_ncbs_week))));

        allRoutes.add(make("icts", "ncbs", Calendar.SUNDAY, TransportType.SHUTTLE,
                Helper.convertStringToList(context.getString(R.string.def_icts_ncbs_sunday))));

        allRoutes.add(make("ncbs", "cbl", Calendar.SUNDAY, TransportType.TTC,
                Helper.convertStringToList(context.getString(R.string.def_ncbs_cbl))));


        for (RouteModel r : allRoutes) {
            new RouteData(context).add(r);
        }

        EventBus.getDefault().post(new SplashLoadingEvent("loading done"));

    }


    private static final String DEFAULT_TRIGGER = "none";

    private RouteModel make(String origin, String destination, int day, TransportType type, List<String> trips) {
        RouteModel route = new RouteModel();
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setDay(day);
        route.setTrips(trips);
        route.setCreatedOn(General.timeStamp());
        route.setAuthor("Default");
        route.setType(type);
        route.setRoute(0); // Handle route no into sqlight methods
        route.setTrigger(DEFAULT_TRIGGER);
        route.setNotes("");
        return route;

    }


}
