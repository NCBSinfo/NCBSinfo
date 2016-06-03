package com.rohitsuratekar.NCBSinfo.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.helpers.GeneralHelp;
import com.rohitsuratekar.NCBSinfo.models.ExternalModel;

import java.util.List;

public class ExternalAdapter extends RecyclerView.Adapter<ExternalAdapter.MyViewHolder> {

    private List<ExternalModel> entryList;
    View currentview;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, message, type, timestamp;
        public LinearLayout icon_holder, fullLayout;
        public ImageView icon;

        public MyViewHolder(View view) {
            super(view);
            currentview=view;
            title = (TextView) view.findViewById(R.id.external_title);
            message = (TextView) view.findViewById(R.id.external_details);
            icon_holder = (LinearLayout)view.findViewById(R.id.external_holder);
            fullLayout = (LinearLayout)view.findViewById(R.id.external_layout);
            type = (TextView)view.findViewById(R.id.external_type);
            timestamp = (TextView)view.findViewById(R.id.external_timestamp);
            icon = (ImageView)view.findViewById(R.id.external_icon);
            //fullLayout.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getLayoutPosition(), v);
        }
    }
    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }



    public ExternalAdapter(List<ExternalModel> entryList) {
        this.entryList = entryList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.external_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ExternalModel entry = entryList.get(position);
        holder.title.setText(entry.getTitle());
        holder.message.setText(entry.getMessage() );
        holder.timestamp.setText(new GeneralHelp().makeReadableTime(new GeneralHelp().reverseTimestamp(entry.getTimestamp())));
        holder.type.setText("INFO");
        holder.icon.setImageResource(R.drawable.icon_information);
        holder.icon_holder.setBackgroundColor(currentview.getResources().getColor(R.color.general_message));
        if(entry.getExtra().equals("IMP")){
            holder.icon_holder.setBackgroundColor(currentview.getResources().getColor(R.color.imp_message));
            holder.icon.setImageResource(R.drawable.icon_star);
            holder.type.setText("IMP");
        }
        else if (entry.getExtra().equals("PERSONAL")){
            holder.icon_holder.setBackgroundColor(currentview.getResources().getColor(R.color.personal_message));
            holder.icon.setImageResource(R.drawable.icon_personal);
            holder.type.setText("YOU");
        }

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }


}
