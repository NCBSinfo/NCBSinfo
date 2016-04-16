package com.rohitsuratekar.NCBSinfo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.StatusCodes;
import com.rohitsuratekar.NCBSinfo.models.LogModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DevelopersLogAdapter extends RecyclerView.Adapter<DevelopersLogAdapter.MyViewHolder> {

    private List<LogModel> entryList;
    View currentview;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, message, timestamp, status;
        public LinearLayout layout1;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            title = (TextView) view.findViewById(R.id.log_message);
            message = (TextView) view.findViewById(R.id.log_details);
            timestamp = (TextView) view.findViewById(R.id.log_timestamp);
            status = (TextView) view.findViewById(R.id.log_status);
            layout1 = (LinearLayout)view.findViewById(R.id.fetch_logLayout);

        }
    }


    public DevelopersLogAdapter(List<LogModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.developers_log_item, parent, false);

        context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        LogModel entry = entryList.get(position);
        holder.status.setText(String.valueOf(entry.getStatus()));
        holder.title.setText(entry.getMessage());
        holder.message.setText(entry.getDetails());
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a d MMM yy", Locale.getDefault());
        SimpleDateFormat useformat = new SimpleDateFormat("d MMM yy hh:mm a", Locale.getDefault());
        try {
            Date now = formatter.parse(entry.getTimestamp());
            holder.timestamp.setText(useformat.format(now));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(entry.getStatus()== StatusCodes.TYPE_SUCCESSFUL){
            holder.status.setText("Successful");
            holder.status.setTextColor(Color.GREEN);
        }
        else
        {holder.status.setTextColor(Color.RED);}
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }
}
