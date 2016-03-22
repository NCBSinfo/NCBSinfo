package com.rohitsuratekar.NCBSinfo.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * Created by Dexter on 3/15/2016.
 */
public class adapters_shuttle_list extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    String[] data = null;

    public adapters_shuttle_list(Context context, int layoutResourceId, String[] data2) {
        super(context, layoutResourceId, data2);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        data = data2;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = convertView;

        if (null == convertView) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

        } else {
            row = convertView;
        }

        TextView tv = (TextView) row.findViewById(R.id.txtTitle);
        tv.setText(Html.fromHtml(getItem(position)));

        return row;
    }

}