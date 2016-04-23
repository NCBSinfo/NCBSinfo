package com.rohitsuratekar.NCBSinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

public class ExperimentalGrid extends BaseAdapter {

    private Context mContext;
    private  String[] mGridItems;
    private int[] mImageIDs;

    public ExperimentalGrid(Context context, String[] GridItems, int[] ImageIDs){
        mContext = context;
        mGridItems = GridItems;
        mImageIDs = ImageIDs;
    }

    @Override
    public int getCount() {
        return mGridItems.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View grid;
        LayoutInflater inflater = (LayoutInflater.from(mContext));

        if (convertView==null){
            grid = new View(mContext);
            grid = inflater.inflate(R.layout.experimental_grid, parent, false);
            ImageView img = (ImageView)grid.findViewById(R.id.experimental_icon);
            TextView txt = (TextView)grid.findViewById(R.id.experimental_caption);
            img.setImageResource(mImageIDs[position]);
            txt.setText(mGridItems[position]);

        }
        else
        {
            grid = (View) convertView;
        }
        return grid;
    }

}