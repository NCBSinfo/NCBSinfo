package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.HashMap;
import java.util.List;


public class TripEditAdapter extends RecyclerView.Adapter<TripEditAdapter.MyViewHolder> {

    public TripEditAdapter(List<String> items) {
        this.entryList = items;
        for (String s : items) {
            viewMap.put(s, false);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transport_edit, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String entry = entryList.get(position);
        holder.delete.setVisibility(View.INVISIBLE);
        holder.edit.setVisibility(View.INVISIBLE);
        holder.mainLayout.setBackgroundColor(Color.TRANSPARENT);
        if (viewMap.get(entry)) {
            holder.delete.setVisibility(View.VISIBLE);
            holder.edit.setVisibility(View.VISIBLE);
            holder.mainLayout.setBackgroundColor(General.getColor(context, R.color.colorPrimaryLight));
        }
        holder.detail.setText(entry);

        if (position == 0) {
            holder.top.setVisibility(View.INVISIBLE);
        } else {
            holder.top.setVisibility(View.VISIBLE);
        }
        if (position == entryList.size() - 1) {
            holder.bottom.setVisibility(View.INVISIBLE);
        } else {
            holder.bottom.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<String> entryList;
    View currentview;

    private ClickListener myClickListener;
    private deletItem deletItem;
    private editItem editItem;
    HashMap<String, Boolean> viewMap = new HashMap<>();
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView detail;
        public ImageView delete, edit, top, bottom;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
            detail = (TextView) view.findViewById(R.id.ad_list_text);
            delete = (ImageView) view.findViewById(R.id.ad_list_delete);
            edit = (ImageView) view.findViewById(R.id.ad_list_edit);
            top = (ImageView) view.findViewById(R.id.ad_list_top);
            bottom = (ImageView) view.findViewById(R.id.ad_list_bottom);

            mainLayout = (ConstraintLayout) view.findViewById(R.id.ad_list_item);
            context = view.getContext();
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getLayoutPosition());
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deletItem.onItemClick(getLayoutPosition());
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editItem.onItemClick(getLayoutPosition());
                }
            });


        }
    }


    public void showOptions(int position) {

        for (int i = 0; i < entryList.size(); i++) {
            if (i == position) {
                viewMap.put(entryList.get(i), !viewMap.get(entryList.get(i)));
            } else {
                viewMap.put(entryList.get(i), false);
            }
        }
    }

    public void addItem(String item) {
        viewMap.put(item, false);
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void deletItem(deletItem item) {
        this.deletItem = item;
    }

    public void editItem(editItem item) {
        this.editItem = item;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }

    public interface deletItem {
        void onItemClick(int position);
    }

    public interface editItem {
        void onItemClick(int position);
    }
}
