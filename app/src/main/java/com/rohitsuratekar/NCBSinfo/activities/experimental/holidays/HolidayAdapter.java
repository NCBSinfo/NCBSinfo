package com.rohitsuratekar.NCBSinfo.activities.experimental.holidays;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 06-07-16.
 */
public class HolidayAdapter extends RecyclerView.Adapter<HolidayAdapter.MyViewHolder> {

    private List<HolidayModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, message, event_date, event_month;
        public LinearLayout layout1;

        public MyViewHolder(View view) {
            super(view);
            currentview = view;
            title = (TextView) view.findViewById(R.id.events_title);
            message = (TextView) view.findViewById(R.id.events_details);
            layout1 = (LinearLayout) view.findViewById(R.id.events_layout);
            event_date = (TextView) view.findViewById(R.id.events_date);
            event_month = (TextView) view.findViewById(R.id.events_month);
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


    public HolidayAdapter(List<HolidayModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HolidayModel entry = entryList.get(position);
        holder.title.setText(entry.getTitle());

        Calendar showCal = Calendar.getInstance();
        showCal.setTime(entry.getDate());
        int week = showCal.get(Calendar.WEEK_OF_MONTH);
        String temp = "";
        switch (week){
            case 1: temp = "First "; break;
            case 2: temp = "Second "; break;
            case 3: temp = "Third "; break;
            case 4: temp = "Fourth "; break;
            case 5: temp = "Fifth  "; break;
            case 6: temp = "Sixth "; break;
        }
        holder.message.setText(temp + new SimpleDateFormat("EEEE", Locale.getDefault()).format(entry.getDate()));

        int daysleft = showCal.get(Calendar.DAY_OF_YEAR) - Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
        if( daysleft > 0){
            holder.message.setText(holder.message.getText().toString() + " ( in " + daysleft + " days )");
        }

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
