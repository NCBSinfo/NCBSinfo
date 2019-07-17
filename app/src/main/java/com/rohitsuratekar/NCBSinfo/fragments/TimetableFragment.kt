package com.rohitsuratekar.NCBSinfo.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.rohitsuratekar.NCBSinfo.R
import com.rohitsuratekar.NCBSinfo.adapters.TimetableAdapter
import com.rohitsuratekar.NCBSinfo.common.Constants
import com.rohitsuratekar.NCBSinfo.common.invisible
import com.rohitsuratekar.NCBSinfo.common.showMe
import com.rohitsuratekar.NCBSinfo.models.MyFragment
import com.rohitsuratekar.NCBSinfo.models.NextTrip
import com.rohitsuratekar.NCBSinfo.models.Route
import com.rohitsuratekar.NCBSinfo.models.Trip
import com.rohitsuratekar.NCBSinfo.viewmodels.TimetableViewModel
import kotlinx.android.synthetic.main.fragment_timetable.*
import java.text.SimpleDateFormat
import java.util.*

class TimetableFragment : MyFragment() {


    private lateinit var viewModel: TimetableViewModel
    private var adapter: TimetableAdapter? = null
    private var routeID: Int = 0
    private lateinit var currentCalendar: Calendar
    private var currentRoute: Route? = null
    private var next: Trip? = null
    private var swapRoute: Route? = null
    private val dayList = mutableListOf<TextView>()
    private val linkList = mutableListOf<ImageView>()
    private val calDays = listOf(
        Calendar.SUNDAY,
        Calendar.MONDAY,
        Calendar.TUESDAY,
        Calendar.WEDNESDAY,
        Calendar.THURSDAY,
        Calendar.FRIDAY,
        Calendar.SATURDAY
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timetable, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentCalendar = Calendar.getInstance()
        viewModel = ViewModelProviders.of(this).get(TimetableViewModel::class.java)
        sharedModel.currentRoute.value?.let {
            routeID = it.routeData.routeID
        }
        viewModel.fetchRoute(repository, routeID)
        callback?.showProgress()

        dayList.addAll(listOf(tp_day_sun, tp_day_mon, tp_day_tue, tp_day_wed, tp_day_thu, tp_day_fri, tp_day_sat))
        linkList.addAll(listOf(tp_link_0, tp_link_1, tp_link_2, tp_link_3, tp_link_4, tp_link_5, tp_link_6))
        subscribe()

        tp_show_all_btn.setOnClickListener {
            currentRoute?.routeData?.routeID?.let { id ->
                callback?.showRouteList(id)
            } ?: kotlin.run {
                callback?.showRouteList(0)
            }
        }

        tp_swap.setOnClickListener {
            swapRoute?.let { r ->
                routeID = r.routeData.routeID
                viewModel.fetchRoute(repository, routeID)
            }
        }

        tp_manage.setOnClickListener {
            currentRoute?.routeData?.routeID?.let { id ->
                val arg = TimetableFragmentDirections.actionTimetableFragmentToEditTransport().setRouteNo(id)
                callback?.editRoute(arg)
            }

        }

        for (i in 0 until dayList.size) {
            dayList[i].setOnClickListener {
                currentCalendar.set(Calendar.DAY_OF_WEEK, calDays[i])
                adapter?.changeDay(currentCalendar)
                adapter?.notifyDataSetChanged()
                updateUI()
            }
        }

    }

    private fun setupRecycler() {
        tp_recycler.adapter = adapter
        tp_recycler.layoutManager = LinearLayoutManager(context)
        adapter?.notifyDataSetChanged()
    }

    private fun subscribe() {
        sharedModel.currentRoute.observe(this, Observer {
            routeID = it.routeData.routeID
            viewModel.fetchRoute(repository, routeID)
        })
        viewModel.returnedRoute.observe(this, Observer { route ->
            currentRoute = route
            currentCalendar = Calendar.getInstance()
            next = NextTrip(route.tripData).calculate(Calendar.getInstance())
            if (adapter == null) {
                routeID = route.routeData.routeID
                adapter = TimetableAdapter(route, currentCalendar)
                setupRecycler()
            } else {
                adapter?.changeRoute(route)
                adapter?.changeDay(currentCalendar)
                adapter?.notifyDataSetChanged()
            }
            updateUI()
        })

        viewModel.reverseRoute.observe(this, Observer {
            swapRoute = it
        })
    }

    private fun updateUI() {
        callback?.hideProgress()
        currentRoute?.let {
            tp_origin.text = it.routeData.origin?.toUpperCase(Locale.getDefault())
            tp_destination.text = it.routeData.destination?.toUpperCase(Locale.getDefault())
            tp_type.text = getString(R.string.tp_type, it.routeData.type)
            if (it.routeData.type == "other") {
                tp_type.text = getString(R.string.tp_type, "transport")
            }
            tp_creation_date.text = getString(R.string.tp_update_on, formatDate(it.routeData.modifiedOn))
        }

        swapRoute?.let {
            tp_swap.showMe()
        } ?: kotlin.run {
            tp_swap.invisible()
        }

        val today = currentCalendar.get(Calendar.DAY_OF_WEEK)

        for (i in 0 until dayList.size) {
            if (today == calDays[i]) {
                dayList[i].setBackgroundResource(R.color.colorPrimary)
                linkList[i].setImageResource(R.color.colorPrimary)
                dayList[i].setTextColor(ContextCompat.getColor(context!!, R.color.white))
            } else {
                dayList[i].setBackgroundResource(R.color.colorLight)
                dayList[i].setTextColor(ContextCompat.getColor(context!!, R.color.black))
            }

            if (calDays[i] < today + 1) {
                linkList[i].setImageResource(R.color.colorPrimary)
            } else {
                linkList[i].setImageResource(android.R.color.transparent)
            }
        }

        if (today != next?.tripHighlightDay()) {
            tp_linker.setImageResource(R.color.colorLight)
        } else {
            tp_linker.setImageResource(R.color.colorPrimary)
        }

    }

    private fun formatDate(input: String?): String {
        input?.let {
            val readFormat = SimpleDateFormat(Constants.FORMAT_SERVER_TIMESTAMP, Locale.ENGLISH)
            val outFormat = SimpleDateFormat("dd MMM yy", Locale.ENGLISH)
            return try {
                val d = readFormat.parse(input)
                outFormat.format(d)
            } catch (e: Exception) {
                "N/A"
            }
        } ?: kotlin.run {
            return "N/A"
        }
    }

}
