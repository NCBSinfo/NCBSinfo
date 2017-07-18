package com.rohitsuratekar.NCBSinfo.activities.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;
import com.secretbiology.helpers.general.Log;
import com.secretbiology.helpers.general.TimeUtils.ConverterMode;
import com.secretbiology.helpers.general.TimeUtils.DateConverter;

import java.text.ParseException;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by Rohit Suratekar on 19-06-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<HomeCardModel> modelList;
    private OnCardClick cardClick;
    private int currentFav = -1;
    private boolean onlyFirst = false;

    HomeAdapter(List<HomeCardModel> modelList) {
        this.modelList = modelList;
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final HomeViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        HomeCardModel model = modelList.get(position);
        holder.image.setImageResource(model.getImage());
        holder.back.setImageResource(model.getColor());
        holder.title.setText(context.getString(R.string.tp_route_name, model.getOrigin().toUpperCase(), model.getDestination().toUpperCase()));
        holder.subtitle.setText(model.getType().toUpperCase());
        try {
            holder.nextTrip.setText(DateConverter.changeFormat(ConverterMode.DATE_FIRST, model.getNextTrip(), "hh:mm a"));
        } catch (ParseException e) {
            Log.error(e.getMessage());
            holder.nextTrip.setText("--:--");
        }
        holder.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClick.onCardClick(holder.getLayoutPosition());
            }
        });
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClick.onCardClick(holder.getLayoutPosition());
            }
        });

        holder.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardClick.onFavoriteClick(holder.getLayoutPosition());
            }
        });
        //Need following to not send database change query
        if (model.isFavorite() && currentFav == -1) {
            holder.fav.setImageResource(R.drawable.icon_fav);
        } else if (currentFav == position) {
            holder.fav.setImageResource(R.drawable.icon_fav);
        } else {
            holder.fav.setImageResource(R.drawable.icon_fav_empty);
        }
    }

    boolean isOnlyFirst() {
        return onlyFirst;
    }

    void setOnlyFirst(boolean onlyFirst) {
        this.onlyFirst = onlyFirst;
    }

    @Override
    public int getItemCount() {
        if (onlyFirst) {
            return 1;
        } else {
            return modelList.size();
        }
    }

    class HomeViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle, nextTrip;
        ImageView image, back, fav;

        HomeViewHolder(View itemView) {
            super(itemView);
            image = ButterKnife.findById(itemView, R.id.hm_item_image);
            back = ButterKnife.findById(itemView, R.id.hm_item_back);
            title = ButterKnife.findById(itemView, R.id.hm_item_title);
            subtitle = ButterKnife.findById(itemView, R.id.hm_item_subtitle);
            nextTrip = ButterKnife.findById(itemView, R.id.hm_item_next_trip);
            fav = ButterKnife.findById(itemView, R.id.hm_item_fav);
        }
    }

    void setCurrentFav(int currentFav) {
        this.currentFav = currentFav;
    }

    void setOnCardClick(OnCardClick c) {
        cardClick = c;
    }

    interface OnCardClick {
        void onCardClick(int position);

        void onFavoriteClick(int position);
    }


}
