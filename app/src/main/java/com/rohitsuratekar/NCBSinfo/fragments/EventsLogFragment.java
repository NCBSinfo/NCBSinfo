package com.rohitsuratekar.NCBSinfo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.EventDetails;
import com.rohitsuratekar.NCBSinfo.adapters.DevelopersLogAdapter;
import com.rohitsuratekar.NCBSinfo.adapters.EventLogAdapter;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.models.CommonEventModel;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.LogModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventsLogFragment extends Fragment {
    RecyclerView recyclerView;
    List<DataModel> entrylist = new ArrayList<>();
    List<TalkModel> talkList = new ArrayList<>();
    List<CommonEventModel> commonList = new ArrayList<>();
    List<CommonEventModel> refined_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_update_log, container, false);
        Database db = new Database(rootView.getContext());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_log_recycleview);
        entrylist = db.getFullDatabase();
        talkList = db.getTalkDatabase();
        commonList = new GeneralHelp().makeCommonList(entrylist,talkList);

        Collections.sort(commonList, new Comparator<CommonEventModel>(){
            @Override
            public int compare(CommonEventModel lhs, CommonEventModel rhs) {
                Date entry1 = new GeneralHelp().convertToDate(lhs.getDate(),lhs.getTime());
                Date entry2 = new GeneralHelp().convertToDate(rhs.getDate(),rhs.getTime());
                return entry1.compareTo(entry2);
            }
        });

        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        Date eventDateTime = Calendar.getInstance().getTime();
        for (CommonEventModel entry : commonList){
            try {
                eventDateTime = eventFormat.parse(entry.getDate()+" "+entry.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
                Log.i("FAILED", "PARSING");
            }
            Calendar c1 = Calendar.getInstance();
            c1.setTime(eventDateTime);

            if((eventDateTime.getTime()-Calendar.getInstance().getTime().getTime())>0){
                refined_list.add(entry);
            }
        }
        EventLogAdapter log_adapter = new EventLogAdapter(refined_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(log_adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        db.close();
        log_adapter.setOnItemClickListener(new EventLogAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(getContext(), EventDetails.class);
                intent.putExtra(General.GEN_EVENTDETAILS_DATACODE,refined_list.get(position).getDatacode());
                intent.putExtra(General.GEN_EVENTDETAILS_DATA_ID,refined_list.get(position).getDataID());
                startActivity(intent);
            }
        });

        return rootView;
    }
}