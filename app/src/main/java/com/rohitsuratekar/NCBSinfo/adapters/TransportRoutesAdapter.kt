package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.database.RouteData

class TransportRoutesAdapter(
    private val routeDataList: List<RouteData>,
    private var routeID: Int,
    private val listener: OnRouteClick
) :
    RecyclerView.Adapter<TransportRoutesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_transport_routes_item))
    }

    override fun getItemCount(): Int {
        return routeDataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context
        val route = routeDataList[position]

        holder.text.text = context.getString(R.string.tp_route_name, route.origin, route.destination).toUpperCase()
        holder.subText.text = route.type

        if (routeID == route.routeID) {
            holder.icon.setImageResource(R.color.green)
        }else{
            holder.icon.setImageResource(R.color.colorPrimary)
        }

        holder.layout.setOnClickListener {
            listener.clicked(route)
        }
    }

    fun setRouteID(routeID: Int) {
        this.routeID = routeID
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.tp_sheet_icon)
        var text: TextView = itemView.findViewById(R.id.tp_sheet_text)
        var subText: TextView = itemView.findViewById(R.id.tp_sheet_subtext)
        var layout: ConstraintLayout = itemView.findViewById(R.id.tp_sheet_layout)
    }

    interface OnRouteClick {
        fun clicked(routeData: RouteData)
    }


}