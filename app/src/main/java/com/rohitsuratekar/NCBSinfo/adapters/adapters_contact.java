package com.rohitsuratekar.NCBSinfo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.rohitsuratekar.NCBSinfo.DatabaseHelper;
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.models.models_contacts_row;

import java.util.ArrayList;
import java.util.List;

public class adapters_contact extends RecyclerView.Adapter<adapters_contact.DataObjectHolder> implements Filterable {

    private ArrayList<models_contacts_row> mDataset;
    private MyClickListener myClickListener;
    private MyClickListener2 myClickListener2;
    ValueFilter valueFilter;
    private List<models_contacts_row> modelItems;
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
        TextView ContactName, ContactNumber, ContactDepartment;

        ImageButton favButton;


        public DataObjectHolder(View itemView) {
            super(itemView);
            ContactName = (TextView) itemView.findViewById(R.id.item_title);
            ContactNumber = (TextView) itemView.findViewById(R.id.item_number);
            ContactDepartment = (TextView) itemView.findViewById(R.id.item_department);
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
            menu.add(Menu.NONE, R.id.contact_list_edit, Menu.NONE, "Edit");
            menu.add(Menu.NONE, R.id.contact_list_delete, Menu.NONE, "Delete");
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setOnItemClickListener2(MyClickListener2 myClickListener2) {
        this.myClickListener2 = myClickListener2;
    }

    public adapters_contact(ArrayList<models_contacts_row> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_contact_row_view, parent, false);

        context= parent.getContext();
        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }



    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.ContactName.setText(Html.fromHtml(mDataset.get(position).getmText1()));
        holder.ContactNumber.setText(Html.fromHtml(mDataset.get(position).getmText2()));
        holder.ContactDepartment.setText(Html.fromHtml(mDataset.get(position).getmText3()));

        String firstLetter =  holder.ContactName.getText().toString().substring(0,1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color1);
        ImageView image = (ImageView) holder.itemView.findViewById(R.id.thumbnail);
        image.setImageDrawable(drawable);

        DatabaseHelper db2 = new DatabaseHelper(context);
        final int k = mDataset.get(position).getmID();
        ImageButton img = (ImageButton) holder.itemView.findViewById(R.id.fav_button);
        img.setColorFilter(null);
        if (db2.getContact(k).getContact_favorite().equals("1")){
            img.setColorFilter(Color.RED);

        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(k);
                return false;
            }
        });
        db2.close();
    }
    public void addItem(models_contacts_row dataObj, int index) {
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
            //DBHandler db2 = new DBHandler(context);
            if (constraint != null && constraint.length() > 0) {
                ArrayList<models_contacts_row> filterList = new ArrayList<models_contacts_row>();
                for (int i = 0; i < mDataset.size(); i++){

                    if ((mDataset.get(i).getmText1().toUpperCase()).contains(constraint.toString().toUpperCase()) ) {

                        models_contacts_row m = new models_contacts_row(mDataset.get(i).getmText1(), mDataset.get(i).getmText2(), mDataset.get(i).getmText3(), mDataset.get(i).getmID());

                        filterList.add(m);
                    }

                    else if ((mDataset.get(i).getmText3().toUpperCase()).contains(constraint.toString().toUpperCase())){
                        models_contacts_row m = new models_contacts_row(mDataset.get(i).getmText1(), mDataset.get(i).getmText2(), mDataset.get(i).getmText3(), mDataset.get(i).getmID());

                        filterList.add(m);
                    }
                    results.count = filterList.size();
                    results.values = filterList;}
            } else {
                results.count = mDataset.size();
                results.values = mDataset;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            modelItems = (List<models_contacts_row>) results.values;
            notifyDataSetChanged();
        }

    }




}