package com.rohitsuratekar.NCBSinfo.activities.transport.reminder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.activities.transport.TransportHelper;
import com.rohitsuratekar.NCBSinfo.activities.transport.models.TransportModel;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.database.models.AlarmModel;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 08-07-16.
 */
public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.MyViewHolder> {

    private List<AlarmModel> entryList;
    View currentview;
    Context context;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView routeName, time, date, type;
        public LinearLayout layout1;
        public ImageView delete;

        public MyViewHolder(View view) {
            super(view);
            currentview = view;
            routeName = (TextView) view.findViewById(R.id.reminder_transport_time);
            time = (TextView) view.findViewById(R.id.reminder_time);
            layout1 = (LinearLayout) view.findViewById(R.id.reminder_layout);
            date = (TextView) view.findViewById(R.id.reminder_date);
            type = (TextView) view.findViewById(R.id.reminder_type);
            delete = (ImageView) view.findViewById(R.id.reminder_delete);
            delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }


    public ReminderAdapter(List<AlarmModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transort_reminder_item, parent, false);
        context = itemView.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AlarmModel entry = entryList.get(position);
        TransportModel trasport = new TransportModel(new TransportHelper(context).
                getRoute(Integer.parseInt(entry.getExtraParameter())), context);
        holder.routeName.setText(trasport.getFrom().toUpperCase() + " - " + trasport.getTO().toUpperCase());
        holder.date.setText(entry.getAlarmDate());
        holder.type.setText(trasport.getType());
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.ENGLISH);
        Date eventDateTime = Calendar.getInstance().getTime();
        try {
            eventDateTime = eventFormat.parse(entry.getAlarmDate() + " " + entry.getAlarmTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar showCal = Calendar.getInstance();
        showCal.setTime(eventDateTime);
        holder.time.setText(new DateConverters().convertFormat(entry.getExtraValue(), DateFormats.TIME_12_HOURS_STANDARD));
        holder.date.setText("at " +
                new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(showCal.getTime()) + " on " +
                new SimpleDateFormat("EEEE", Locale.ENGLISH).format(showCal.getTime()));
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }


}