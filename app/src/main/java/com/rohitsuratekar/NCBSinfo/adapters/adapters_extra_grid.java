package com.rohitsuratekar.NCBSinfo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

/**
 * Created by Dexter on 20-03-16.
 */
public class adapters_extra_grid extends BaseAdapter {

    private Context mContext;
    private  String[] mGridItems;
    private int[] mImageIDs;

    public adapters_extra_grid(Context context, String[] GridItems, int[] ImageIDs){
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
            grid = inflater.inflate(R.layout.activity_extra_grid_list, parent, false);
            ImageView img = (ImageView)grid.findViewById(R.id.otherinfo_icon);
            TextView txt = (TextView)grid.findViewById(R.id.otherinfo_icon_caption);
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