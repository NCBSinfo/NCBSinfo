package com.rohitsuratekar.NCBSinfo.activities.edit;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 21-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class StepperAdapter extends RecyclerView.Adapter<StepperAdapter.StepperHolder> {

    private List<StepperModel> itemList;

    StepperAdapter(List<StepperModel> itemList) {
        this.itemList = itemList;
    }

    @Override
    public StepperHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new StepperHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.steper_item, parent, false));
    }

    @Override
    public void onBindViewHolder(StepperHolder holder, int position) {
        Context context = holder.itemView.getContext();
        StepperModel model = itemList.get(position);
        holder.image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        holder.image.setImageResource(R.drawable.icon_circle);
        holder.text.setTextColor(ContextCompat.getColor(context, R.color.primaryText));
        holder.text.setTypeface(Typeface.DEFAULT);


        holder.number.setText(String.valueOf(position + 1));
        holder.text.setText(model.getTitle());

        holder.image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimaryLight));
        holder.left.setVisibility(View.VISIBLE);
        holder.right.setVisibility(View.VISIBLE);
        if (position == 0) {
            holder.left.setVisibility(View.INVISIBLE);

        } else if (position == itemList.size() - 1) {
            holder.right.setVisibility(View.INVISIBLE);
        }

        switch (model.getState()) {
            case StepperModel.STATE_SELECTED:
                holder.image.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
                break;
            case StepperModel.STATE_COMPLETED:
                holder.image.setColorFilter(ContextCompat.getColor(context, R.color.green));
                holder.image.setImageResource(R.drawable.icon_circle_done);
                holder.number.setText("");
                holder.text.setTextColor(ContextCompat.getColor(context, R.color.green));
                break;
            case StepperModel.STATE_INCOMPLETE:
                holder.image.setColorFilter(ContextCompat.getColor(context, R.color.red));
                holder.image.setImageResource(R.drawable.icon_circle_error);
                holder.number.setText("");
                holder.text.setTextColor(ContextCompat.getColor(context, R.color.red));
                break;
        }

        if (model.isSelected()) {
            holder.text.setTypeface(Typeface.DEFAULT_BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    class StepperHolder extends RecyclerView.ViewHolder {

        ImageView left, right, image;
        TextView number, text;

        StepperHolder(View itemView) {
            super(itemView);

            image = ButterKnife.findById(itemView, R.id.stepper_image);
            left = ButterKnife.findById(itemView, R.id.stepper_left_line);
            right = ButterKnife.findById(itemView, R.id.stepper_right_line);
            number = ButterKnife.findById(itemView, R.id.stepper_number);
            text = ButterKnife.findById(itemView, R.id.stepper_text);

        }
    }
}
