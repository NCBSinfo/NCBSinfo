package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

public class TransportAdapter extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    String[] data = null;
    Routes.type type;
    boolean isWeek;

    public TransportAdapter(Context context, int layoutResourceId, String[] mData, Routes.type type, boolean isWeek) {
        super(context, layoutResourceId, mData);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.type = type;
        this.isWeek = isWeek;
        data = mData;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (null == convertView) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
        } else {
            row = convertView;
        }
        TextView item = (TextView) row.findViewById(R.id.transport_item_text);
        item.setText(Html.fromHtml(getItem(position)));
        return row;
    }


}

