package com.rohitsuratekar.NCBSinfo.activities.contact;

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

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    public ContactAdapter(List<ContactModel> items) {
        this.entryList = items;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contacts_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ContactModel entry = entryList.get(position);
        holder.name.setText(entry.getName());
        holder.number.setText(entry.getNumber());
        holder.icon.setImageResource(entry.getIcon());
    }

    @Override
    public int getItemCount() {
        return entryList.size();

    }


    private List<ContactModel> entryList;
    View currentview;
    private ClickListener myClickListener;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, number;
        public ImageView background, icon;
        public ConstraintLayout mainLayout;

        public MyViewHolder(final View view) {
            super(view);
            currentview = view;
            name = (TextView) view.findViewById(R.id.co_item_txt_name);
            number = (TextView) view.findViewById(R.id.co_item_txt_number);
            mainLayout = (ConstraintLayout) view.findViewById(R.id.co_item_layout);
            background = (ImageView) view.findViewById(R.id.co_item_back_image);
            icon = (ImageView) view.findViewById(R.id.co_item_icon);
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

    public interface ClickListener {
        void onItemClick(int position);
    }

}

