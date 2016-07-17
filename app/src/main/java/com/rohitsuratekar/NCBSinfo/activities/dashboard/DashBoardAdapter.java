package com.rohitsuratekar.NCBSinfo.activities.dashboard;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.AppConstants;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;

import java.util.List;

/**
 * NCBSinfo © 2016, Secret Biology
 * https://github.com/NCBSinfo/NCBSinfo
 * Created by Rohit Suratekar on 16-07-16.
 */
public class DashBoardAdapter extends RecyclerView.Adapter<DashBoardViewHolder> {

    private List<DashBoardModel> DataList;
    private Context context;
    private Preferences pref;
    public static DashBoardAdapter.ItemClickListener ItemClickListener;

    public DashBoardAdapter(List<DashBoardModel> dataList) {
        DataList = dataList;
    }

    public void setOnItemClickListener(ItemClickListener myClickListener) {
        ItemClickListener = myClickListener;
    }

    @Override
    public DashBoardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dashboard_item, parent, false);
        context = itemView.getContext();
        pref = new Preferences(context);
        return new DashBoardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DashBoardViewHolder holder, int position) {

        DashBoardModel data = DataList.get(position);
        holder.fieldTitle.setText(data.getFieldName());
        holder.icon.setImageResource(data.getIcon());

        if (data.isEditable()) {
            holder.editLayout.setVisibility(View.VISIBLE);
        } else {
            holder.editLayout.setVisibility(View.INVISIBLE);
        }

        if (pref.user().getUserType().equals(AppConstants.userType.OLD_USER)
                && !DataList.get(position).getFieldName().equals(context.getString(R.string.dashboard_password))) {
            holder.fieldValue.setText("---");
            holder.editLayout.setVisibility(View.INVISIBLE);
            holder.icon.setAlpha((float) 0.6);
        } else {
            holder.fieldValue.setText(data.getFieldValue());
        }

        if (data.fieldName.equals(context.getString(R.string.dashboard_email))) {
            holder.fieldValue.setText(data.getFieldValue());
            holder.fieldTitle.setAlpha((float) 0.6);
            holder.fieldValue.setAlpha((float) 0.6);
            holder.icon.setAlpha((float) 0.6);
        }


    }

    @Override
    public int getItemCount() {
        return DataList.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position, View v);
    }
}
