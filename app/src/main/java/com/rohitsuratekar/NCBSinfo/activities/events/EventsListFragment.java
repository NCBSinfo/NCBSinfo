package com.rohitsuratekar.NCBSinfo.activities.events;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.locations.LectureHalls;
import com.rohitsuratekar.NCBSinfo.database.TalkData;
import com.rohitsuratekar.NCBSinfo.database.models.TalkModel;
import com.rohitsuratekar.NCBSinfo.ui.DividerDecoration;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class EventsListFragment extends Fragment {

    //Public
    public static String BUNDLE = "bundleFragment";

    RecyclerView recyclerView;
    LinearLayout blankLayout;
    List<TalkModel> talkList = new ArrayList<>();
    List<TalkModel> refined_list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.events_fragment, container, false);
        Bundle bundle = this.getArguments();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.events_recycleview);
        blankLayout = (LinearLayout) rootView.findViewById(R.id.event_blank);
        talkList = new TalkData(getContext()).getAll();
        if (talkList.size() > 0) {
            Collections.sort(talkList, new Comparator<TalkModel>() {
                @Override
                public int compare(TalkModel lhs, TalkModel rhs) {
                    Date entry1 = new DateConverters().convertToDate(lhs.getDate() + " " + lhs.getTime());
                    Date entry2 = new DateConverters().convertToDate(rhs.getDate() + " " + rhs.getTime());
                    return entry1.compareTo(entry2);
                }
            });
        }

        if (bundle.getString(BUNDLE) != null) {
            if (bundle.getString(BUNDLE).equals("1")) {
                upcoming();
            } else {
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
                showDialog(getContext(), refined_list.get(position));

            }
        });

        if (refined_list.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            blankLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            blankLayout.setVisibility(View.GONE);

        }

        return rootView;
    }

    public void upcoming() {


        for (TalkModel entry : talkList) {
            Date eventDateTime = new DateConverters().convertToDate(entry.getDate() + " " + entry.getTime());
            Calendar c1 = Calendar.getInstance();
            c1.setTime(eventDateTime);
            if ((eventDateTime.getTime() - Calendar.getInstance().getTime().getTime()) > 0) {
                refined_list.add(entry);
            }
        }
    }

    public void past() {
        for (TalkModel entry : talkList) {
            Date eventDateTime = new DateConverters().convertToDate(entry.getDate() + " " + entry.getTime());
            Calendar c1 = Calendar.getInstance();
            c1.setTime(eventDateTime);

            if ((eventDateTime.getTime() - Calendar.getInstance().getTime().getTime()) <= 0) {
                refined_list.add(entry);
            }
        }
        Collections.reverse(refined_list);
    }

    public void showDialog(final Context context, final TalkModel talk) {
        //Inflate layout
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.events_details_viewer, null);

        TextView title = (TextView) view.findViewById(R.id.eventView_title);
        TextView speaker = (TextView) view.findViewById(R.id.eventView_speaker);
        TextView date = (TextView) view.findViewById(R.id.eventView_date);
        TextView time = (TextView) view.findViewById(R.id.eventView_time);
        TextView venue = (TextView) view.findViewById(R.id.eventView_venue);
        TextView affiliation = (TextView) view.findViewById(R.id.eventView_affiliation);
        TextView host = (TextView) view.findViewById(R.id.eventView_host);
        Button where = (Button) view.findViewById(R.id.eventView_where);

        Date dt = new DateConverters().convertToDate(talk.getDate() + " " + talk.getTime());
        Calendar calendar = new DateConverters().convertToCalendar(talk.getDate() + " " + talk.getTime());

        title.setText(talk.getTitle());

        //Custom title for AWS
        if (talk.getNotificationTitle().matches("(?i).*AWS.*")
                && calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY
                && calendar.get(Calendar.HOUR_OF_DAY) == 16) {
            title.setText(talk.getNotificationTitle());
        }
        speaker.setText(talk.getSpeaker());


        DateFormat currentDate = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        DateFormat currentTime = new SimpleDateFormat("hh:mm aaa", Locale.getDefault());

        date.setText(currentDate.format(dt));
        time.setText(currentTime.format(dt));
        venue.setText(talk.getVenue());
        affiliation.setText(talk.getAffilication());
        host.setText(talk.getHost());
        where.setText("Where is '" + talk.getVenue() + "' ?");


        //Show dialog to add view
        final AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(view)
                .show();

        //To adjust content wrapping of dialog box depending on orientation of screen
        Window window = dialog.getWindow();
        if (getContext() != null) {
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }

        where.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, LectureHalls.class);
                intent.putExtra(LectureHalls.INTENT, talk.getVenue());
                startActivity(intent);
            }
        });
    }


}

