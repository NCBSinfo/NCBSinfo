package com.rohitsuratekar.NCBSinfo.adapters

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate

class SettingsDialogAdapter(
    private val itemList: List<String>,
    private val currentFav: Int,
    private val listener: OnRouteSelected
) :
    RecyclerView.Adapter<SettingsDialogAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_settings_dialog_item))
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            title.text = itemList[position]
            if (position == currentFav) {
                title.typeface = Typeface.DEFAULT_BOLD
                title.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.colorAccent))
            } else {
                title.typeface = Typeface.DEFAULT
                title.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.grey))
            }
            layout.setOnClickListener { listener.routeSelected(position) }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.st_dialog_item_text)
        var layout: LinearLayout = itemView.findViewById(R.id.st_dialog_item_layout)
    }

    interface OnRouteSelected {
        fun routeSelected(position: Int)
    }
}