package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 03-07-16.
 */
public class PinnedContactAdapter extends RecyclerView.Adapter<PinnedContactAdapter.MyViewHolder> {

    private List<ContactRowModel> entryList;
    View currentview;
    private Context context;
    private ClickListener myClickListener;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, email, phone, firstLetter;
        public LinearLayout layout1;
        public LinearLayout icon;

        public MyViewHolder(View view) {
            super(view);
            currentview = view;
            name = (TextView) view.findViewById(R.id.pinned_item_title);
            email = (TextView) view.findViewById(R.id.pinned_item_email);
            phone = (TextView) view.findViewById(R.id.pinned_item_number);
            firstLetter = (TextView) view.findViewById(R.id.pinned_contact_letter);
            layout1 = (LinearLayout) view.findViewById(R.id.pinned_layout);
            icon = (LinearLayout) view.findViewById(R.id.pinned_thumbnail);
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


    public PinnedContactAdapter(List<ContactRowModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_pinned, parent, false);
        this.context = parent.getContext();

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContactRowModel entry = entryList.get(position);
        holder.name.setText(entry.getName());
        holder.email.setText(entry.getDepartment());
        holder.phone.setText(entry.getNuber());

        String firstLetter = entry.getName().substring(0, 1).toUpperCase();
        holder.icon.setBackgroundColor(new ContactColors(context, firstLetter.toLowerCase()).colorID);
        holder.firstLetter.setText(firstLetter.toUpperCase());

    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);


    }


}