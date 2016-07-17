package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 15-07-16.
 */
public class DashBoardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView fieldTitle, fieldValue;
    public ImageView icon, editButton;
    public LinearLayout editLayout;


    public DashBoardViewHolder(View itemView) {
        super(itemView);
        fieldTitle = (TextView) itemView.findViewById(R.id.dashboard_item_field);
        fieldValue = (TextView) itemView.findViewById(R.id.dashboard_item_value);
        icon = (ImageView) itemView.findViewById(R.id.dashboard_item_icon);
        editButton = (ImageView) itemView.findViewById(R.id.dashboard_item_edit_icon);
        editLayout = (LinearLayout) itemView.findViewById(R.id.dashboard_edit_layout);
        editLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        DashBoardAdapter.ItemClickListener.onItemClick(getLayoutPosition(), view);
    }

}
