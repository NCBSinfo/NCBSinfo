package com.rohitsuratekar.NCBSinfo.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.fragments.InformationFragment

class InformationAdapter(
    private val infoList: List<InformationFragment.Information>,
    private val listener: OnInfoClick
) : RecyclerView.Adapter<InformationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_information_item))
    }

    override fun getItemCount(): Int {
        return infoList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = infoList[position]
        val context = holder.itemView.context
        holder.apply {
            title.text = context.getString(info.title)
            details.text = context.getString(info.details)
            image.setImageResource(info.image)
            card.setOnClickListener { listener.sectionClicked(info.action) }
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var card: CardView = itemView.findViewById(R.id.info_card)
        var image: ImageView = itemView.findViewById(R.id.info_card_image)
        var title: TextView = itemView.findViewById(R.id.info_card_title)
        var details: TextView = itemView.findViewById(R.id.info_card_details)

    }

    interface OnInfoClick {
        fun sectionClicked(action: Int)
    }
}