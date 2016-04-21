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

import java.util.List;

public class EventLogAdapter extends RecyclerView.Adapter<EventLogAdapter.MyViewHolder> {

    private List<CommonEventModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, message;
        public LinearLayout layout1;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            title = (TextView) view.findViewById(R.id.event_log_title);
            message = (TextView) view.findViewById(R.id.event_log_details);
            layout1 = (LinearLayout)view.findViewById(R.id.event_log_layout);
            image = (ImageView)view.findViewById(R.id.event_log_icon);
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
        holder.image.setColorFilter(R.color.colorPrimary);
        holder.image.setAlpha((float) 0.7);

        if(entry.getDatacode().equals(General.GEN_DATACODE_TALK)){
            holder.image.setBackgroundResource(R.drawable.icon_feed);
        }
        else if(entry.getDatacode().equals(General.GEN_DATACODE_CBJC)){
            holder.image.setBackgroundResource(R.drawable.icon_jc);
        }
        else { holder.image.setBackgroundResource(R.drawable.icon_event); }



    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        public void onItemClick(int position, View v);


    }


}