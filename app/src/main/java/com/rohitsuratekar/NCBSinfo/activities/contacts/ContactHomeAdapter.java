package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.database.models.ContactModel;

import java.util.List;

public class ContactHomeAdapter extends RecyclerView.Adapter<ContactHomeAdapter.MyViewHolder> {

    private List<ContactModel> entryList;
    private int currentItem;
    View currentview;
    private ClickListener myClickListener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;
        public ImageView icon;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
            name = (TextView) view.findViewById(R.id.contact_hm_name);
            number = (TextView) view.findViewById(R.id.contact_hm_number);
            icon = (ImageView) view.findViewById(R.id.contact_hm_icon);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.contact_hm_layout);
            context = view.getContext();
            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myClickListener.onItemClick(getLayoutPosition());
                }
            });
        }
    }

    public void setOnItemClickListener(ClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public ContactHomeAdapter(List<ContactModel> entryList) {
        this.entryList = entryList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContactModel entry = entryList.get(position);
        holder.name.setText(entry.getName());
        holder.number.setText(entry.getNumber());
        if (entry.getIcon() != 0) {
            holder.icon.setImageResource(entry.getIcon());
        } else {
            holder.icon.setImageResource(R.drawable.icon_account);
        }
    }

    public void setCurrentItem(int item) {
        this.currentItem = item;
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public interface ClickListener {
        void onItemClick(int position);
    }


}
