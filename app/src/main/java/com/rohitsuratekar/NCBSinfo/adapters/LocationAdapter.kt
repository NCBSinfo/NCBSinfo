package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.models.Location

class LocationAdapter(private val locationList: List<Location>) : RecyclerView.Adapter<LocationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_location_item))
    }

    override fun getItemCount(): Int {
        return locationList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val loc = locationList[position]

        holder.apply {
            name.text = loc.name
            oldName.text = loc.oldName
            details.text = loc.details
            floor.text = loc.floor.toString()
            building.text = loc.building
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.loc_item_name)
        var oldName: TextView = itemView.findViewById(R.id.loc_item_old_name)
        var details: TextView = itemView.findViewById(R.id.loc_item_details)
        var floor: TextView = itemView.findViewById(R.id.loc_item_floor)
        var building: TextView = itemView.findViewById(R.id.loc_item_building)
        var icon: ImageView = itemView.findViewById(R.id.loc_item_icon)
    }
}