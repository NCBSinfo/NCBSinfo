package com.rohitsuratekar.NCBSinfo.background;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.database.AppData;
import com.secretbiology.helpers.general.Log;

public class CommonTasks extends IntentService {

    private static final String SEND_FAVORITE_CHANGE = "com.rohitsuratekar.NCBSinfo.background.action.sendfav";

    private static final String FAV_ROUTE = "FavRoute";

    public CommonTasks() {
        super("CommonTasks");
    }

    public static void sendFavoriteRoute(Context context, int routeID) {
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
            }
        }
    }


    private void changeFavRoute(int routeID) {
        if (routeID != -1) {
            AppData db = AppData.getDatabase(getApplicationContext());
            db.routes().removeAllFavorite();
            db.routes().setFavorite(routeID);
        } else {
            Log.error("Unable to set favorite route");
        }
    }


}
