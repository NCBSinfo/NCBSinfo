package com.rohitsuratekar.NCBSinfo.activities.home;


import android.content.Context;
import android.content.Intent;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.dashboard.Dashboard;
import com.rohitsuratekar.NCBSinfo.activities.login.Login;
import com.rohitsuratekar.NCBSinfo.activities.transport.edit.TransportEdit;
import com.rohitsuratekar.NCBSinfo.preferences.AppPrefs;
import com.rohitsuratekar.NCBSinfo.preferences.LearningPrefs;
import com.secretbiology.helpers.general.General;

import java.util.ArrayList;
import java.util.List;

class Suggestions implements SuggestionIDs {

    private Context context;
    private LearningPrefs prefs;

    Suggestions(Context context) {
        this.context = context;
        this.prefs = new LearningPrefs(context);
    }

    public List<SuggestionModel> get() {
        List<SuggestionModel> modelList = new ArrayList<>();
        if (prefs.opened() < 4) {
            SuggestionModel intro = new SuggestionModel()
                    .setDetails(context.getString(R.string.sug_intro))
                    .setAction(NONE)
                    .setIcon(R.mipmap.ic_launcher);
            modelList.add(intro);

            SuggestionModel sugg = new SuggestionModel()
                    .setDetails(context.getString(R.string.sug_sugg))
                    .setAction(NONE)
                    .setIcon(R.drawable.icon_favorite);
            modelList.add(sugg);

        }

        SuggestionModel editTransport = new SuggestionModel()
                .setDetails(context.getString(R.string.sug_transport))
                .setIcon(R.drawable.icon_transport)
                .setAction(EDIT_TRANSPORT);
        modelList.add(editTransport);

        SuggestionModel login = new SuggestionModel()
                .setIcon(R.drawable.icon_authenticated)
                .setAction(LOGIN)
                .setDetails(context.getString(R.string.sug_login));
        modelList.add(login);

        SuggestionModel defRoute = new SuggestionModel()
                .setAction(NONE)
                .setDetails(context.getString(R.string.sug_default_route))
                .setIcon(R.drawable.icon_favorite_border);
        modelList.add(defRoute);

        SuggestionModel swipe = new SuggestionModel()
                .setAction(NONE)
                .setDetails(context.getString(R.string.sug_swipe))
                .setIcon(R.drawable.icon_home);
        modelList.add(swipe);

        SuggestionModel allTrips = new SuggestionModel()
                .setAction(NONE)
                .setDetails(context.getString(R.string.sug_all_trips))
                .setIcon(R.drawable.icon_transport);
        modelList.add(allTrips);

        List<SuggestionModel> returnList = new ArrayList<>();
        if (modelList.size() > 3) {
            for (int i = 0; i < 3; i++) {
                int no = General.randInt(0, modelList.size() - 1);
                returnList.add(modelList.get(no));
                modelList.remove(no);
            }
        } else {
            return modelList;
        }
        return returnList;
    }





}
