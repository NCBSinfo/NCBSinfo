package com.rohitsuratekar.NCBSinfo.activities.transport;

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
 * Created by Rohit Suratekar on 15-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class TransportOptionAdapter extends RecyclerView.Adapter<TransportOptionAdapter.OptionHolder> {

    private List<TransportOption> optionList;
    private OnOptionSelect select;

    TransportOptionAdapter(List<TransportOption> optionList) {
        this.optionList = optionList;
    }

    @Override
    public OptionHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new OptionHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.transport_option_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(OptionHolder optionHolder, int i) {
        final TransportOption option = optionList.get(i);
        optionHolder.icon.setImageResource(option.getIcon());
        optionHolder.text.setText(option.getText());
        optionHolder.icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select.select(option.getAction());
            }
        });
    }

    @Override
    public int getItemCount() {
        return optionList.size();
    }

    class OptionHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView text;

        OptionHolder(View itemView) {
            super(itemView);
            icon = ButterKnife.findById(itemView, R.id.transport_option_icon);
            text = ButterKnife.findById(itemView, R.id.transport_option_text);
        }
    }

    void setOnOptionSelect(OnOptionSelect s) {
        select = s;
    }

    interface OnOptionSelect {
        void select(int action);
    }
}
