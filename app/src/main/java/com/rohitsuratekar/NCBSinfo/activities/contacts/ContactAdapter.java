package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.List;

/**
 * Created by Rohit Suratekar on 19-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactHolder> {

    private List<ContactModel> modelList;

    ContactAdapter(List<ContactModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContactHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_item, parent, false));
    }

    private Spannable getSpan(String original, String search) {
        Spannable spannable = new SpannableString(original);
        ColorStateList blackColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.BLACK});
        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blackColor, null);
        int startPos = original.toLowerCase().indexOf(search.toLowerCase());
        int endPos = startPos + search.length();
        spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    @Override
    public void onBindViewHolder(ContactHolder holder, int position) {
        Context context = holder.itemView.getContext();
        ContactModel model = modelList.get(position);
        holder.name.setText(model.getName());
        holder.primary.setText(model.getPrimaryExtension());

        if (model.getSearchString().length() != 0) {
            for (ContactModel.AREA area : model.getSearchArea()) {
                switch (area) {
                    case NAME:
                        holder.name.setText(getSpan(model.getName(), model.getSearchString()));
                        break;
                    case EXTENSION:
                        holder.primary.setText(getSpan(model.getPrimaryExtension(), model.getSearchString()));
                        break;
                }
            }

        }


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class ContactHolder extends RecyclerView.ViewHolder {

        TextView name, primary;

        ContactHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.ct_list_name);
            primary = itemView.findViewById(R.id.ct_list_primary_number);
        }
    }
}
