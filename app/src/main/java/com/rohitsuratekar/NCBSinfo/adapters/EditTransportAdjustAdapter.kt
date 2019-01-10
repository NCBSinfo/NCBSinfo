package com.rohitsuratekar.NCBSinfo.adapters

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.convertTimeFormat
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.common.showMe

class EditTransportAdjustAdapter(private val tripList: MutableList<String>, private val listener: OnTripAdjust) :
    RecyclerView.Adapter<EditTransportAdjustAdapter.ViewHolder>() {

    private var tripSelected: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_adjust_trip_item))
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            text.text = convertTimeFormat(tripList[position])
            text.typeface = Typeface.DEFAULT
            if ((position == 0) and tripSelected) {
                note.showMe()
                icon.showMe()
                note.text = holder.itemView.context.getString(R.string.et_first_trip)
                text.typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            } else {
                if (tripSelected) {
                    note.hideMe()
                    icon.hideMe()
                } else {
                    note.showMe()
                    icon.showMe()
                    note.text = "?"
                }
            }
            layout.setOnClickListener { listener.firstTripSelected(position) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var text: TextView = itemView.findViewById(R.id.et_adjust_item_text)
        var note: TextView = itemView.findViewById(R.id.et_adjust_item_note)
        var icon: ImageView = itemView.findViewById(R.id.et_adjust_item_icon)
        var layout: LinearLayout = itemView.findViewById(R.id.et_adjust_item_layout)

    }

    interface OnTripAdjust {
        fun firstTripSelected(position: Int)
    }

    fun tripSelected(value: Boolean) {
        tripSelected = value
        notifyDataSetChanged()
    }
}