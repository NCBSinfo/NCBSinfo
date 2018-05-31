package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.Helper;

import java.text.ParseException;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 15-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ETSimpleAdapter extends RecyclerView.Adapter<ETSimpleAdapter.ItemHolder> {

    private List<String> itemList;
    private OnSelect onSelect;
    private boolean isFinalList;

    ETSimpleAdapter(List<String> itemList) {
        this.itemList = itemList;
        isFinalList = false;
    }

    ETSimpleAdapter(List<String> itemList, boolean isFinalList) {
        this.itemList = itemList;
        this.isFinalList = isFinalList;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_transport_simple_row, parent, false));
    }

    @Override
    public void onBindViewHolder(final ItemHolder holder, int position) {
        try {
            holder.text.setText(Helper.displayTime(itemList.get(position)));
        } catch (ParseException e) {
            holder.text.setText("--:--");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelect.selected(holder.getAdapterPosition());
            }
        });
        if (position == 0 && !isFinalList) {
            holder.text.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    void setOnSelect(OnSelect s) {
        onSelect = s;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView text;

        ItemHolder(View itemView) {
            super(itemView);
            text = ButterKnife.findById(itemView, R.id.et_trips_simple_row_text);
        }
    }

    interface OnSelect {
        void selected(int position);
    }
}
