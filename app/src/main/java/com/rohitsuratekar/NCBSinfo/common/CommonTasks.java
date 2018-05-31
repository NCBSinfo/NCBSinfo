package com.rohitsuratekar.NCBSinfo.common;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.rohitsuratekar.NCBSinfo.database.AppData;

public class CommonTasks extends IntentService {
    static final String TAG = "CommonTasks";

    private static final String SEND_FAVORITE_CHANGE = "com.rohitsuratekar.NCBSinfo.common.action.fav";
    private static final String FAV_ROUTE = "fav_route";


    public CommonTasks() {
        super("CommonTasks");
    }


    public static void sendFavoriteRoute(Context context, int routeID) {
        Log.i(TAG, "New favorite route");
        Intent intent = new Intent(context, CommonTasks.class);
        intent.setAction(SEND_FAVORITE_CHANGE);
        intent.putExtra(FAV_ROUTE, routeID);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (SEND_FAVORITE_CHANGE.equals(action)) {
                changeFavRoute(intent.getIntExtra(FAV_ROUTE, -1));
            } else {
                reportError(action + " not found.");
            }
        }
    }

    private void changeFavRoute(int routeID) {
        if (routeID != -1) {
            AppData db = AppData.getDatabase(getApplicationContext());
            db.routes().removeAllFavorite();
            db.routes().setFavorite(routeID);
        } else {
            reportError("Unable to set favorite route");
        }
    }

    private void reportError(String error) {
        Log.i(TAG, error);
        //TODO
    }
}
