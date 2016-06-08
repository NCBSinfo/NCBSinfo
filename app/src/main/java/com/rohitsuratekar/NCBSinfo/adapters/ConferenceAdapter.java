package com.rohitsuratekar.NCBSinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.ExternalConstants;
import com.rohitsuratekar.NCBSinfo.models.ConferenceModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ConferenceAdapter extends RecyclerView.Adapter<ConferenceAdapter.MyViewHolder> {

    private List<ConferenceModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, speaker, date, time, venue;
        public LinearLayout fullLayout;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            title = (TextView) view.findViewById(R.id.conference_eventTitle);
            speaker = (TextView) view.findViewById(R.id.conference_speakerName);
            date = (TextView) view.findViewById(R.id.conference_date);
            time = (TextView) view.findViewById(R.id.conference_Time);
            venue = (TextView) view.findViewById(R.id.conference_venue);
            fullLayout = (LinearLayout)view.findViewById(R.id.conference_Layout);


        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public ConferenceAdapter(List<ConferenceModel> entryList) {
        this.entryList = entryList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.conference_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ConferenceModel entry = entryList.get(position);
        holder.title.setText(entry.getEventTitle());
        holder.speaker.setText("Speaker: "+entry.getEventSpeaker());
        holder.date.setText(convertDate(entry.getEventDate()));
        holder.venue.setText("Venue: "+entry.getEventVenue());
        holder.time.setText(convertTime(entry.getEventStartTime(), entry.getEventEndTime()));
        if(entry.getEventCode()!=null){
            if(entry.getEventCode().equals(ExternalConstants.CAMP2016_ACTIVITY)) {
                holder.speaker.setVisibility(View.GONE);
            }
            if(entry.getEventCode().equals(ExternalConstants.CAMP2016_TUTORIAL)){
                holder.speaker.setText("Tutor: "+entry.getEventSpeaker());
            }
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }

    private String convertDate(String oldFormat){
        String returnString;
        Date returnDate=new Date();
        DateFormat eventFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            returnDate = eventFormat.parse(oldFormat);
            returnString = new SimpleDateFormat("dd MMM", Locale.getDefault()).format(returnDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
            returnString = "N/A";
        }
        return returnString.toUpperCase();
    }

    private String convertTime(String start, String end){
        String returnStart, returnEnd;
        Date startdate=new Date();
        Date enddate=new Date();
        DateFormat eventFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
        try {
            startdate = eventFormat.parse(start);
            returnStart = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(startdate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
            returnStart = "N/A";
        }

        try {
            enddate = eventFormat.parse(end);
            returnEnd = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("FAILED", "PARSING");
            returnEnd = "N/A";
        }

        return returnStart+" to "+returnEnd;
    }


}
