package com.rohitsuratekar.NCBSinfo.activities.manage;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.rohitsuratekar.NCBSinfo.common.Helper;
import com.rohitsuratekar.NCBSinfo.database.RouteData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Rohit Suratekar on 17-07-17 for NCBSinfo.
 * All code is released under MIT License.
 */

public class ManageTransportAdapter extends RecyclerView.Adapter<ManageTransportAdapter.MTViewHolder> {

    private List<RouteData> routeData;
    private int rotationAngle = 0;
    private OnOptionClick click;

    ManageTransportAdapter(List<RouteData> routeData) {
        this.routeData = routeData;
    }


    @NonNull
    @Override
    public MTViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MTViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.manage_transport_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MTViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        RouteData data = routeData.get(position);

        setColor(context, holder.edit.getCompoundDrawables(), R.color.green);
        setColor(context, holder.delete.getCompoundDrawables(), R.color.red);
        setColor(context, holder.report.getCompoundDrawables(), R.color.colorPrimary);
        String modifyDate;
        if (data.getModifiedOn() == null) {
            modifyDate = "N/A";
        } else {
            try {
                SimpleDateFormat format = new SimpleDateFormat(Helper.FORMAT_TIMESTAMP, Locale.ENGLISH);
                Date d = format.parse(data.getModifiedOn());
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis(d.getTime());
                modifyDate = Helper.convertToString(c, "dd MMM yy");
            } catch (ParseException e) {
                modifyDate = "--:--";
            }
        }
        holder.lastModified.setText(context.getString(R.string.mt_last_modified, modifyDate));
        holder.title.setText(context.getString(R.string.tp_route_name, data.getOrigin().toUpperCase(), data.getDestination().toUpperCase()));
        holder.subtitle.setText(data.getType());
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.edit(holder.getAdapterPosition());
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.delete(holder.getLayoutPosition());
            }
        });
        holder.report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.report(holder.getLayoutPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return routeData.size();
    }

    class MTViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, subtitle;
        LinearLayout options;
        TextView delete, edit, report, lastModified;
        ImageView expand;

        private int originalHeight = 0;
        private boolean isViewExpanded = false;

        MTViewHolder(View itemView) {
            super(itemView);
            expand = itemView.findViewById(R.id.mt_expand);
            title = itemView.findViewById(R.id.mt_item_title);
            subtitle = itemView.findViewById(R.id.mt_item_subtitle);
            options = itemView.findViewById(R.id.mt_item_options);
            delete = itemView.findViewById(R.id.mt_item_delete);
            edit = itemView.findViewById(R.id.mt_item_edit);
            report = itemView.findViewById(R.id.mt_item_report);
            lastModified = itemView.findViewById(R.id.mt_item_modified);

            itemView.setOnClickListener(this);
            isViewExpanded = itemView.isSelected();
            if (!isViewExpanded) {
                // Set Views to View.GONE and .setEnabled(false)
                options.setEnabled(false);
                options.setVisibility(View.GONE);
            }

        }

        @Override
        public void onClick(final View v) {

            if (originalHeight == 0) {
                originalHeight = v.getHeight();
            }

            // Declare a ValueAnimator object
            ValueAnimator valueAnimator;
            if (!isViewExpanded) {
                options.setVisibility(View.VISIBLE);
                options.setEnabled(true);
                isViewExpanded = true;
                valueAnimator = ValueAnimator.ofInt(originalHeight, (int) Math.round(originalHeight * 1.8)); // These values in this method can be changed to expand however much you like
            } else {
                isViewExpanded = false;
                valueAnimator = ValueAnimator.ofInt((int) Math.round(originalHeight * 1.8), originalHeight);

                Animation a = new AlphaAnimation(1.00f, 0.00f); // Fade out

                a.setDuration(300);
                // Set a listener to the animation and configure onAnimationEnd
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        options.setEnabled(false);
                        options.setVisibility(View.GONE);

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                // Set the animation on the custom view
                options.startAnimation(a);
            }
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    v.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                    v.requestLayout();
                }
            });
            valueAnimator.start();

            ObjectAnimator anim = ObjectAnimator.ofFloat(expand, "rotation", rotationAngle, rotationAngle + 180);
            anim.setDuration(500);
            anim.start();
            rotationAngle += 180;
            rotationAngle = rotationAngle % 360;

        }

    }

    private void setColor(Context context, Drawable[] drawables, int color) {
        if (drawables[1] != null) {  // 1 is for top drawable
            drawables[1].mutate();
            drawables[1].setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    void setOnOptionClick(OnOptionClick c) {
        click = c;
    }

    interface OnOptionClick {
        void edit(int position);

        void delete(int position);

        void report(int position);
    }


}
