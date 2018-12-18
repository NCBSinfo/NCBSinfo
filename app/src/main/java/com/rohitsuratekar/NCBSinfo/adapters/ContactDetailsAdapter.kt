package com.rohitsuratekar.NCBSinfo.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.models.Contact

class ContactDetailsAdapter(private val contact: Contact, private val listener: OnCalled) :
    RecyclerView.Adapter<ContactDetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_contact_details_item))
    }

    override fun getItemCount(): Int {
        return contact.extraExtensions().size + 1
    }

    private fun formatExtension(string: String): Spannable {
        var converted = string
        if ((string.length == 4) and string.startsWith("6")) {
            converted = "+91 80 2366 $string"
        }
        if ((string.length == 4) and string.startsWith("5")) {
            converted = "+91 80 2308 $string"
        }
        val spannable = SpannableString(converted)
        val color = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.BLACK))
        val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, color, null)
        val startPos = converted.indexOf(string)
        val endPos = startPos + string.length
        spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0) {
            contact.primaryExtension?.let {
                holder.extension.text = formatExtension(it.trim())
            } ?: kotlin.run {
                holder.extension.text = "N/A"
            }

        } else {
            holder.extension.text = formatExtension(contact.extraExtensions()[position - 1].trim())
        }

        if (position % 2 == 0) {
            holder.layout.setBackgroundResource(R.color.extension_back)
        } else {
            holder.layout.setBackgroundResource(R.color.extension_back_2)
        }

        holder.icon.setOnClickListener {
            listener.calledNumber(holder.extension.text.toString())
        }


    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var extension: TextView = itemView.findViewById(R.id.ct_sheet_item_extension)
        var icon: ImageView = itemView.findViewById(R.id.ct_sheet_item_icon)
        var layout: ConstraintLayout = itemView.findViewById(R.id.ct_sheet_item_layout)
    }

    interface OnCalled {
        fun calledNumber(number: String)
    }
}