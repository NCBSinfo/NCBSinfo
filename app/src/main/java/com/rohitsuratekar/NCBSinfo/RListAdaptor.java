package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


/**
 * Created by Dexter on 1/17/2016.
 */
public class RListAdaptor extends ArrayAdapter<String> {

    Context context;
    int layoutResourceId;
    String[] data = null;

    public RListAdaptor(Context context, int layoutResourceId, String[] data2) {
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