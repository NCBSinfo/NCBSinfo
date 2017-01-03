package com.rohitsuratekar.NCBSinfo.activities.transport.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.ui.ColorScheme;
import com.secretbiology.helpers.general.General;

import java.util.List;


public class SelectFirstAdapter extends RecyclerView.Adapter<SelectFirstAdapter.MyViewHolder> {

    private List<String> entryList;
    View currentview;
    private ClickListener myClickListener;
    private boolean isSelected;
    private int currentItem;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView details, note;
        public ImageView dot, top, bottom, indicator;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
            details = (TextView) view.findViewById(R.id.tp_sf_text);
            note = (TextView) view.findViewById(R.id.tp_sf_indicator);
            dot = (ImageView) view.findViewById(R.id.tp_sf_dot);
            top = (ImageView) view.findViewById(R.id.tp_sf_top);
            bottom = (ImageView) view.findViewById(R.id.tp_sf_bottom);
            indicator = (ImageView) view.findViewById(R.id.tp_sf_indicator_icon);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.tp_select_layout);
            context = view.getContext();
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getLayoutPosition());
                }
            });


        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public SelectFirstAdapter(List<String> entryList, boolean isSelected) {
        this.entryList = entryList;
        this.isSelected = isSelected;
        this.currentItem = entryList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transport_select_first, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        String entry = entryList.get(position);
        holder.details.setText(entry);

        ColorScheme scheme = new ColorScheme(context);

        //Universal
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

        //Local
        if (position == currentItem && isSelected) {
            holder.indicator.setVisibility(View.VISIBLE);
            holder.note.setVisibility(View.VISIBLE);
            holder.dot.setImageResource(R.drawable.icon_gps_on);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_out);
            holder.indicator.startAnimation(animation);
            holder.details.setTextColor(General.getColor(context, scheme.getAccent()));
        } else {
            holder.indicator.setVisibility(View.INVISIBLE);
            holder.note.setVisibility(View.INVISIBLE);
            holder.dot.setImageResource(R.drawable.icon_gps_off);
            holder.details.setTextColor(General.getColor(context, scheme.getSecondaryText()));
        }


    }

    public void itemSelected(int item) {
        if (item >= 0) {
            this.isSelected = true;
            this.currentItem = item;
        } else {
            this.isSelected = false;
        }
    }


    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }


}