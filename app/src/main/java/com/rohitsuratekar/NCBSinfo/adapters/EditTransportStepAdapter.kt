package com.rohitsuratekar.NCBSinfo.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.common.invisible
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.models.EditTransportStep

class EditTransportStepAdapter(private val stepList: List<EditTransportStep>, private val listener: OnStepClick) :
    RecyclerView.Adapter<EditTransportStepAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.activity_edit_transport_step_item))
    }

    override fun getItemCount(): Int {
        return stepList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context: Context = holder.itemView.context
        val step = stepList[position]
        holder.apply {
            text.text = context.getString(step.text)
            icon.setImageResource(R.drawable.icon_information)
            if (step.isSeen) {
                ImageViewCompat.setImageTintList(
                    icon,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorPrimary))
                )
                right.setImageResource(R.color.colorPrimary)
                left.setImageResource(R.color.colorPrimary)
            } else {
                ImageViewCompat.setImageTintList(
                    icon,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.grey))
                )
                right.setImageResource(R.color.grey)
                left.setImageResource(R.color.grey)
            }

            if (step.isComplete) {
                icon.setImageResource(R.drawable.icon_check_circle)
                ImageViewCompat.setImageTintList(
                    icon,
                    ColorStateList.valueOf(ContextCompat.getColor(context, R.color.green))
                )
                right.setImageResource(R.color.green)
                left.setImageResource(R.color.green)

            }

            layout.setOnClickListener { listener.setClicked(step.number) }
            if (position == 0) {
                left.invisible()
            } else {
                left.showMe()
            }
            if (position == stepList.size - 1) {
                right.invisible()
            } else {
                right.showMe()
            }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.et_step_icon)
        var left: ImageView = itemView.findViewById(R.id.et_step_left_bar)
        var right: ImageView = itemView.findViewById(R.id.et_step_right_bar)
        var text: TextView = itemView.findViewById(R.id.et_step_text)
        var layout: ConstraintLayout = itemView.findViewById(R.id.et_step_layout)
    }

    interface OnStepClick {
        fun setClicked(step: Int)
    }
}