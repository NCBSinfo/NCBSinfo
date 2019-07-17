package com.rohitsuratekar.NCBSinfo.adapters

import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.hideMe
import com.rohitsuratekar.NCBSinfo.common.inflate
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.models.NextTrip
import com.rohitsuratekar.NCBSinfo.models.Route
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimetableAdapter(private var route: Route, private var currentCal: Calendar) :
    RecyclerView.Adapter<TimetableAdapter.ViewHolder>() {

    private val currentTrips = mutableListOf<String>()
    private var nextTripDay: Int = -1
    private var nextTripText: String = ""
    private var endingColor: Int = R.color.colorLight
    private var originalCal: Calendar = Calendar.getInstance()

    init {
        originalCal.timeInMillis = currentCal.timeInMillis
        updateList()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(parent.inflate(R.layout.fragment_timetable_item))
    }

    override fun getItemCount(): Int {
        return currentTrips.size + 1
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val context = holder.itemView.context

        if (position < currentTrips.size) {
            holder.layout.showMe()
            holder.itemText.text = convertFormat(currentTrips[position])

            holder.itemText.typeface = Typeface.DEFAULT
            holder.itemText.setTextColor(ContextCompat.getColor(context, R.color.black))
            holder.ending.hideMe()



            if ((nextTripDay == currentCal.get(Calendar.DAY_OF_WEEK)) and (currentTrips[position] == nextTripText)) {
                holder.extraText.showMe()
                holder.arrow.showMe()
                holder.extraText.text = context.getString(R.string.hm_next, route.routeData.type)

                if (route.routeData.type == "other") {
                    holder.extraText.text = context.getString(R.string.hm_next, "transport")
                }

                if (position == currentTrips.size - 1) {
                    endingColor = R.color.colorAccent
                }

                holder.frontImage.setImageResource(R.color.colorAccent)

            } else {
                holder.extraText.hideMe()
                holder.arrow.hideMe()
                holder.frontImage.setImageResource(R.color.colorLight)
            }

            if ((position < currentTrips.indexOf(nextTripText)) and (nextTripDay == currentCal.get(Calendar.DAY_OF_WEEK))) {
                holder.frontImage.setImageResource(R.color.colorPrimary)
                holder.itemText.setTextColor(ContextCompat.getColor(context, R.color.grey))
            }


        } else {
            holder.ending.showMe()
            holder.layout.hideMe()
            holder.ending.setColorFilter(ContextCompat.getColor(context, endingColor))
        }


    }

    private fun updateList() {
        val next = NextTrip(route.tripData).calculate(originalCal)
        nextTripDay = next.tripHighlightDay()
        nextTripText = next.raw()
        currentTrips.clear()
        currentTrips.addAll(route.tripList(currentCal))
        endingColor = R.color.colorLight

    }

    fun changeDay(cal: Calendar) {
        currentCal.timeInMillis = cal.timeInMillis
        updateList()
    }

    fun changeRoute(newRoute: Route) {
        route = newRoute
        updateList()
    }

    private fun convertFormat(input: String): String {
        val inputFormat = SimpleDateFormat(Constants.FORMAT_TRIP_LIST, Locale.ENGLISH)
        val outputFormat = SimpleDateFormat(Constants.FORMAT_DISPLAY_TIME, Locale.ENGLISH)
        val returnCal = Calendar.getInstance().apply { timeInMillis = currentCal.timeInMillis }
        val tempCal = Calendar.getInstance()
        try {
            tempCal.timeInMillis = inputFormat.parse(input).time
        } catch (e: ParseException) {
            Log.e("Trip", e.localizedMessage)
            return "--:--"
        }
        returnCal.set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY))
        returnCal.set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE))
        return outputFormat.format(Date(returnCal.timeInMillis))
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var frontImage: ImageView = itemView.findViewById(R.id.tp_item_front_image)
        var arrow: ImageView = itemView.findViewById(R.id.tp_item_arrow)
        var itemText: TextView = itemView.findViewById(R.id.tp_item_text)
        var extraText: TextView = itemView.findViewById(R.id.tp_item_extra_text)
        var ending: ImageView = itemView.findViewById(R.id.tp_item_ending)
        var layout: ConstraintLayout = itemView.findViewById(R.id.tp_sub_layout)
    }
}