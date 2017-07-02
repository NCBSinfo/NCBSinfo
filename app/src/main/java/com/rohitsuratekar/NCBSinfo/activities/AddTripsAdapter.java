package com.rohitsuratekar.NCBSinfo.activities;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.rohitsuratekar.NCBSinfo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class AddTripsAdapter extends RecyclerView.Adapter {
    private static final int PENDING_REMOVAL_TIMEOUT = 2000; // 3sec

    private List<String> items;
    private List<String> itemsPendingRemoval;
    private boolean undoOn; // is undo on, you can turn it on from the toolbar menu

    private Handler handler = new Handler(); // hanlder for running delayed runnables
    private HashMap<String, Runnable> pendingRunnable = new HashMap<>(); // map of items to pending runnables, so we can cancel a removal if need be

    AddTripsAdapter() {
        items = new ArrayList<>();
        itemsPendingRemoval = new ArrayList<>();
        // let's generate some items
        int lastInsertedIndex = 15;
        // this should give us a couple of screens worth
        for (int i = 1; i <= lastInsertedIndex; i++) {
            items.add("Item " + i);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder viewHolder = (ItemViewHolder) holder;
        final String item = items.get(position);

        if (itemsPendingRemoval.contains(item)) {
            // we need to show the "undo" state of the row
            viewHolder.itemView.setBackgroundColor(Color.RED);
            viewHolder.titleTextView.setVisibility(View.GONE);
            viewHolder.undoButton.setVisibility(View.VISIBLE);
            viewHolder.undoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // user wants to undo the removal, let's cancel the pending task
                    Runnable pendingRemovalRunnable = pendingRunnable.get(item);
                    pendingRunnable.remove(item);
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable);
                    itemsPendingRemoval.remove(item);
                    // this will rebind the row in "normal" state
                    notifyItemChanged(items.indexOf(item));
                }
            });
        } else {
            // we need to show the "normal" state
            viewHolder.itemView.setBackgroundColor(Color.WHITE);
            viewHolder.titleTextView.setVisibility(View.VISIBLE);
            viewHolder.titleTextView.setText(item);
            viewHolder.undoButton.setVisibility(View.GONE);
            viewHolder.undoButton.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void setUndoOn(boolean undoOn) {
        this.undoOn = undoOn;
    }

    public boolean isUndoOn() {
        return undoOn;
    }

    public void pendingRemoval(int position) {
        final String item = items.get(position);
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item);
            // this will redraw row in "undo" state
            notifyItemChanged(position);
            // let's create, store and post a runnable to remove the item
            Runnable pendingRemovalRunnable = new Runnable() {
                @Override
                public void run() {
                    remove(items.indexOf(item));
                }
            };
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT);
            pendingRunnable.put(item, pendingRemovalRunnable);
        }
    }

    public void remove(int position) {
        String item = items.get(position);
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item);
        }
        if (items.contains(item)) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    public boolean isPendingRemoval(int position) {
        String item = items.get(position);
        return itemsPendingRemoval.contains(item);
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        Button undoButton;

        ItemViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_transport_row_view, parent, false));
            titleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            undoButton = (Button) itemView.findViewById(R.id.undo_button);
        }

    }
}
