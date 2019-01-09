package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.inflate

class EditTransportAdjustAdapter(private val tripList: MutableList<String>) :
    RecyclerView.Adapter<EditTransportAdjustAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_add_trips_item))
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            undoButton.hideMe()
            titleTextView.text = tripList[position]
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView = itemView.findViewById(R.id.et_trip_row_text)
        var undoButton: Button = itemView.findViewById(R.id.et_trip_row_undo)
    }
}