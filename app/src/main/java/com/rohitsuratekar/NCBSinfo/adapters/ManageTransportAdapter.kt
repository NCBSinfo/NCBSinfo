package com.rohitsuratekar.NCBSinfo.adapters

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.models.Route
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ManageTransportAdapter(private val routeList: List<Route>, private val listener: OnOptionClicked) :
    RecyclerView.Adapter<ManageTransportAdapter.ViewHolder>() {

    private val inputFormat = SimpleDateFormat(Constants.FORMAT_SERVER_TIMESTAMP, Locale.ENGLISH)
    private val outputFormat = SimpleDateFormat(Constants.FORMAT_MODIFIED, Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_manage_transport_item))
    }

    override fun getItemCount(): Int {
        return routeList.size
    }

    private fun format(string: String?): String {
        string?.let {
            return try {
                outputFormat.format(inputFormat.parse(it))
            } catch (e: ParseException) {
                "N/A"
            }

        }
        return "N/A"
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val route = routeList[position]
        val context = holder.itemView.context

        holder.apply {
            name.text = context.getString(R.string.tp_route_name, route.routeData.origin, route.routeData.destination)
                .toUpperCase()
            type.text = route.routeData.type
            modified.text = context.getString(R.string.mt_last_modified, format(route.routeData.modifiedOn))
        }

        if (route.isExpanded) {
            holder.apply {
                optionsLayout.showMe()
                expand.rotation = 0f
                routeIcon.setImageResource(R.color.colorAccent)
                setColor(context, edit.compoundDrawables, R.color.colorPrimary)
                setColor(context, delete.compoundDrawables, R.color.red)
                setColor(context, report.compoundDrawables, R.color.yellow)
                edit.setOnClickListener { listener.edit(route) }
                delete.setOnClickListener { listener.delete(route) }
                report.setOnClickListener { listener.report(route) }
            }

        } else {
            holder.apply {
                optionsLayout.hideMe()
                expand.rotation = 180f
                routeIcon.setImageResource(R.color.colorPrimary)
            }
        }

        holder.mainLayout.setOnClickListener { listener.expand(route) }
    }

    private fun setColor(context: Context, drawables: Array<Drawable>, color: Int) {
        drawables[1].mutate()
        drawables[1].setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_ATOP)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.mt_item_name)
        var type: TextView = itemView.findViewById(R.id.mt_item_type)
        var modified: TextView = itemView.findViewById(R.id.mt_item_modified)
        var edit: TextView = itemView.findViewById(R.id.mt_item_edit)
        var delete: TextView = itemView.findViewById(R.id.mt_item_delete)
        var report: TextView = itemView.findViewById(R.id.mt_item_report)
        var expand: ImageView = itemView.findViewById(R.id.mt_item_expand)
        var routeIcon: ImageView = itemView.findViewById(R.id.mt_item_route_icon)
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