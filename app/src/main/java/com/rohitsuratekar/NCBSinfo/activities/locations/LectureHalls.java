package com.rohitsuratekar.NCBSinfo.activities.locations;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.BaseActivity;
import com.rohitsuratekar.NCBSinfo.ui.CurrentActivity;

import java.util.ArrayList;


//TODO: convert array to list based model

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class LectureHalls extends BaseActivity {

    public static final String INTENT = "lecture_hall";

    ExpandableListView expListView;
    private int lastExpandedPosition = -1;
    LectureHallAdapter mLectureAdapter;
    ArrayList<String[]> temparray = new LectureHallsList().listHalls();

    @Override
    protected CurrentActivity setCurrentActivity() {
        return CurrentActivity.LECTUREHALLS;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        expListView = (ExpandableListView) findViewById(R.id.lecturehall_expandableList);

        setGroupData();
        //setChildGroupData();
        mLectureAdapter = new LectureHallAdapter(groupItem, childItem);
        mLectureAdapter
                .setInflater(
                        (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                        this);

        // setting list adapter
        expListView.setAdapter(mLectureAdapter);
        registerForContextMenu(expListView);
        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {


            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    expListView.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }
        });

        /**
         * This will 'try' to select location based on venue received from intent
         */

        Intent intent = getIntent();
        String incomingIntent = intent.getStringExtra(INTENT);
        if (incomingIntent != null) {
            int selectIndex = 1989;
            for (int i = 0; i < temparray.size(); i++) {
                if (incomingIntent.trim().toUpperCase().replace("-", "").contains(temparray.get(i)[0].replace("-", "").toUpperCase()) ||
                        incomingIntent.toUpperCase().replace("-", "").contains(temparray.get(i)[1].replace("-", "").toUpperCase())) {
                    selectIndex = i;
                }
            }
            if (selectIndex != 1989) {
                expListView.setSelection(selectIndex);
                expListView.expandGroup(selectIndex);
            }
        }

    }

    ArrayList<String> groupItem = new ArrayList<String>();
    ArrayList<Object> childItem = new ArrayList<Object>();

    public void setGroupData() {

        groupItem.clear();
        ArrayList<String> child;

        for (int i = 0; i < temparray.size(); i++) {
            child = new ArrayList<String>();
            groupItem.add("<b>" + temparray.get(i)[0] + "</b><small> (" + temparray.get(i)[1] + ")</small>");
            child.add(temparray.get(i)[3] + " , " + temparray.get(i)[2]);
            child.add("<small>" + temparray.get(i)[4] + "</small>");
            childItem.add(child);
        }

    }

}
