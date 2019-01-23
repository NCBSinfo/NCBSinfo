package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.ACTION_APP_DETAILS
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.VIEW_HEADER
import com.rohitsuratekar.NCBSinfo.common.SettingsActions.VIEW_ITEM
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.models.SettingsItem

class SettingsAdapter(private val itemList: List<SettingsItem>, private val listener: OnSettingItemClicked) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_HEADER -> Header(parent.inflate(R.layout.fragment_settings_header))
            VIEW_ITEM -> ItemHolder(parent.inflate(R.layout.fragment_settings_item))
            else -> Line(parent.inflate(R.layout.fragment_settings_line))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return itemList[position].viewType
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (item.viewType) {
            VIEW_ITEM -> {
                (holder as ItemHolder).apply {

                    icon.setImageResource(android.R.color.transparent)
                    icon.setBackgroundResource(android.R.color.transparent)
                    title.alpha = 1f
                    details.alpha = 0.54f
                    icon.alpha = 1f

                    title.text = item.title
                    details.text = item.description
                    if (item.icon != 0) {
                        icon.setImageResource(item.icon)
                    }
                    if (item.action == ACTION_APP_DETAILS) {
                        icon.setImageResource(android.R.color.transparent)
                        icon.setBackgroundResource(R.mipmap.ic_launcher_round)
                    }
                    if (item.isDisabled) {
                        title.alpha = 0.54f
                    }

                    layout.setOnClickListener { listener.itemClicked(item.action) }

                }
            }
            VIEW_HEADER -> {
                (holder as Header).apply {
                    header.text = item.title
                }
            }
        }

    }


    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.st_item_title)
        var details: TextView = itemView.findViewById(R.id.st_item_details)
        var icon: ImageView = itemView.findViewById(R.id.st_item_icon)
        var layout: ConstraintLayout = itemView.findViewById(R.id.st_item_layout)
    }

    inner class Header(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var header: TextView = itemView.findViewById(R.id.st_item_header)
    }

    inner class Line(itemView: View) : RecyclerView.ViewHolder(itemView)

    interface OnSettingItemClicked {
        fun itemClicked(action: Int)
    }
}