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
import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.Database;
import com.rohitsuratekar.NCBSinfo.models.ContactRowModel;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.DataObjectHolder> implements Filterable {

    private ArrayList<ContactRowModel> mDataset;
    private MyClickListener myClickListener;
    private MyClickListener2 myClickListener2;
    ValueFilter valueFilter;
    private Context context;
    List<ContactRowModel> modelItems;


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
            menu.add(Menu.NONE, R.id.contact_list_call,Menu.NONE, v.getResources().getString(R.string.action_call));
            menu.add(Menu.NONE, R.id.contact_list_fav, Menu.NONE, v.getResources().getString(R.string.action_add_to_favorite));
            menu.add(Menu.NONE, R.id.contact_list_edit, Menu.NONE, v.getResources().getString(R.string.action_edit));
            menu.add(Menu.NONE, R.id.contact_list_delete, Menu.NONE, v.getResources().getString(R.string.action_delete));
        }

    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public void setOnItemClickListener2(MyClickListener2 myClickListener2) {
        this.myClickListener2 = myClickListener2;
    }

    public ContactAdapter(ArrayList<ContactRowModel> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        context= parent.getContext();
        return new DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final DataObjectHolder holder, final int position) {
        holder.ContactName.setText(Html.fromHtml(mDataset.get(position).getName()));
        holder.ContactNumber.setText(Html.fromHtml(mDataset.get(position).getNuber()));
        holder.ContactDepartment.setText(Html.fromHtml(mDataset.get(position).getDepartment()));

        String firstLetter =  holder.ContactName.getText().toString().substring(0,1).toUpperCase();
        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color1 = generator.getRandomColor();
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(firstLetter, color1);
        ImageView image = (ImageView) holder.itemView.findViewById(R.id.thumbnail);
        image.setImageDrawable(drawable);

        Database db2 = new Database(context);
        final int k = mDataset.get(position).getId();
        ImageButton img = (ImageButton) holder.itemView.findViewById(R.id.fav_button);
        img.setColorFilter(null);
        if (db2.getContact(k).getFavorite().equals("1")){
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
    public void addItem(ContactRowModel dataObj, int index) {
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
        void onItemClick(int position, View v);


    }

    public interface MyClickListener2 {
        void onItemClick(int position, View v);
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
                ArrayList<ContactRowModel> filterList = new ArrayList<ContactRowModel>();
                for (int i = 0; i < mDataset.size(); i++){

                    if ((mDataset.get(i).getName().toUpperCase()).contains(constraint.toString().toUpperCase()) ) {

                        ContactRowModel m = new ContactRowModel(mDataset.get(i).getName(), mDataset.get(i).getNuber(), mDataset.get(i).getDepartment(), mDataset.get(i).getId());

                        filterList.add(m);
                    }

                    else if ((mDataset.get(i).getDepartment().toUpperCase()).contains(constraint.toString().toUpperCase())){
                        ContactRowModel m = new ContactRowModel(mDataset.get(i).getName(), mDataset.get(i).getNuber(), mDataset.get(i).getDepartment(), mDataset.get(i).getId());

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
            modelItems = (List<ContactRowModel>) results.values;
            notifyDataSetChanged();
        }

    }




}