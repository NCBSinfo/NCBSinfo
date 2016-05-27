package com.rohitsuratekar.NCBSinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.General;
import com.rohitsuratekar.NCBSinfo.models.CommonEventModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventLogAdapter extends RecyclerView.Adapter<EventLogAdapter.MyViewHolder> {

    private List<CommonEventModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, message, event_date, event_month;
        public LinearLayout layout1;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            title = (TextView) view.findViewById(R.id.event_log_title);
            message = (TextView) view.findViewById(R.id.event_log_details);
            layout1 = (LinearLayout)view.findViewById(R.id.event_log_layout);
            event_date = (TextView)view.findViewById(R.id.event_log_date);
            event_month = (TextView)view.findViewById(R.id.event_log_month);
            layout1.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public EventLogAdapter(List<CommonEventModel> entryList) {
        this.entryList = entryList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_updates_log_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        CommonEventModel entry = entryList.get(position);
        holder.title.setText(entry.getNotificationTitle());
        holder.message.setText(entry.getCommonItem1() );
        if(entry.getDatacode().equals(General.GEN_DATACODE_TALK)){
            holder.message.setText(entry.getCommonItem2() );
        }
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.ENGLISH);
        Date eventDateTime = Calendar.getInstance().getTime();
        try {
            eventDateTime = eventFormat.parse(entry.getDate()+" "+entry.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar showCal = Calendar.getInstance();
        showCal.setTime(eventDateTime);
        holder.event_date.setText(new SimpleDateFormat("dd", Locale.ENGLISH).format(showCal.getTime()));
        holder.event_month.setText(new SimpleDateFormat("MMM", Locale.ENGLISH).format(showCal.getTime()).toUpperCase());
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }


}