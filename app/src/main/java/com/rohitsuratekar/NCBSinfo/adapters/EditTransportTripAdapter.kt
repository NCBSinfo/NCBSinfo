package com.rohitsuratekar.NCBSinfo.adapters

import android.graphics.Color
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.common.showMe
import java.util.*

private const val PENDING_REMOVAL_TIMEOUT = 1000 // 1sec

class EditTransportTripAdapter(private val items: MutableList<String>) :
    RecyclerView.Adapter<EditTransportTripAdapter.ViewHolder>() {

    private val itemsPendingRemoval = mutableListOf<String>()
    private var undoOn: Boolean = false // is undo on, you can turn it on from the toolbar menu

    private val handler = Handler() // hanlder for running delayed runnables
    private val pendingRunnable =
        HashMap<String, Runnable>() // map of items to pending runnables, so we can cancel a removal if need be


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_add_trips_item))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            if (itemsPendingRemoval.contains(item)) {
                // we need to show the "undo" state of the row
                itemView.setBackgroundColor(Color.RED)
                titleTextView.hideMe()
                undoButton.showMe()
                undoButton.setOnClickListener {
                    // user wants to undo the removal, let's cancel the pending task
                    val pendingRemovalRunnable = pendingRunnable[item]
                    pendingRunnable.remove(item)
                    if (pendingRemovalRunnable != null)
                        handler.removeCallbacks(pendingRemovalRunnable)
                    itemsPendingRemoval.remove(item)
                    // this will rebind the row in "normal" state
                    notifyItemChanged(items.indexOf(item))
                }
            } else {
                // we need to show the "normal" state
                itemView.setBackgroundColor(Color.WHITE)
                titleTextView.showMe()
                titleTextView.text = item

                undoButton.hideMe()
                undoButton.setOnClickListener(null)
            }
        }
    }

    fun setUndoOn(undoOn: Boolean) {
        this.undoOn = undoOn
    }

    fun isUndoOn(): Boolean {
        return undoOn
    }

    fun pendingRemoval(position: Int) {
        val item = items[position]
        if (!itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.add(item)
            // this will redraw row in "undo" state
            notifyItemChanged(position)
            // let's create, store and post a runnable to remove the item
            val pendingRemovalRunnable = Runnable { remove(items.indexOf(item)) }
            handler.postDelayed(pendingRemovalRunnable, PENDING_REMOVAL_TIMEOUT.toLong())
            pendingRunnable[item] = pendingRemovalRunnable
        }
    }

    fun remove(position: Int) {
        val item = items[position]
        if (itemsPendingRemoval.contains(item)) {
            itemsPendingRemoval.remove(item)
        }
        if (items.contains(item)) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun isPendingRemoval(position: Int): Boolean {
        val item = items[position]
        return itemsPendingRemoval.contains(item)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.et_trip_row_text)
        var undoButton: Button = itemView.findViewById(R.id.et_trip_row_undo)
    }
}