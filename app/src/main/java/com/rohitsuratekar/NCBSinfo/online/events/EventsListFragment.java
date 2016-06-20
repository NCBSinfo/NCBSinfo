package com.rohitsuratekar.NCBSinfo.online.events;

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
import com.rohitsuratekar.NCBSinfo.common.utilities.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;

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

public class EventsListFragment extends Fragment {

    //Public
    public static String BUNDLE = "bundleFragment";

    RecyclerView recyclerView;
    List<TalkModel> talkList = new ArrayList<>();
    List<TalkModel> refined_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_fragment, container, false);
        Bundle bundle = this.getArguments();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.events_recycleview);
        talkList = new TalkData(getContext()).getAll();
        Collections.sort(talkList, new Comparator<TalkModel>(){
            @Override
            public int compare(TalkModel lhs, TalkModel rhs) {
                Date entry1 = new Utilities().convertToDate(lhs.getDate(),lhs.getTime());
                Date entry2 = new Utilities().convertToDate(rhs.getDate(),rhs.getTime());
                return entry1.compareTo(entry2);
            }
        });

        if(bundle.getString(BUNDLE)!=null) {
            if (bundle.getString(BUNDLE).equals("1")){
                upcoming();
            }
            else {
                past();
            }
        }


        EventsAdapter log_adapter = new EventsAdapter(refined_list);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(log_adapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        log_adapter.setOnItemClickListener(new EventsAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
              /*  Intent intent = new Intent(getContext(), EventDetails.class);
                intent.putExtra(General.GEN_EVENTDETAILS_DATACODE,refined_list.get(position).getDatacode());
                intent.putExtra(General.GEN_EVENTDETAILS_DATA_ID,refined_list.get(position).getDataID());
                startActivity(intent);*/
            }
        });

        return rootView;
    }

    public void upcoming(){
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        Date eventDateTime = Calendar.getInstance().getTime();
        for (TalkModel entry : talkList){
            try {
                eventDateTime = eventFormat.parse(entry.getDate()+" "+entry.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c1 = Calendar.getInstance();
            c1.setTime(eventDateTime);

            if((eventDateTime.getTime()-Calendar.getInstance().getTime().getTime())>0){
                refined_list.add(entry);
            }
        }
    }

    public void past(){
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        Date eventDateTime = Calendar.getInstance().getTime();
        for (TalkModel entry : talkList){
            try {
                eventDateTime = eventFormat.parse(entry.getDate()+" "+entry.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c1 = Calendar.getInstance();
            c1.setTime(eventDateTime);

            if((eventDateTime.getTime()-Calendar.getInstance().getTime().getTime())<=0){
                refined_list.add(entry);
            }
        }
        Collections.reverse(refined_list);
    }


}
