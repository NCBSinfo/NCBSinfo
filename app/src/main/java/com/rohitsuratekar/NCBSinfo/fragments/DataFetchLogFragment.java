package com.rohitsuratekar.NCBSinfo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activity.EventDetails;
import com.rohitsuratekar.NCBSinfo.adapters.EventLogAdapter;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.helpers.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.models.CommonEventModel;
import com.rohitsuratekar.NCBSinfo.models.DataModel;
import com.rohitsuratekar.NCBSinfo.models.TalkModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by Rohit Suratekar on 12-04-16.
 */
public class DataFetchLogFragment  extends Fragment {
    RecyclerView recyclerView;
    List<DataModel> entrylist = new ArrayList<>();
    List<TalkModel> Talklist = new ArrayList<>();
    List<CommonEventModel> commonList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_update_log, container, false);
        Database db = new Database(rootView.getContext());

        recyclerView = (RecyclerView) rootView.findViewById(R.id.event_log_recycleview);
        entrylist = db.getFullDatabase();
        Talklist = db.getTalkDatabase();
        commonList = new GeneralHelp().makeCommonList(entrylist,Talklist);
        Collections.sort(commonList, new Comparator<CommonEventModel>(){
            @Override
            public int compare(CommonEventModel lhs, CommonEventModel rhs) {
                Date entry1 = new Date(new GeneralHelp().getMiliseconds(lhs.getTimestamp()));
                Date entry2 = new Date(new GeneralHelp().getMiliseconds(rhs.getTimestamp()));
                return entry1.compareTo(entry2);
            }
        });
        EventLogAdapter log_adapter = new EventLogAdapter(commonList);
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
                intent.putExtra(General.GEN_EVENTDETAILS_DATACODE,commonList.get(position).getDatacode());
                intent.putExtra(General.GEN_EVENTDETAILS_DATA_ID,commonList.get(position).getDataID());
                startActivity(intent);

            }
        });

        return rootView;
    }

}