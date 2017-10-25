package com.rohitsuratekar.NCBSinfo.activities.contacts;

import android.graphics.Typeface;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.General;

import java.util.List;

/**
 * Created by Rohit Suratekar on 20-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ContactFragmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ContactDetailsModel> models;
    private OnCallClick callClick;

    ContactFragmentAdapter(List<ContactDetailsModel> models, OnCallClick callClick) {
        this.models = models;
        this.callClick = callClick;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (models.get(viewType).isHeader()) {
            return new Header(LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_sheet_header, parent, false));
        } else {
            return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contacts_sheet_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ContactDetailsModel model = models.get(position);
        if (model.isHeader()) {
            Header header = (Header) holder;
            header.title.setText(model.getName());
            if (model.hasSubHeader()) {
                header.details.setVisibility(View.VISIBLE);
                header.details.setText(model.getDetails());

            } else {
                header.details.setVisibility(View.GONE);
            }

        } else {
            final Holder detail = (Holder) holder;
            detail.text.setText(getSpan(model.getName()));
            if (position % 2 == 0) {
                detail.layout.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.contactExtension));
            } else {
                detail.layout.setBackgroundColor(0x00000000);
            }

            detail.text.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    General.copyToClipBoardWithToast(holder.itemView.getContext(), detail.text.getText().toString());
                    return false;
                }
            });

            detail.call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callClick.onclick(detail.text.getText().toString());
                }
            });
        }
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return models.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        TextView text;
        ImageView call;

        Holder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.ct_sheet_item_text);
            layout = itemView.findViewById(R.id.ct_sheet_item_layout);
            call = itemView.findViewById(R.id.ct_sheet_item_call);
        }
    }

    class Header extends RecyclerView.ViewHolder {
        TextView title, details;

        Header(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.ct_sheet_header_title);
            details = itemView.findViewById(R.id.ct_sheet_header_details);
        }
    }

    private Spannable getSpan(String original) {

        String preText = "+910 802 366 ";
        if (original.length() > 4) {
            preText = "";
        }
        String givenText = preText + original;
        Spannable spannable = new SpannableString(givenText);
        TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, null, null);
        int startPos = preText.length();
        int endPos = givenText.length();
        spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannable;
    }

    interface OnCallClick {
        void onclick(String call);
    }

}
