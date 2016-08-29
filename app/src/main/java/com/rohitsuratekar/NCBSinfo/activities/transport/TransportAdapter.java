package com.rohitsuratekar.NCBSinfo.activities.transport;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.constants.DateFormats;
import com.rohitsuratekar.NCBSinfo.preferences.Preferences;
import com.rohitsuratekar.NCBSinfo.utilities.DateConverters;
import com.secretbiology.helpers.general.General;

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
        Preferences pref = new Preferences(row.getContext());
        TextView item = (TextView) row.findViewById(R.id.transport_item_text);
        item.setText(Html.fromHtml(getItem(position)));

        item.setBackgroundColor(Color.TRANSPARENT);
        if (type == Routes.type.BUGGY) {
            if (isWeek) {
                String[] buggy2 = convertList(pref.transport().getNCBSBuggy2());
                for (String s : buggy2) {
                    if (getItem(position).contains(s)) {
                        item.setBackgroundColor(General.getColor(row.getContext(), R.color.buggy_background));
                    }
                }
            } else {
                String[] buggy2 = convertList(pref.transport().getMandaraBuggy2());
                for (String s : buggy2) {
                    if (getItem(position).contains(s)) {
                        item.setBackgroundColor(General.getColor(row.getContext(), R.color.buggy_background));
                    }
                }
            }
        }

        return row;
    }

    private String[] convertList(String[] list) {
        for (int i = 0; i < list.length; i++) {
            list[i] = new DateConverters().convertFormat(list[i], DateFormats.TIME_12_HOURS_STANDARD);
        }
        return list;
    }


}

