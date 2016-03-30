package com.rohitsuratekar.NCBSinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * Created by Dexter on 21-03-16.
 */
public class adapeters_home_GoSpinner extends BaseAdapter {

    LayoutInflater inflator;
    String[] mCounting;

    public adapeters_home_GoSpinner( Context context ,String[] counting)
    {
        inflator = LayoutInflater.from(context);
        mCounting=counting;
    }

    @Override
    public int getCount() {
        return mCounting.length;
    }

    @Override
    public Object getItem(int position) {
        return mCounting[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
        convertView = inflator.inflate(R.layout.home_sh_gospinner_layout, parent, false);
        TextView tv = (TextView) convertView.findViewById(R.id.home_sh_gospinner_text);
        tv.setText(mCounting[position]);}
        return convertView;

    }
}
