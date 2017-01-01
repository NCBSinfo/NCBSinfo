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


public class TripAdapter extends RecyclerView.Adapter<TripAdapter.MyViewHolder> {

    private List<String> entryList;
    private int currentItem;
    View currentview;
    private ClickListener myClickListener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView details;
        public ImageView dot, top, bottom;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
            details = (TextView) view.findViewById(R.id.tp_list_text);
            dot = (ImageView) view.findViewById(R.id.tp_list_dot);
            top = (ImageView) view.findViewById(R.id.tp_list_top_line);
            bottom = (ImageView) view.findViewById(R.id.tp_list_bottom_line);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.tp_list_layout);
            context = view.getContext();


        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public TripAdapter(List<String> entryList, int currentItem) {
        this.entryList = entryList;
        this.currentItem = currentItem;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transport_list, parent, false);

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
        }
        if (position == entryList.size() - 1) {
            holder.bottom.setVisibility(View.INVISIBLE);
        }

        //Local

        if (position < currentItem) {
            holder.dot.setColorFilter(General.getColor(context, scheme.getSecondaryText()));
            holder.details.setTextColor(General.getColor(context, scheme.getSecondaryText()));
            holder.dot.setImageResource(R.drawable.icon_dot);
            holder.dot.setAlpha((float) 0.6);
            holder.details.setAlpha((float) 0.6);
        } else if (position == currentItem) {
            holder.dot.setColorFilter(General.getColor(context, scheme.getAccent()));
            holder.details.setTextColor(General.getColor(context, scheme.getPrimaryText()));
            holder.dot.setImageResource(R.drawable.icon_gps_on);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in_out);
            holder.dot.startAnimation(animation);
            holder.dot.setAlpha((float) 1);
            holder.details.setAlpha((float) 1);
        } else {
            holder.dot.setColorFilter(General.getColor(context, scheme.getSecondaryText()));
            holder.details.setTextColor(General.getColor(context, scheme.getSecondaryText()));
            holder.dot.setImageResource(R.drawable.icon_gps_off);
            holder.dot.setAlpha((float) 1);
            holder.details.setAlpha((float) 1);
        }


    }

    public void setCurrentItem(int item) {
        this.currentItem = item;
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

