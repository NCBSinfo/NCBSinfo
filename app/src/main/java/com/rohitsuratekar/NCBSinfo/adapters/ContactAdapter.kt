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

class ContactAdapter(private var contactList: List<Contact>, private val listener: OnContactSelect) :
    RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_contacts_item))
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val con = contactList[position]
        holder.name.text = con.name
        holder.extension.text = con.primaryExtension

        if (con.searchString.isNotEmpty()) {
            for (a in con.searchArea) {
                when (a) {
                    Contact.AREA.NAME -> {
                        holder.name.text = getSpan(con.name, con.searchString)
                    }
                    Contact.AREA.EXTENSION -> {
                        holder.extension.text = getSpan(con.primaryExtension, con.searchString)
                    }
                    else -> {

                    }
                }
            }
        }

        con.type?.let {
            holder.icon.setImageResource(getIcon(it))
        }

        holder.layout.setOnClickListener {
            listener.contactSelected(con)
        }
    }

    private fun getSpan(original: String?, search: String): Spannable {
        val spannable = SpannableString(original)
        original?.let {
            val color = ColorStateList(arrayOf(intArrayOf()), intArrayOf(Color.RED))
            val highlightSpan = TextAppearanceSpan(null, Typeface.BOLD, -1, color, null)
            val startPos = it.toLowerCase().indexOf(search.toLowerCase())
            val endPos = startPos + search.length
            spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return spannable
    }

    private fun getIcon(type: String): Int {
        return when (type.toLowerCase()) {
            "emergency" -> R.drawable.icon_star
            "department" -> R.drawable.icon_department
            "facility" -> R.drawable.icon_facility
            "office" -> R.drawable.icon_office
            else -> R.drawable.icon_contacts
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var icon: ImageView = itemView.findViewById(R.id.ct_icon)
        var name: TextView = itemView.findViewById(R.id.ct_list_name)
        var extension: TextView = itemView.findViewById(R.id.ct_list_primary_number)
        var layout: ConstraintLayout = itemView.findViewById(R.id.ct_layout)
    }

    interface OnContactSelect {
        fun contactSelected(contact: Contact)
    }
}