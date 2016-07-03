package com.rohitsuratekar.NCBSinfo.online.temp.camp;

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
import com.rohitsuratekar.NCBSinfo.common.utilities.Utilities;
import com.rohitsuratekar.NCBSinfo.database.ConferenceData;
import com.rohitsuratekar.NCBSinfo.database.models.ConferenceModel;
import com.rohitsuratekar.NCBSinfo.interfaces.UserInformation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 01-07-16.
 */
public class CAMPPastevents extends Fragment implements UserInformation {
    RecyclerView recyclerView;
    List<ConferenceModel> fullList;
    CAMPadapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.common_fragment, container, false); //Just need one recycler view. Can be taken from any fragment
        recyclerView = (RecyclerView) rootView.findViewById(R.id.common_recycleview);
        fullList = new ConferenceData(getContext()).getAll();
        List<ConferenceModel> filteredList = new ArrayList<>();
        for (ConferenceModel item : fullList){
            if(item.getCode().equals(registration.camp16.events.EXTERNAL_CONSTANT)){
                filteredList.add(item);
            }
        }

        Collections.sort(fullList, new Comparator<ConferenceModel>() {
            @Override
            public int compare(ConferenceModel lhs, ConferenceModel rhs) {
                Date entry1 = new Utilities().convertToTalkDate(lhs.getEventDate(), lhs.getEventStartTime());
                Date entry2 = new Utilities().convertToTalkDate(rhs.getEventDate(), rhs.getEventStartTime());
                return entry1.compareTo(entry2);
            }
        });

        List<ConferenceModel> refinedList = new ArrayList<>();

        for (ConferenceModel c : fullList){

            Date targetDate =  new Utilities().convertToTalkDate(c.getEventDate(), c.getEventStartTime());
            if(targetDate.before(new Date())){
                refinedList.add(c);
            }
        }

        Collections.reverse(refinedList);

        adapter = new CAMPadapter(refinedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(rootView.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootView;
    }
}