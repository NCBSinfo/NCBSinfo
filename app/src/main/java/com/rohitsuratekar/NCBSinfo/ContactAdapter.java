package com.rohitsuratekar.NCBSinfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.bignerdranch.expandablerecyclerview.ViewHolder.ChildViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.DataObjectHolder> implements Filterable {

    private ArrayList<DataObject> mDataset;
    private MyClickListener myClickListener;
    private MyClickListener2 myClickListener2;
    ValueFilter valueFilter;
    private List<DataObject> modelItems;
    private Context context;


    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }


    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnCreateContextMenuListener {
        TextView label;
        TextView dateTime;
        ImageButton favButton;


        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.item_title);
            dateTime = (TextView) itemView.findViewById(R.id.item_number);
            favButton = (ImageButton) itemView.findViewById(R.id.fav_button);
            itemView.setOnCreateContextMenuListener(this);

            itemView.setOnClickListener(this);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener2.onItemClick(getLayoutPosition(), v);

                }
            });
        }


        @Override
        public void onClick(View v) {

            myClickListener.onItemClick(getLayoutPosition(), v);


        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.contact_list_call,Menu.NONE,"Call");
            menu.add(Menu.NONE, R.id.contact_list_fav, Menu.NONE, " Add to Favorite");
            menu.add(Menu.NONE, R.id.contact_list_delete, Menu.NONE, "Delete");
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setOnItemClickListener2(MyClickListener2 myClickListener2) {
        this.myClickListener2 = myClickListener2;
    }

    public ContactAdapter(ArrayList<DataObject> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_view, parent, false);

        context= parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }



    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.label.setText(mDataset.get(position).getmText1());
        holder.dateTime.setText(mDataset.get(position).getmText2());

        String firstLetter =  holder.label.getText().toString().substring(0,1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color1);
        ImageView image = (ImageView) holder.itemView.findViewById(R.id.thumbnail);
        image.setImageDrawable(drawable);

        DBHandler db2 = new DBHandler(context);
        final int k = mDataset.get(position).getmID();
        ImageButton img = (ImageButton) holder.itemView.findViewById(R.id.fav_button);
        img.setColorFilter(null);
        if (db2.getDocument(k).getContactFavorite().equals("1")){
            img.setColorFilter(Color.RED);

        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(k);
                return false;
            }
        });
    }
    public void addItem(DataObject dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }
    public void clearAllitems()
    {
        mDataset.clear();
        this.notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);


    }

    public interface MyClickListener2 {
        public void onItemClick(int position, View v);


    }

    private long position;

    public long getPosition() {
        return position;
    }

    public void setPosition(long position) {
        this.position = position;
    }




    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<DataObject> filterList = new ArrayList<DataObject>();
                for (int i = 0; i < mDataset.size(); i++)
                    if ((mDataset.get(i).getmText1().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {


                        DataObject m = new DataObject(mDataset.get(i).getmText1(), mDataset.get(i).getmText2(), mDataset.get(i).getmID());

                        filterList.add(m);
                    }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = mDataset.size();
                results.values = mDataset;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            modelItems = (List<DataObject>) results.values;
            notifyDataSetChanged();
        }

    }




}