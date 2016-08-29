package com.rohitsuratekar.NCBSinfo.utilities;

import android.content.Context;

import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class General {

    public List<TalkModel> getUpcomingTalks(Context context, Date targetDate) {
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
}
