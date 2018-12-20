package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.models.Route

class ManageTransportAdapter(private val routeList: List<Route>, private val listener: OnOptionClicked) :
    RecyclerView.Adapter<ManageTransportAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_manage_transport_item))
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = routeList[position]
        holder.apply {
            origin.text = route.routeData.origin?.toUpperCase()
            destination.text = route.routeData.destination?.toUpperCase()
            type.text = route.routeData.type
        }
        if (route.isExpanded) {
            holder.optionsLayout.showMe()
            holder.expand.rotation = 0f
        } else {
            holder.optionsLayout.hideMe()
            holder.expand.rotation = 180f
        }

        holder.mainLayout.setOnClickListener { listener.expand(route) }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var origin: TextView = itemView.findViewById(R.id.mt_item_origin)
        var destination: TextView = itemView.findViewById(R.id.mt_item_destination)
        var type: TextView = itemView.findViewById(R.id.mt_item_type)
        var expand: ImageView = itemView.findViewById(R.id.mt_item_expand)
        var optionsLayout: LinearLayout = itemView.findViewById(R.id.mt_item_options)
        var mainLayout: ConstraintLayout = itemView.findViewById(R.id.mt_main_layout)

    }

    interface OnOptionClicked {
        fun expand(route: Route)
        fun edit(route: Route)
        fun delete(route: Route)
        fun report(route: Route)
    }

}