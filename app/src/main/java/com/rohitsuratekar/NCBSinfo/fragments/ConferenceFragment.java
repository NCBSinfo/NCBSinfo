package com.rohitsuratekar.NCBSinfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.adapters.ConferenceAdapter;
import com.rohitsuratekar.NCBSinfo.database.ConferenceData;
import com.rohitsuratekar.NCBSinfo.helpers.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.models.CommonEventModel;
import com.rohitsuratekar.NCBSinfo.models.ConferenceModel;

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

public class ConferenceFragment extends Fragment {
    RecyclerView recyclerView;
    List<ConferenceModel> fullList;
    ConferenceAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conference_list, container, false); //Just need one recycler view. Can be taken from any fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.conference_recycleview);
        fullList = new ConferenceData(getContext()).getAll();

        Collections.sort(fullList, new Comparator<ConferenceModel>(){
            @Override
            public int compare(ConferenceModel lhs, ConferenceModel rhs) {
                Date entry1 = new GeneralHelp().convertToDate(lhs.getEventDate(),lhs.getEventStartTime());
                Date entry2 = new GeneralHelp().convertToDate(rhs.getEventDate(),rhs.getEventStartTime());
                return entry1.compareTo(entry2);
            }
        });
        List<ConferenceModel> refinedList = new ArrayList<>();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        Date eventDateTime = Calendar.getInstance().getTime();
        for (ConferenceModel entry : fullList){
            try {
                eventDateTime = eventFormat.parse(entry.getEventDate()+" "+entry.getEventStartTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar c1 = Calendar.getInstance();
            c1.setTime(eventDateTime);

            if((eventDateTime.getTime()-Calendar.getInstance().getTime().getTime())>0){
                refinedList.add(entry);
            }
        }

        adapter = new ConferenceAdapter(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}

