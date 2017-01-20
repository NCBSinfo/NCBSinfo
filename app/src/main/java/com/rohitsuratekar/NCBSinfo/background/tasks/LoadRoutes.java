package com.rohitsuratekar.NCBSinfo.background.tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.rohitsuratekar.NCBSinfo.activities.transport.TransportMethods;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.Route;
import com.rohitsuratekar.NCBSinfo.background.CurrentSession;
import com.rohitsuratekar.NCBSinfo.database.RouteData;
import com.rohitsuratekar.NCBSinfo.database.models.RouteModel;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.secretbiology.helpers.general.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.rohitsuratekar.NCBSinfo.activities.Helper.getType;

public class LoadRoutes extends AsyncTask<Object, Void, Void> {

    private OnTaskCompleted taskCompleted;
    private List<Route> all = new ArrayList<>();
    private Route currentRoute;
    private int currentIndex;
    private List<String> nextList;

    public LoadRoutes(OnTaskCompleted taskCompleted) {
        this.taskCompleted = taskCompleted;
    }

    @Override
    protected Void doInBackground(Object... params) {
        Context context = (Context) params[0];
        int favoriteRoute = new AppPrefs(context).getFavoriteRoute();
        List<String[]> routeList = new RouteData(context).getRouteNames();
        for (String[] s : routeList) {
            List<RouteModel> models = new RouteData(context).getAllDays(s[0], s[1], getType(s[2]));
            if (models != null) {
                Route r = new Route(models);
                all.add(r);
                if (favoriteRoute == r.getRouteNo()) {
                    currentRoute = r;
                }
            }
        }
        currentIndex = all.indexOf(currentRoute);
        nextList = new TransportMethods().nextTransport(Calendar.getInstance(), currentRoute);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.inform("All Routes loaded successfully");
        CurrentSession.getInstance().setRouteInfo(all, currentRoute, currentIndex);
        CurrentSession.getInstance().setNextList(nextList);
        taskCompleted.onTaskCompleted();
    }
}
