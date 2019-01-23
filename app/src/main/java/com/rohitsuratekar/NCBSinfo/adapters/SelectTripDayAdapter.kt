package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.fragments.SelectTripDayFragment

class SelectTripDayAdapter(
    private val itemList: List<SelectTripDayFragment.SelectTripDayModel>,
    private val listener: OnDaySelected
) :
    RecyclerView.Adapter<SelectTripDayAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_select_trip_day_item))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val t = itemList[position]
        val context = holder.itemView.context
        holder.apply {
            title.text = context.getString(t.title)
            subtitle.text = context.getString(t.subtitle)
            note.text = t.note
            layout.setOnClickListener { listener.daySelected(t) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.et_sd_item_title)
        var subtitle: TextView = itemView.findViewById(R.id.et_sd_item_subtitle)
        var note: TextView = itemView.findViewById(R.id.et_sd_item_note)
        var layout: ConstraintLayout = itemView.findViewById(R.id.et_sd_item_layout)
    }

    interface OnDaySelected {
        fun daySelected(dayModel: SelectTripDayFragment.SelectTripDayModel)
    }
}